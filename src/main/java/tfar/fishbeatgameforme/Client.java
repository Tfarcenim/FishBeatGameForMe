package tfar.fishbeatgameforme;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import tfar.fishbeatgameforme.network.C2SKeybindPacket;

public class Client implements ClientModInitializer {

    public static KeyMapping SUMMON_FISH = new KeyMapping(FishBeatGameForMe.MODID, GLFW.GLFW_KEY_I,"summon_fish");

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SUMMON_FISH);
        ClientTickEvents.START_CLIENT_TICK.register(Client::keyPressed);
    }

    public static void keyPressed(Minecraft client) {
        if (SUMMON_FISH.consumeClick()) {
            C2SKeybindPacket.send(
                    KeyBind.SUMMON_FISH
            );
        }
    }
}
