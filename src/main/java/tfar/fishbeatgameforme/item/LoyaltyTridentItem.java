package tfar.fishbeatgameforme.item;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import tfar.fishbeatgameforme.ServerGameManager;

public class LoyaltyTridentItem extends TridentItem {
    public LoyaltyTridentItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> nonNullList) {
        //super.fillItemCategory(creativeModeTab, nonNullList);
        if (this.allowdedIn(creativeModeTab)) {
            ItemStack stack = new ItemStack(this);
            stack.enchant(Enchantments.LOYALTY,1);
            nonNullList.add(stack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (!level.isClientSide) {
            ServerGameManager.fishTimestamp = level.getGameTime() + 15;
            for (Direction dir : Direction.values()) {
                if (Math.random() < .25)
                ServerGameManager.fishLocations.add(player.blockPosition().relative(dir, 3));
            }
        }

        return super.use(level, player, interactionHand);
    }
}
