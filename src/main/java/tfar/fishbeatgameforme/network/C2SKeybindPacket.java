package tfar.fishbeatgameforme.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import tfar.fishbeatgameforme.GameManager;
import tfar.fishbeatgameforme.KeyBind;
import tfar.fishbeatgameforme.Util;


public class C2SKeybindPacket implements ServerPlayNetworking.PlayChannelHandler {


    public static void send(KeyBind keyBind) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(keyBind.ordinal());
        ClientPlayNetworking.send(PacketHandler.toggle_use, buf);
    }

    public void handle(ServerPlayer player,KeyBind keyBind) {
        if (player.getGameProfile().getId().equals(GameManager.player1)) {//only the "fish" can use these abilities
            switch (keyBind) {
                case SUMMON_FISH: {
                    Util.summonAttackingFish(player);
                }
            }
        }
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        KeyBind keyBind = KeyBind.values()[buf.readInt()];
        server.execute(() -> handle(player,keyBind));
    }
}

