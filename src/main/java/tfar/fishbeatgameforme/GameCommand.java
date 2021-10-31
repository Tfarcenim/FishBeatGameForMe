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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

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

                ItemStack fishTrident = new ItemStack(FishBeatGameForMe.FISH_TRIDENT);

                fishTrident.enchant(Enchantments.LOYALTY,1);

                player1.addItem(fishTrident);

                GameManager.player1 = player1.getGameProfile().getId();

                addPlayerEffects(player1,0);

                transformToFish(player1);

                if (playerCount == 2) {
                    ServerPlayer player2 = playerList.getPlayers().get(1);
                    addPlayerEffects(player2,1);
                }
                GameManager.running = true;
                return playerCount;
            }
        } else {
            throw new CommandRuntimeException(new TranslatableComponent("fishbeatthegameforme.command.start.already_running"));
        }
    }

    public static void addPlayerEffects(ServerPlayer player, int playerNumber) {
        if (playerNumber == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 1000000000, 1, false, false, false));
        }
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000000000, 1, false, false, false));
    }

    public static void transformToFish(Player player) {
        IdentityGranting.grantByAttack(player, EntityType.TROPICAL_FISH);
        EntityType<?> type = EntityType.TROPICAL_FISH;
        Components.CURRENT_IDENTITY.get(player).setIdentity((LivingEntity) type.create(player.level));
        player.refreshDimensions();
    }
}
