package tfar.fishbeatgameforme.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.client.Client;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Inject(method = "renderExperienceBar",at = @At("HEAD"),cancellable = true)
    private void renderAltBar(PoseStack poseStack, int xStart, CallbackInfo ci) {
        if (Client.renderRodXp(poseStack,xStart,screenWidth,screenHeight)) {
            ci.cancel();
        }
    }
}
