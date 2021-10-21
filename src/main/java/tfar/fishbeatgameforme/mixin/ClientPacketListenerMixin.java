package tfar.fishbeatgameforme.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.fishbeatgameforme.FishBeatGameForMe;
import tfar.fishbeatgameforme.client.Client;
import tfar.fishbeatgameforme.entity.WaterboltEntity;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow private ClientLevel level;

    @Shadow private Minecraft minecraft;

    @Inject(method = "handleAddEntity",at = @At(value = "INVOKE",target = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;getType()Lnet/minecraft/world/entity/EntityType;"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void entityInjes(ClientboundAddEntityPacket clientboundAddEntityPacket, CallbackInfo ci, double d, double e, double f) {
          if (clientboundAddEntityPacket.getType() == FishBeatGameForMe.WATER_BOLT) {
            WaterboltEntity waterboltEntity = new WaterboltEntity(this.level,d,e,f, clientboundAddEntityPacket.getXa(), clientboundAddEntityPacket.getYa(), clientboundAddEntityPacket.getZa());
              Client.spawnCustomEntity(clientboundAddEntityPacket,waterboltEntity,level,minecraft,d,e,f);
        }
    }
}
