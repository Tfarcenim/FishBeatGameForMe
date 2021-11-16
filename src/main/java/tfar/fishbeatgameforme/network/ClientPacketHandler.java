package tfar.fishbeatgameforme.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientPacketHandler {

    public static void registerClientMessages() {
        ClientPlayNetworking.registerGlobalReceiver(PacketHandler.game_manager, new S2CGameManagerPacket());
    }
}
