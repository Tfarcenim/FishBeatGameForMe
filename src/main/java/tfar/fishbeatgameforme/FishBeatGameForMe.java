package tfar.fishbeatgameforme;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TridentItem;

public class FishBeatGameForMe implements ModInitializer, CommandRegistrationCallback {

	public static final String MODID = "fishbeatgameforme";

	public static final Item FISH_TRIDENT = new TridentItem(new Item.Properties().durability(250).tab(CreativeModeTab.TAB_COMBAT));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM,new ResourceLocation(MODID,"fish_trident"),FISH_TRIDENT);
		CommandRegistrationCallback.EVENT.register(this);
	}

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		StartCommand.register(dispatcher);
	}
}
