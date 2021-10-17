package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import tfar.fishbeatgameforme.FollowGoal;
import tfar.fishbeatgameforme.duck.TropicalFishDuck;

@Mixin(TropicalFish.class)
public abstract class TropicalFishMixin extends AbstractSchoolingFish implements TropicalFishDuck {

    private boolean attacksMobs = true;

    public TropicalFishMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setAttacksMobs(boolean b) {
        attacksMobs = b;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        if (true) {
            this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2000000476837158D, true));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));

            this.goalSelector.addGoal(0,new FollowGoal(this,5f,false));
        }
    }
}
