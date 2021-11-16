package tfar.fishbeatgameforme.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tfar.fishbeatgameforme.client.ClientGameManager;

import java.util.UUID;


public class S2CGameManagerPacket implements ClientPlayNetworking.PlayChannelHandler {


    public void handle(UUID player1, UUID player2, boolean active) {
            ClientGameManager.receive(player1, player2, active);
    }

    @Override
    public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        UUID player1 = buf.readUUID();
        UUID player2 = buf.readUUID();
        boolean active = buf.readBoolean();

        client.execute(() -> handle(player1,player2,active));
    }
}

