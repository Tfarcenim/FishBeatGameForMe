package tfar.fishbeatgameforme;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantments;

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
}
