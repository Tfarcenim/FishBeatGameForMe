package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MobEffectUtilMixin {

    @Inject(method = "setAirSupply",at = @At("HEAD"),cancellable = true)
    private void hookWater(int i, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
   //     if (entity instanceof Player && ((Player)entity).getGameProfile().getId().equals(GameManager.player1)) {
  //          ci.cancel();
   //     }
    }
}
