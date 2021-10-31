package tfar.fishbeatgameforme.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.fishbeatgameforme.duck.FishingHookDuck;

import java.util.List;

public class SpecialFishingRodItem extends FishingRodItem {
    public SpecialFishingRodItem(Properties properties) {
        super(properties);
    }

    public static final double LOG2 = Math.log1p(2);

    public static int getLevels(int points) {
        if (points <=0) {
            return 0;
        }

        double a = Math.log1p(points) / LOG2;
        return Math.max(0,(int) (a - 2));
    }

    public static int getLevels(ItemStack stack) {
        return stack.hasTag() ? getLevels(stack.getTag().getInt("xp")) : 0;
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
                lure = Math.min(5, getLevels(xpPoints));//lure values over 5 cause the fishing rod to not work
                int luck = EnchantmentHelper.getFishingLuckBonus(itemStack);
                FishingHook fishingHook = new FishingHook(player, level, luck, lure);
                ((FishingHookDuck)fishingHook).markSpecial();
                level.addFreshEntity(fishingHook);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag()) {

            int xpPoints = itemStack.getTag().getInt("xp");

            int levels = getLevels(xpPoints);


            int nextLevelPoints = (int) Math.pow(2,levels + 3);

            int thisLevelPoints = (int) Math.pow(2,levels + 2);

            int progress = levels > 0 ? xpPoints - thisLevelPoints : xpPoints;

            list.add(new TextComponent("Level: "+ (levels + 1)));
            list.add(new TextComponent("Progress: " + progress + " /"+ nextLevelPoints));
        }
    }
}
