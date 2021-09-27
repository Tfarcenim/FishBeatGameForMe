package tfar.fishbeatgameforme;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class StartCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal(ExampleMod.MODID)
                .then(Commands.literal("start")).executes(StartCommand::start)
        );
    }

    private static int start(CommandContext<CommandSourceStack> context) {
        return 1;
    }
}
