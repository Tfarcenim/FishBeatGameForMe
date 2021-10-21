package tfar.fishbeatgameforme.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistrySpecificAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;
import tfar.fishbeatgameforme.FishBeatGameForMe;
import tfar.fishbeatgameforme.Hooks;
import tfar.fishbeatgameforme.KeyBind;
import tfar.fishbeatgameforme.network.C2SKeybindPacket;

public class Client implements ClientModInitializer {

    public static KeyMapping SUMMON_FISH = new KeyMapping("summon_fish", GLFW.GLFW_KEY_I,FishBeatGameForMe.MODID);

    public static KeyMapping SUMMON_TSUNAMI = new KeyMapping("summon_tsunami", GLFW.GLFW_KEY_O,FishBeatGameForMe.MODID);

    public static KeyMapping GROW = new KeyMapping("grow", GLFW.GLFW_KEY_EQUAL,FishBeatGameForMe.MODID);
    public static KeyMapping SHRINK = new KeyMapping("shrink", GLFW.GLFW_KEY_MINUS,FishBeatGameForMe.MODID);


    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SUMMON_FISH);
        KeyBindingHelper.registerKeyBinding(SUMMON_TSUNAMI);
        KeyBindingHelper.registerKeyBinding(GROW);
        KeyBindingHelper.registerKeyBinding(SHRINK);
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
        EntityRendererRegistry.INSTANCE.register(FishBeatGameForMe.WATER_BOLT,(manager, context) -> new WaterBoltRenderer(manager,Minecraft.getInstance().getItemRenderer()));
    }

    public static void keyPressed(Minecraft client) {
        if (SUMMON_FISH.consumeClick()) {
            C2SKeybindPacket.send(
                    KeyBind.SUMMON_FISH
            );
        }
        if (SUMMON_TSUNAMI.consumeClick()) {
            C2SKeybindPacket.send(
                    KeyBind.SUMMON_TSUNAMI
            );
        }

        if (GROW.consumeClick()) {
            C2SKeybindPacket.send(
                    KeyBind.GROW
            );
        }

        if (SHRINK.consumeClick()) {
            C2SKeybindPacket.send(
                    KeyBind.SHRINK
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

    public static void spawnCustomEntity(ClientboundAddEntityPacket clientboundAddEntityPacket, Entity entity, ClientLevel level, Minecraft minecraft, double d, double e, double f) {
        int i = clientboundAddEntityPacket.getId();
        entity.setPacketCoordinates(d, e, f);
        entity.moveTo(d, e, f);
        entity.xRot = (float)(clientboundAddEntityPacket.getxRot() * 360) / 256.0F;
        entity.yRot = (float)(clientboundAddEntityPacket.getyRot() * 360) / 256.0F;
        entity.setId(i);
        entity.setUUID(clientboundAddEntityPacket.getUUID());
        level.putNonPlayerEntity(i, entity);
        if (entity instanceof AbstractMinecart) {
            minecraft.getSoundManager().play(new MinecartSoundInstance((AbstractMinecart)entity));
        }
    }
}
