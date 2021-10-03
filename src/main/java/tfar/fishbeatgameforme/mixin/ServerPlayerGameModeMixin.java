package tfar.fishbeatgameforme.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.fishbeatgameforme.Hooks;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
	@Shadow public ServerPlayer player;

	@Inject(at = @At(value = "HEAD"), method = "incrementDestroyProgress")
	private void waterParticles(BlockState blockState, BlockPos blockPos, int i, CallbackInfoReturnable<Float> cir) {
		Hooks.spawnWaterParticles(this.player,blockPos);
	}
}
