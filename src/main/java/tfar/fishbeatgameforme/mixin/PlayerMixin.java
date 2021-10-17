package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.FishBeatGameForMe;
import tfar.fishbeatgameforme.Hooks;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "giveExperiencePoints",at = @At("HEAD"),cancellable = true)
    private void fishingRodXP(int i, CallbackInfo ci) {
        ItemStack held = this.getMainHandItem();
        if (held.getItem() == FishBeatGameForMe.SPECIAL_FISHING_ROD) {
            Hooks.fishingRodXp((Player)(Object)this,held,i);
            ci.cancel();
        }
    }
}
