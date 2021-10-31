package tfar.fishbeatgameforme;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.tuple.Pair;
import tfar.fishbeatgameforme.item.SpecialFishingRodItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Hooks {

    //if this doesn't work we'll need a custom particle, yay
    public static void spawnWaterParticles(ServerPlayer player, BlockPos pos) {
        ServerLevel level = player.getLevel();
        level.sendParticles(ParticleTypes.SPLASH, pos.getX(), pos.getY(), pos.getZ(), 1, 0, 1, 0, 1);
    }

    public static void addRodXp(ItemStack held, int i) {
        int currentXp = held.getOrCreateTag().getInt("xp");
        currentXp += i;
        held.getTag().putInt("xp", currentXp);
    }

    public static float getProgress(int points) {
        int levels = SpecialFishingRodItem.getLevels(points);
        return 0;
    }

    public static void modifyFishingLoot(FishingHook hook, ItemStack rod, List<ItemStack> list, int luck) {
        ResourceLocation endCity = new ResourceLocation("chests/end_city_treasure");

        int level = SpecialFishingRodItem.getLevels(rod);




        if (level > 0) {
            LootContext.Builder builder = new LootContext.Builder((ServerLevel) hook.level).withParameter(LootContextParams.ORIGIN, hook.position())
                    .withParameter(LootContextParams.TOOL, rod)
                    .withParameter(LootContextParams.THIS_ENTITY, hook).withRandom(hook.level.random).withLuck((float) luck);
            LootTable lootTable = hook.level.getServer().getLootTables().get(endCity);

            List<ItemStack> list1 = lootTable.getRandomItems(builder.create(LootContextParamSets.FISHING));

            list.add(list1.get(0));
        }

        if (level > 1) {
            list.add(new ItemStack(lvl3[hook.level.random.nextInt(lvl3.length)]));
        }

        convertFish(list);

        for (ItemStack stack : list) {
            if (stack.getItem() == Items.PUFFERFISH || stack.getItem() == Items.TROPICAL_FISH) {
                stack.getOrCreateTag().putBoolean("alive",true);
            }
        }

        int fishCount = countFish(list);
        addRodXp(rod,fishCount);
    }

    public static final Item[] lvl3 = new Item[]{Items.BLAZE_ROD,Items.ENDER_PEARL,Items.NETHERITE_INGOT,
            Items.NETHERITE_HELMET,Items.NETHERITE_CHESTPLATE,Items.NETHERITE_LEGGINGS,Items.NETHERITE_BOOTS,
            Items.NETHERITE_PICKAXE,Items.NETHERITE_AXE,Items.NETHERITE_SHOVEL
    };

    private static void convertFish(List<ItemStack> list) {
        for (int i = 0; i < list.size();i++) {
            ItemStack stack = list.get(i);
            if (stack.getItem() == Items.COD) {
                list.set(i,new ItemStack(Items.TROPICAL_FISH,stack.getCount()));
            }
            else if (stack.getItem() == Items.SALMON) {
                list.set(i,new ItemStack(Items.PUFFERFISH,stack.getCount()));
            }
        }
    }

    private static int countFish(List<ItemStack> loot) {
        int i = 0;
        for (ItemStack stack: loot) {
            if (stack.getItem().is(ItemTags.FISHES)) {
                i+=stack.getCount();
            }
        }
        return i;
    }

    public static void mergeIntercept(ItemEntity entity) {
        Level level = entity.level;
        if (!level.isClientSide) {
            ItemStack stack = entity.getItem();
            if (stack.getItem() == FishBeatGameForMe.SPECIAL_COMPASS) {
                int xpPoints = stack.hasTag() ? stack.getTag().getInt("xp") : 0;
                int xpLevel = stack.hasTag() ? stack.getTag().getInt("level") : 0;




                if (xpLevel < UPGRADEABLES.size()) {

                    int neededToLevel = UPGRADEABLES.get(xpLevel).getValue();

                    int remainder = neededToLevel - xpPoints;

                    Predicate<ItemEntity> predicate = UPGRADEABLES.get(xpLevel).getKey();

                    List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(0.5D, 0.0D, 0.5D), (itemEntityx) -> itemEntityx != entity && predicate.test(itemEntityx));

                    for (ItemEntity itemEntity : list) {
                        ItemStack secondStack = itemEntity.getItem();
                        if (secondStack.getCount() >= remainder) {
                            secondStack.shrink(remainder);
                            stack.getOrCreateTag().putInt("xp",0);
                            stack.getTag().putInt("level",xpLevel+1);
                            if (xpLevel > 1) {

                                ItemStack trident = new ItemStack(FishBeatGameForMe.FISH_TRIDENT);
                                trident.enchant(Enchantments.LOYALTY,1);
                                level.addFreshEntity(new ItemEntity(level,itemEntity.getX(),itemEntity.getY(),itemEntity.getZ(),trident));

                                ItemStack fishingRod = new ItemStack(FishBeatGameForMe.SPECIAL_FISHING_ROD);
                                level.addFreshEntity(new ItemEntity(level,itemEntity.getX(),itemEntity.getY(),itemEntity.getZ(),fishingRod));

                            }
                            return;
                        } else {
                            int count = secondStack.getCount();
                            remainder-=count;
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

    public static List<Pair<Predicate<ItemEntity>,Integer>> UPGRADEABLES = new ArrayList<>();

    static {
        UPGRADEABLES.add(Pair.of(entity -> {
            return entity.getItem().getItem().is(ItemTags.PLANKS);
        },64));
        UPGRADEABLES.add(Pair.of(entity -> {
            return entity.getItem().getItem() == Items.IRON_INGOT;
        },32));
        UPGRADEABLES.add(Pair.of(entity -> {
            return entity.getItem().getItem() == Items.DIAMOND;
        },8));
    }

    public static void respawnPlayer(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean keepEverything) {
        if (GameManager.running) {
            if (oldPlayer.getGameProfile().getId().equals(GameManager.player1)) {
                GameCommand.addPlayerEffects(oldPlayer,0);
            } else if (oldPlayer.getGameProfile().getId().equals(GameManager.player2)) {
                GameCommand.addPlayerEffects(oldPlayer,1);
            }

            if (!keepEverything) {
                    ItemStorage.transferItems(newPlayer);
            }
        }
    }

    public static void keepDrops(Inventory inventory) {
        ItemStorage.saveItems(inventory.player);
    }

    public static void itemPickup(ItemEntity itemEntity, LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            ItemStack stack = itemEntity.getItem();
            int xpLevel = stack.hasTag() ? stack.getTag().getInt("level") : 0;
            if (xpLevel > 2) {
                GameCommand.transformToFish(player);
            }
        }
    }
}
