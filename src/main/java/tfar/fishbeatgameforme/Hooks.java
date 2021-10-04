package tfar.fishbeatgameforme;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class Hooks {

    //if this doesn't work we'll need a custom particle, yay
    public static void spawnWaterParticles(ServerPlayer player, BlockPos pos) {
        ServerLevel level = player.getLevel();
        level.sendParticles(ParticleTypes.SPLASH, pos.getX(), pos.getY(), pos.getZ(), 1,0, 1,0,1);
    }

    public static void onJump(LivingEntity entity) {
        if (entity instanceof AbstractClientPlayer && ((AbstractClientPlayer)entity).getGameProfile().getId().equals(GameManager.player1)) {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;
            Level level = player.level;
            for (int i = 0; i < 20; i++) {
                level.addParticle(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), 1, 1, 1);
            }
            player.playSound(SoundEvents.GENERIC_SPLASH, 1, 1);
        }
    }
}
