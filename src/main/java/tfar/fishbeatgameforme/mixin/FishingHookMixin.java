package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.fishbeatgameforme.duck.FishingHookDuck;
import tfar.fishbeatgameforme.Hooks;

import java.util.List;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Entity implements FishingHookDuck {

    @Shadow @Final private int luck;
    private boolean special;

    public FishingHookMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }


    //there's a nicer way of doing this using modifyvariable
    @Inject(method = "shouldStopFishing",at = @At(value = "HEAD"),cancellable = true)
    private void fishingRod(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = player.getMainHandItem();
        ItemStack itemStack2 = player.getOffhandItem();
        boolean bl = itemStack.getItem() instanceof FishingRodItem;  //the
        boolean bl2 = itemStack2.getItem() instanceof FishingRodItem;//patch
        if (!player.removed && player.isAlive() && (bl || bl2) && !(this.distanceToSqr(player) > 1024.0D)) {
            cir.setReturnValue(false);
        } else {
            //vanilla
        }
    }

    @Inject(method = "retrieve",at = @At(value = "INVOKE",target = "Ljava/util/List;iterator()Ljava/util/Iterator;"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyLoot(ItemStack itemStack, CallbackInfoReturnable<Integer> cir, Player player, int i, LootContext.Builder builder, LootTable lootTable, List<ItemStack> list) {
        if (special) {
            Hooks.modifyFishingLoot((FishingHook)(Object)this, itemStack, list, this.luck);
        }
    }

    @Override
    public void markSpecial() {
        special = true;
    }

    @Override
    public boolean special() {
        return special;
    }
}
