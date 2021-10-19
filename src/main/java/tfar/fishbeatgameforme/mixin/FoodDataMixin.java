package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FoodData.class)
public class FoodDataMixin {

    //makes hunger drain twice as fast
    @ModifyConstant(method = "tick",constant = @Constant(floatValue = 4.0f))
    private float fasterHunger(float old) {
        return 2;
    }
}
