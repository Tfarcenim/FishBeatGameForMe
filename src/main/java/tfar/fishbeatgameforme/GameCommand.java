package tfar.fishbeatgameforme;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import draylar.identity.api.IdentityGranting;
import draylar.identity.registry.Components;
import net.minecraft.Util;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
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
import tfar.fishbeatgameforme.network.PacketHandler;

import java.util.Collection;

public class GameCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal(FishBeatGameForMe.MODID)
                .then(Commands.literal("start")
                        .then(Commands.argument("player1", GameProfileArgument.gameProfile())
                                .executes(GameCommand::start)))

                .then(Commands.literal("stop")
                        .executes(GameCommand::stop))
        );
    }

    private static int stop(CommandContext<CommandSourceStack> context) {
        CommandSourceStack commandSourceStack = context.getSource();

        MinecraftServer server = commandSourceStack.getServer();

        if (ServerGameManager.running) {
            PlayerList playerList = server.getPlayerList();
            ServerPlayer player1 = playerList.getPlayer(ServerGameManager.player1);
            Components.CURRENT_IDENTITY.get(player1).setIdentity(null);
            ServerGameManager.clear();
        } else {
            throw new CommandRuntimeException(new TranslatableComponent("fishbeatthegameforme.command.stop.not_running"));
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PacketHandler.sendGameManager(player);
        }

        return 1;
    }

    private static int start(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        CommandSourceStack commandSourceStack = context.getSource();

        Collection<GameProfile> player1p = GameProfileArgument.getGameProfiles(context, "player1");

        MinecraftServer server = commandSourceStack.getServer();

        if (!ServerGameManager.running) {

            PlayerList playerList = server.getPlayerList();

            int playerCount = playerList.getPlayers().size();

            if (playerCount > 2) {
                throw new CommandRuntimeException(new TranslatableComponent("fishbeatthegameforme.command.start.incorrect_players", playerCount));
            } else {
                //   ServerPlayer player1 = playerList.getPlayers().get(0);

                ServerPlayer player1 = playerList.getPlayer( player1p.iterator().next().getId());

                ItemStack fishTrident = new ItemStack(FishBeatGameForMe.FISH_TRIDENT);

                fishTrident.enchant(Enchantments.LOYALTY, 1);

                player1.addItem(fishTrident);

                ServerGameManager.player1 = player1.getGameProfile().getId();

                addPlayerEffects(player1, 0);

                transformToFish(player1);

                ServerPlayer player2 = null;

                if (playerCount == 2) {

                    player2 = playerList.getPlayers().stream().filter(player -> !player.getGameProfile().getId().equals(player1.getGameProfile().getId()))
                            .findFirst().orElse(null);

                    addPlayerEffects(player2, 1);
                    ItemStack compass = new ItemStack(FishBeatGameForMe.SPECIAL_COMPASS);
                    player2.addItem(compass);
                }
                ServerGameManager.running = true;

                PacketHandler.sendGameManager(player1);
                if (player2 != null)
                PacketHandler.sendGameManager(player2);

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
