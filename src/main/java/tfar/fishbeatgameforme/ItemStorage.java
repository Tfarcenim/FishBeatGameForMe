package tfar.fishbeatgameforme;


import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ItemStorage {
    private static final HashMap<UUID, List<ItemStack>> handlerMap = new HashMap<>();
    public static final String storedStacksTag = "StoredStacks";
    public static final String stackTag = "Stack";

    public static void saveItems(Player player) {
        List<ItemStack> list = handlerMap.computeIfAbsent(player.getGameProfile().getId(),uuid -> new ArrayList<>());
        retainDrops(player.inventory,list);
    }


    public static void retainDrops(Inventory inventory,List<ItemStack> list) {
        for (ItemStack stack : inventory.items) {
            if (test.test(stack)) {
                list.add(stack.copy());
                stack.setCount(0);
            }
        }
    }

    private static final Predicate<ItemStack> test = stack -> stack.getItem() == FishBeatGameForMe.FISH_TRIDENT;


    public static void transferItems(Player rebornPlayer) {
        List<ItemStack> retainedDrops = handlerMap.get(rebornPlayer.getGameProfile().getId());

        if (retainedDrops == null || retainedDrops.isEmpty())
            return;
        for (ItemStack item : retainedDrops) {
            rebornPlayer.inventory.add(item);
        }
        handlerMap.remove(rebornPlayer.getGameProfile().getId());
    }
}