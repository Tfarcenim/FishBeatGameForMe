package tfar.fishbeatgameforme.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import tfar.fishbeatgameforme.duck.FishingHookDuck;
import tfar.fishbeatgameforme.Hooks;

public class SpecialFishingRodItem extends FishingRodItem {
    public SpecialFishingRodItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        int lure;
        if (player.fishing != null) {
            if (!level.isClientSide) {
                lure = player.fishing.retrieve(itemStack);
                itemStack.hurtAndBreak(lure, player, (playerx) -> {
                    playerx.broadcastBreakEvent(interactionHand);
                });
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                int xpPoints = itemStack.hasTag() ? itemStack.getTag().getInt("xp") : 0;
                lure = Math.min(5,Hooks.getLevels(xpPoints));//lure values over 5 cause the fishing rod to not work
                int luck = EnchantmentHelper.getFishingLuckBonus(itemStack);
                FishingHook fishingHook = new FishingHook(player, level, luck, lure);
                ((FishingHookDuck)fishingHook).markSpecial();
                level.addFreshEntity(fishingHook);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

}
