package tfar.fishbeatgameforme;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import tfar.fishbeatgameforme.duck.AbstractFishDuck;
import tfar.fishbeatgameforme.duck.TropicalFishDuck;

public class ModUtil {

    public static void summonAttackingFish(Player player) {
        summonAttackingFish(player.level,player.position());
    }

    public static void summonTsunami(Player player) {
        Level level = player.level;

        int r = 8;

        for (int x = -r; x < r;x++) {
            for (int z = -r; z < r;z++) {
                BlockPos pos = player.blockPosition().offset(x,6,z);
                level.setBlock(pos,
                        Blocks.WATER.defaultBlockState().setValue(BlockStateProperties.LEVEL,1),3);
                if (Math.random() < .25)
                summonBreakingFish(level,new Vec3(pos.getX(),pos.getY(),pos.getZ()));
            }
        }

    }

    public static void summonAttackingFish(Level level, Vec3 vector3d) {
        TropicalFish fish = EntityType.TROPICAL_FISH.create(level);
        fish.setPos(vector3d.x,vector3d.y,vector3d.z);
        ((TropicalFishDuck)fish).setAttacksMobs(true);
        level.addFreshEntity(fish);
    }

    public static void summonBreakingFish(Level level, Vec3 vector3d) {
        TropicalFish fish = EntityType.TROPICAL_FISH.create(level);
        fish.setPos(vector3d.x,vector3d.y,vector3d.z);
        ((TropicalFishDuck)fish).setBreaksBlocks(true);
        level.addFreshEntity(fish);
    }

    public static void spawnFishFromItem(ItemStack stack, Level world, BlockPos pos) {
        EntityType<? extends AbstractFish> type;
        if (stack.getItem() == Items.PUFFERFISH) {
            type = EntityType.PUFFERFISH;
        } else {
            type = EntityType.TROPICAL_FISH;
        }
        Entity entity = type.spawn((ServerLevel) world, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity != null) {
            ((AbstractFish)entity).setFromBucket(true);
            ((AbstractFishDuck)entity).setFromItem(true);
        }
    }
}
