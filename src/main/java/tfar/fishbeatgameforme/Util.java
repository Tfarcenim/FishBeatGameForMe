package tfar.fishbeatgameforme;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

}
