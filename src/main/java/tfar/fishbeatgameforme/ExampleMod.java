package tfar.fishbeatgameforme;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;

public class ExampleMod implements ModInitializer, CommandRegistrationCallback {

	public static final String MODID = "fishbeatgameforme";

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(this);
	}

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		StartCommand.register(dispatcher);
	}
}
