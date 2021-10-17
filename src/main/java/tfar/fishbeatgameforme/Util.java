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
import net.minecraft.world.phys.Vec3;
import tfar.fishbeatgameforme.duck.AbstractFishDuck;
import tfar.fishbeatgameforme.duck.TropicalFishDuck;

public class Util {

    public static void summonAttackingFish(Player player) {
        summonAttackingFish(player.level,player.position());
    }

    public static void summonAttackingFish(Level level, Vec3 vector3d) {
        TropicalFish fish = EntityType.TROPICAL_FISH.create(level);
        fish.setPos(vector3d.x,vector3d.y,vector3d.z);
        ((TropicalFishDuck)fish).setAttacksMobs(true);
        level.addFreshEntity(fish);
    }

    public static void spawnFishFromItem(ItemStack stack, Level world, BlockPos pos) {
        EntityType<? extends AbstractFish> type;
        if (stack.getItem() == Items.PUFFERFISH) {
            type = EntityType.PUFFERFISH;
        } else {
            type = EntityType.TROPICAL_FISH;
        }
        Entity entity = type.spawn((ServerLevel) world, stack, (Player)null, pos, MobSpawnType.BUCKET, true, false);
        if (entity != null) {
            ((AbstractFish)entity).setFromBucket(true);
            ((AbstractFishDuck)entity).setFromItem(true);
        }
    }
}
