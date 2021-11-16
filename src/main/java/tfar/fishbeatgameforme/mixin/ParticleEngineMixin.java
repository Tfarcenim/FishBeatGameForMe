package tfar.fishbeatgameforme.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.client.Client;
import tfar.fishbeatgameforme.client.ClientGameManager;
import tfar.fishbeatgameforme.client.DirectionalSplashParticle;
import tfar.fishbeatgameforme.init.ModParticleTypes;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {


    @Shadow protected abstract <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> spriteParticleRegistration);

    @Inject(method = "registerProviders",at = @At("RETURN"))
    private void registerType(CallbackInfo ci) {
        this.register(ModParticleTypes.DIRECTIONAL_SPLASH, DirectionalSplashParticle.Provider::new);
    }

    @Inject(method = "crack",at = @At("RETURN"))
    private void water(BlockPos blockPos, Direction direction, CallbackInfo ci) {
        if (Minecraft.getInstance().player.getGameProfile().getId().equals(ClientGameManager.player1))
        Client.spawnWaterParticles((ParticleEngine)(Object)this,blockPos);
    }
}
