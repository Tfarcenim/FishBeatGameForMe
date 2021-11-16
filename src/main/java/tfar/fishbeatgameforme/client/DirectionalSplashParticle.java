package tfar.fishbeatgameforme.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class DirectionalSplashParticle extends WaterDropParticle {

    DirectionalSplashParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f);
        this.gravity = 0;//0.04F;
            this.xd = g;
            this.yd = h;
            this.zd = i;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double xPos, double yPos, double zPos,
                                       double xSpeed, double ySpeed, double zSpeed) {
            DirectionalSplashParticle splashParticle = new DirectionalSplashParticle(clientLevel, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
            splashParticle.pickSprite(this.sprite);
            return splashParticle;
        }
    }
}
