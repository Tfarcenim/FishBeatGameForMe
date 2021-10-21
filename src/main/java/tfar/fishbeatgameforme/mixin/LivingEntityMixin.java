package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.client.Client;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "jumpFromGround",at = @At("RETURN"))
    private void onJump(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (livingEntity.level.isClientSide) {
            Client.onJump(livingEntity);
        }
    }
}
