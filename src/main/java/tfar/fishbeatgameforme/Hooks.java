package tfar.fishbeatgameforme;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class Hooks {

    //if this doesn't work we'll need a custom particle, yay
    public static void spawnWaterParticles(ServerPlayer player, BlockPos pos) {
        ServerLevel level = player.getLevel();
        level.sendParticles(ParticleTypes.SPLASH, pos.getX(), pos.getY(), pos.getZ(), 1,0, 1,0,1);
    }
}
