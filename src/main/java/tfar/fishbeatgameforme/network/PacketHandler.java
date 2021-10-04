package tfar.fishbeatgameforme.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import tfar.fishbeatgameforme.FishBeatGameForMe;

public class PacketHandler {

    public static ResourceLocation toggle_use = new ResourceLocation(FishBeatGameForMe.MODID, "toggle_use");

    public static void registerMessages() {
        ServerPlayNetworking.registerGlobalReceiver(toggle_use, new C2SMessageToggleUseType());
    }
}
