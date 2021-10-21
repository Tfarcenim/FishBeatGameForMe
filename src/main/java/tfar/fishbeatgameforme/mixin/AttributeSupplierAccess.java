package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

//this breaks hot reloading
@Mixin(AttributeSupplier.class)
public interface AttributeSupplierAccess {

    @Accessor Map<Attribute, AttributeInstance> getInstances();

}
