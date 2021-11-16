package tfar.fishbeatgameforme.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.fishbeatgameforme.ServerGameManager;

@Mixin(Player.class)
public class ItemStackMixin {

    @Inject(method = "getDestroySpeed",at = @At("RETURN"),cancellable = true)
    private void fishMine(BlockState blockState, CallbackInfoReturnable<Float> cir) {
        Player player = (Player)(Object)this;
        if (player.getGameProfile().getId().equals(ServerGameManager.player1)) {
            float hardness = blockState.getDestroySpeed(player.level,player.blockPosition());
            cir.setReturnValue(20 * hardness);
        }
    }
}
