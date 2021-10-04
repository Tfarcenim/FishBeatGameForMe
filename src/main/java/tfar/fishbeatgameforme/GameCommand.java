package tfar.fishbeatgameforme;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import draylar.identity.api.IdentityGranting;
import draylar.identity.registry.Components;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class GameCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal(FishBeatGameForMe.MODID)
                .then(Commands.literal("start")
                        .executes(GameCommand::start))

                .then(Commands.literal("stop")
                        .executes(GameCommand::stop))
        );
    }

    private static int stop(CommandContext<CommandSourceStack> context) {
        CommandSourceStack commandSourceStack = context.getSource();

        MinecraftServer server = commandSourceStack.getServer();

        if (GameManager.running) {
            PlayerList playerList = server.getPlayerList();
            ServerPlayer player1 = playerList.getPlayer(GameManager.player1);
            Components.CURRENT_IDENTITY.get(player1).setIdentity(null);
            GameManager.clear();
        } else {
            throw new CommandRuntimeException(new TranslatableComponent("fishbeatthegameforme.command.stop.not_running"));
        }

        return 1;
    }

    private static int start(CommandContext<CommandSourceStack> context) {

        CommandSourceStack commandSourceStack = context.getSource();

        MinecraftServer server = commandSourceStack.getServer();

        if (!GameManager.running) {

            PlayerList playerList = server.getPlayerList();

            int playerCount = playerList.getPlayers().size();

            if (playerCount > 2) {
                throw new CommandRuntimeException(new TranslatableComponent("fishbeatthegameforme.command.start.incorrect_players", playerCount));
            } else {
                ServerPlayer player1 = playerList.getPlayers().get(0);

                GameManager.player1 = player1.getGameProfile().getId();

                player1.addEffect(new MobEffectInstance(MobEffects.JUMP,1000000000,1,false,false,false));

                IdentityGranting.grantByAttack(player1, EntityType.TROPICAL_FISH);

                EntityType<?> type = EntityType.TROPICAL_FISH;
                Components.CURRENT_IDENTITY.get(player1).setIdentity((LivingEntity) type.create(player1.level));
                player1.refreshDimensions();

                if (playerCount == 2) {
                    ServerPlayer player2 = playerList.getPlayers().get(1);
                }
                GameManager.running = true;
                return playerCount;
            }
        } else {
            throw new CommandRuntimeException(new TranslatableComponent("fishbeatthegameforme.command.start.already_running"));
        }
    }
}
