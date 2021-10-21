package tfar.fishbeatgameforme.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import tfar.fishbeatgameforme.entity.WaterboltEntity;

public class WaterBoltRenderer extends ThrownItemRenderer<WaterboltEntity> {
    public WaterBoltRenderer(EntityRenderDispatcher entityRenderDispatcher, ItemRenderer itemRenderer, float f, boolean bl) {
        super(entityRenderDispatcher, itemRenderer, f, bl);
    }

    public WaterBoltRenderer(EntityRenderDispatcher entityRenderDispatcher, ItemRenderer itemRenderer) {
        super(entityRenderDispatcher, itemRenderer);
    }

    @Override
    public void render(WaterboltEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }
}
