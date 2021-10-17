package tfar.fishbeatgameforme.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistrySpecificAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import tfar.fishbeatgameforme.FishBeatGameForMe;
import tfar.fishbeatgameforme.Hooks;
import tfar.fishbeatgameforme.KeyBind;
import tfar.fishbeatgameforme.network.C2SKeybindPacket;

public class Client implements ClientModInitializer {

    public static KeyMapping SUMMON_FISH = new KeyMapping(FishBeatGameForMe.MODID, GLFW.GLFW_KEY_I,"summon_fish");

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SUMMON_FISH);
        ClientTickEvents.START_CLIENT_TICK.register(Client::keyPressed);
        ModelPredicateProviderRegistrySpecificAccessor.callRegister(FishBeatGameForMe.SPECIAL_COMPASS,new ResourceLocation("angle"),CompassModelPredicate.FUNCTION);
        ModelPredicateProviderRegistrySpecificAccessor.callRegister(FishBeatGameForMe.SPECIAL_FISHING_ROD,new ResourceLocation("cast"),(itemStack, clientLevel, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                boolean bl = livingEntity.getMainHandItem() == itemStack;
                boolean bl2 = livingEntity.getOffhandItem() == itemStack;
                if (livingEntity.getMainHandItem().getItem() instanceof FishingRodItem) {
                    bl2 = false;
                }

                return (bl || bl2) && livingEntity instanceof Player && ((Player)livingEntity).fishing != null ? 1.0F : 0.0F;
            }
        });
    }

    public static void keyPressed(Minecraft client) {
        if (SUMMON_FISH.consumeClick()) {
            C2SKeybindPacket.send(
                    KeyBind.SUMMON_FISH
            );
        }
    }

    public static boolean renderRodXp(PoseStack poseStack, int xStart, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        ItemStack held = player.getMainHandItem();
        if (held.getItem() == FishBeatGameForMe.SPECIAL_FISHING_ROD) {
            renderAltBar(mc,held,poseStack,xStart,screenWidth,screenHeight);
            return true;
        }

        return false;
    }

    private static void renderAltBar(Minecraft minecraft, ItemStack held, PoseStack poseStack, int xStart, int screenWidth, int screenHeight) {
        minecraft.getProfiler().push("expBar");
        int xpPoints = held.hasTag() ? held.getTag().getInt("xp") : 0;
        if (xpPoints == 0)return;
        minecraft.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
        int j = Hooks.getXpNeededForNextLevel(xpPoints);
        int n;
        int o;
        if (j > 0) {
            n = (int)(Hooks.getProgress(xpPoints) * 183.0F);
            o = screenHeight - 32 + 3;
            minecraft.gui.blit(poseStack, xStart, o, 0, 64, 182, 5);
            if (n > 0) {
                minecraft.gui.blit(poseStack, xStart, o, 0, 69, n, 5);
            }
        }

        minecraft.getProfiler().pop();
        if (Hooks.getLevels(xpPoints) > 0) {
            minecraft.getProfiler().push("expLevel");
            String string = "" + Hooks.getLevels(xpPoints);
            n = (screenWidth - minecraft.gui.getFont().width(string)) / 2;
            o = screenHeight - 31 - 4;
            minecraft.gui.getFont().draw(poseStack, string, (float)(n + 1), (float)o, 0);
            minecraft.gui.getFont().draw(poseStack, string, (float)(n - 1), (float)o, 0);
            minecraft.gui.getFont().draw(poseStack, string, (float)n, (float)(o + 1), 0);
            minecraft.gui.getFont().draw(poseStack, string, (float)n, (float)(o - 1), 0);
            minecraft.gui.getFont().draw(poseStack, string, (float)n, (float)o, 0x80ff20);
            minecraft.getProfiler().pop();
        }
    }

}
