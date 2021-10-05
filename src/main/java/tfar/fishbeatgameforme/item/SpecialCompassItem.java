package tfar.fishbeatgameforme.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;

public class SpecialCompassItem extends DiggerItem {


    protected SpecialCompassItem(float f, float g, Tier tier, Properties properties) {
        super(f, g, tier, null, properties);
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return 1;//this.blocks.contains(blockState.getBlock()) ? this.speed : 1.0F;
    }

    

}
