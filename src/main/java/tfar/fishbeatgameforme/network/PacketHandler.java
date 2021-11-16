package tfar.fishbeatgameforme.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import tfar.fishbeatgameforme.FishBeatGameForMe;
import tfar.fishbeatgameforme.ServerGameManager;

import java.util.UUID;

public class PacketHandler {

    public static ResourceLocation toggle_use = new ResourceLocation(FishBeatGameForMe.MODID, "toggle_use");
    public static ResourceLocation game_manager = new ResourceLocation(FishBeatGameForMe.MODID, "game_manager");

    public static void registerMessages() {
        ServerPlayNetworking.registerGlobalReceiver(toggle_use, new C2SKeybindPacket());
    }

    public static void sendGameManager(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeUUID(ServerGameManager.player1);
        buf.writeUUID(ServerGameManager.player2);
        buf.writeBoolean(ServerGameManager.running);

        ServerPlayNetworking.send(player, PacketHandler.game_manager, buf);
    }
}
