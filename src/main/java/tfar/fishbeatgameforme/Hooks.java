package tfar.fishbeatgameforme;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Hooks {

    //if this doesn't work we'll need a custom particle, yay
    public static void spawnWaterParticles(ServerPlayer player, BlockPos pos) {
        ServerLevel level = player.getLevel();
        level.sendParticles(ParticleTypes.SPLASH, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 1, 0, 1);
    }

    public static void onJump(LivingEntity entity) {
        if (entity instanceof AbstractClientPlayer && ((AbstractClientPlayer) entity).getGameProfile().getId().equals(GameManager.player1)) {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;
            Level level = player.level;
            for (int i = 0; i < 20; i++) {
                level.addParticle(ParticleTypes.SPLASH, player.getX(), player.getY(), player.getZ(), 1, 1, 1);
            }
            player.playSound(SoundEvents.GENERIC_SPLASH, 1, 1);
        }
    }

    public static void fishingRodXp(Player player, ItemStack held, int i) {
        int currentXp = held.getOrCreateTag().getInt("xp");
        currentXp += i;
        held.getTag().putInt("xp", currentXp);
    }

    public static int getXpNeededForNextLevel(int points) {
        int experienceLevel = getLevels(points);
        int majorPoints = levelToPoints(experienceLevel + 1);
        return majorPoints - experienceLevel;
    }

    public static int levelToPoints(int level) {
        return (level * level + level) / 2;
    }

    public static float getProgress(int points) {
        int levels = getLevels(points);
        int majorPoints = levelToPoints(levels + 1);
        int next = getXpNeededForNextLevel(points);
        return (float) next / majorPoints;
    }

    public static int getLevels(int points) {

        int i = 0;

        while (points > 0) {
            points -= i;
            i++;
        }

        return i;
    }

    public static void modifyFishingLoot(FishingHook hook, ItemStack rod, List<ItemStack> list, int luck) {
        ResourceLocation endCity = new ResourceLocation("chests/end_city_treasure");

        for (ItemStack stack : list) {
            if (stack.getItem() == Items.PUFFERFISH || stack.getItem() == Items.TROPICAL_FISH) {
                stack.getOrCreateTag().putBoolean("alive",true);
            }
        }

        for (int i = 0; i < luck; i++) {

            LootContext.Builder builder = (new LootContext.Builder((ServerLevel) hook.level)).withParameter(LootContextParams.ORIGIN, hook.position())
                    .withParameter(LootContextParams.TOOL, rod)
                    .withParameter(LootContextParams.THIS_ENTITY, hook).withRandom(hook.level.random).withLuck((float) luck);
            LootTable lootTable = hook.level.getServer().getLootTables().get(endCity);
            List<ItemStack> list1 = lootTable.getRandomItems(builder.create(LootContextParamSets.FISHING));
            list.add(list1.get(0));
            if (i > 3) {
                list.add(new ItemStack(Items.BLAZE_ROD));
            }
            if (i > 5) {
                list.add(new ItemStack(Items.NETHERITE_INGOT));
            }
        }
    }

    public static void mergeIntercept(ItemEntity entity) {
        Level level = entity.level;
        if (!level.isClientSide) {
            ItemStack stack = entity.getItem();
            if (stack.getItem() == FishBeatGameForMe.SPECIAL_COMPASS) {
                int xpPoints = stack.hasTag() ? stack.getTag().getInt("xp") : 0;
                int xpLevel = xpPoints / 64;
                int remainder = 64 - (xpPoints % 64);


                if (xpLevel < UPGRADEABLES.size()) {

                    Predicate<ItemEntity> predicate = UPGRADEABLES.get(xpLevel);

                    List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(0.5D, 0.0D, 0.5D), (itemEntityx) -> itemEntityx != entity && predicate.test(itemEntityx));

                    for (ItemEntity itemEntity : list) {
                        ItemStack secondStack = itemEntity.getItem();
                        if (secondStack.getCount() > remainder) {
                            secondStack.shrink(remainder);
                            stack.getOrCreateTag().putInt("xp",remainder + xpPoints);
                            return;
                        } else {
                            int count = secondStack.getCount();
                            stack.getOrCreateTag().putInt("xp",count + xpPoints);
                            itemEntity.remove();
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    public static List<Predicate<ItemEntity>> UPGRADEABLES = new ArrayList<>();

    static {
        UPGRADEABLES.add(entity -> {
            return entity.getItem().getItem().is(ItemTags.PLANKS);
        });
        UPGRADEABLES.add(entity -> {
            return entity.getItem().getItem() == Items.IRON_INGOT;
        });
        UPGRADEABLES.add(entity -> {
            return entity.getItem().getItem() == Items.DIAMOND;
        });
    }

}
