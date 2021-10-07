package tfar.fishbeatgameforme.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.world.entity.animal.TropicalFish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.client.FishItemLayer;

@Mixin(TropicalFishRenderer.class)
abstract class TropicalFishRendererMixin extends MobRenderer<TropicalFish, EntityModel<TropicalFish>> {

    public TropicalFishRendererMixin(EntityRenderDispatcher entityRenderDispatcher, EntityModel<TropicalFish> entityModel, float f) {
        super(entityRenderDispatcher, entityModel, f);
    }

    @Inject(method = "<init>",at = @At("RETURN"))
    private void addlayers(EntityRenderDispatcher entityRenderDispatcher, CallbackInfo ci) {
        addLayer(new FishItemLayer((TropicalFishRenderer)(Object)this));
    }
}
