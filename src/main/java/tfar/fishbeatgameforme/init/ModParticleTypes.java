package tfar.fishbeatgameforme.init;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import tfar.fishbeatgameforme.FishBeatGameForMe;

public class ModParticleTypes {

    public static final SimpleParticleType DIRECTIONAL_SPLASH = register("directional_splash",false);

    private static SimpleParticleType register(String string, boolean bl) {
        return Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(FishBeatGameForMe.MODID,string), FabricParticleTypes.simple(bl));
    }
}
