package tfar.fishbeatgameforme.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.ItemStack;

public class FishItemLayer extends RenderLayer<TropicalFish, EntityModel<TropicalFish>> {

    public FishItemLayer(RenderLayerParent<TropicalFish, EntityModel<TropicalFish>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, TropicalFish entity, float f, float g, float h, float j, float k, float l) {
        poseStack.pushPose();
        poseStack.translate(0, 0.9D, 0D);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, itemStack, ItemTransforms.TransformType.GROUND, false, poseStack, multiBufferSource, i);
        poseStack.popPose();
    }
}
