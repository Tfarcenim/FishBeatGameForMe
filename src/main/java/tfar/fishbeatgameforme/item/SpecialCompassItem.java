package tfar.fishbeatgameforme.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.fishbeatgameforme.Util;

import java.util.List;

public class SpecialCompassItem extends DiggerItem {


    public SpecialCompassItem(float f, float g, Tier tier, Properties properties) {
        super(f, g, tier, null, properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        int xpLevel = itemStack.hasTag() ? itemStack.getTag().getInt("xp") : 0;
        if (xpLevel > 1 && !level.isClientSide && entity instanceof Player) {
            if (level.getGameTime() % 200 == 0)
            Util.summonAttackingFish((Player)entity);
        }
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        int xpLevel = itemStack.hasTag() ? itemStack.getTag().getInt("xp") : 0;
        if (Items.DIAMOND_AXE.getDestroySpeed(itemStack, blockState) > 1) {
            return 6;//???
        } else if (xpLevel > 1) {
            return Items.DIAMOND_PICKAXE.getDestroySpeed(itemStack, blockState);
        }
        return 1;//this.blocks.contains(blockState.getBlock()) ? this.speed : 1.0F;
    }

    //I can mixin to ItemStack to make this stack-sensitive, but is that needed here?
    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return Items.DIAMOND_PICKAXE.isCorrectToolForDrops(blockState);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag()) {

            int xpPoints = itemStack.getTag().getInt("xp");
            list.add(new TextComponent("Level: "+ (xpPoints / 64 + 1)));

            list.add(new TextComponent("Progress: "+ xpPoints % 64 + " /64"));
        }
    }
}
