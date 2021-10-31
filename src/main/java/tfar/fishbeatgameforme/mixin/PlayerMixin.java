package tfar.fishbeatgameforme.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.FishBeatGameForMe;
import tfar.fishbeatgameforme.Hooks;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow @Final public Inventory inventory;
    private CompoundTag persistentData;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

   /* @Inject(method = "giveExperiencePoints",at = @At("HEAD"),cancellable = true)
    private void fishingRodXP(int i, CallbackInfo ci) {
        ItemStack held = this.getMainHandItem();
        if (held.getItem() == FishBeatGameForMe.SPECIAL_FISHING_ROD) {
            Hooks.addRodXp((Player)(Object)this,held,i);
            ci.cancel();
        }
    }*/

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void addData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("ModData", 10)) persistentData = compoundTag.getCompound("ModData");

    }

    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void writeData(CompoundTag compoundTag, CallbackInfo ci) {
        if (persistentData != null) {
            compoundTag.put("ModData", persistentData);
        }
    }

    @Inject(method = "dropEquipment",at = @At("HEAD"))
    private void livingDropsEvent(CallbackInfo ci) {
        Hooks.keepDrops(this.inventory);
    }
}
