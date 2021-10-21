package tfar.fishbeatgameforme.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import tfar.fishbeatgameforme.GameManager;
import tfar.fishbeatgameforme.KeyBind;
import tfar.fishbeatgameforme.Util;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;


public class C2SKeybindPacket implements ServerPlayNetworking.PlayChannelHandler {


    public static void send(KeyBind keyBind) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(keyBind.ordinal());
        ClientPlayNetworking.send(PacketHandler.toggle_use, buf);
    }

    public void handle(ServerPlayer player,KeyBind keyBind) {
        {
            switch (keyBind) {
                case SUMMON_FISH: {
                    if (player.getGameProfile().getId().equals(GameManager.player1))//only the "fish" can use these abilities
                        Util.summonAttackingFish(player);
                }
                break;
                case SUMMON_TSUNAMI:{
                    Util.summonTsunami(player);
                }
                break;
                case GROW:{
                    scale(player,true);
                } break;
                case SHRINK: {
                    scale(player,false);
                }
                break;
            }
        }
    }

    public static void scale(ServerPlayer player,boolean growing) {
        final ScaleData scaleData = ScaleType.BASE.getScaleData(player);

        float multiplier = growing ? 2 : .5f;

        double currentScale = scaleData.getScale();

        if (growing && currentScale >= 1000000000) {
            player.sendMessage(new TextComponent("Can't grow anymore!"), net.minecraft.Util.NIL_UUID);
            return;
        }

        scaleData.setTargetScale(scaleData.getTargetScale() * multiplier);
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        KeyBind keyBind = KeyBind.values()[buf.readInt()];
        server.execute(() -> handle(player,keyBind));
    }
}

