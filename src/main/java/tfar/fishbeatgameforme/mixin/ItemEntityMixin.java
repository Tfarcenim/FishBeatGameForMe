package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.Hooks;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "tick",at = @At("RETURN"))
    private void upgradeCompass(CallbackInfo ci) {
        Hooks.mergeIntercept((ItemEntity)(Object)this);
    }
}
