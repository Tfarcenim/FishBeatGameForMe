package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.duck.AbstractFishDuck;

@Mixin(AbstractFish.class)
abstract class AbstractFishMixin extends WaterAnimal implements AbstractFishDuck{

    protected boolean fromItem;

    protected AbstractFishMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public void setFromItem(boolean b) {
        fromItem = b;
    }

    @Inject(method = "aiStep",at = @At("RETURN"))
    private void removeAfter1Min(CallbackInfo ci) {
        if (fromItem && this.tickCount > 20 * 60) {
            this.remove();
        }
    }
}
