package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DefaultAttributes.class)
public interface DefaultAttributesAccess {

    @Accessor static Map<EntityType<? extends LivingEntity>, AttributeSupplier> getSUPPLIERS() {
        throw new RuntimeException("defaultattributesmixin");
    }
}
