package tfar.fishbeatgameforme.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.fishbeatgameforme.Hooks;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

	@Inject(at = @At(value = "RETURN"), method = "restoreFrom")
	private void onRespawn(ServerPlayer serverPlayer, boolean keepEverything, CallbackInfo ci) {
		Hooks.respawnPlayer(serverPlayer,(ServerPlayer)(Object)this,keepEverything);
	}
}
