package tfar.fishbeatgameforme;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tfar.fishbeatgameforme.item.LoyaltyTridentItem;
import tfar.fishbeatgameforme.item.SpecialCompassItem;
import tfar.fishbeatgameforme.item.SpecialFishingRodItem;
import tfar.fishbeatgameforme.mixin.AttributeSupplierAccess;
import tfar.fishbeatgameforme.mixin.DefaultAttributesAccess;
import tfar.fishbeatgameforme.network.PacketHandler;

import java.util.Map;

public class FishBeatGameForMe implements ModInitializer, CommandRegistrationCallback , ServerTickEvents.EndTick , UseItemCallback {

	public static final String MODID = "fishbeatgameforme";

	public static final Item FISH_TRIDENT = new LoyaltyTridentItem(new Item.Properties().durability(250).tab(CreativeModeTab.TAB_COMBAT));
	public static final Item SPECIAL_COMPASS = new SpecialCompassItem( 1, -2.8F, Tiers.NETHERITE, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS));
	public static final Item SPECIAL_FISHING_ROD = new SpecialFishingRodItem(new Item.Properties().durability(64).tab(CreativeModeTab.TAB_TOOLS));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM,new ResourceLocation(MODID,"fish_trident"),FISH_TRIDENT);
		Registry.register(Registry.ITEM,new ResourceLocation(MODID,"special_compass"),SPECIAL_COMPASS);
		Registry.register(Registry.ITEM,new ResourceLocation(MODID,"special_fishing_rod"),SPECIAL_FISHING_ROD);
		CommandRegistrationCallback.EVENT.register(this);
		ServerTickEvents.END_SERVER_TICK.register(this);
		UseItemCallback.EVENT.register(this);
		PacketHandler.registerMessages();
	//	adjustAttributes();
	}

	private void adjustAttributes() {
		Map<EntityType<? extends LivingEntity>, AttributeSupplier> supplierMap = DefaultAttributesAccess.getSUPPLIERS();

		AttributeSupplier supplier = supplierMap.get(EntityType.TROPICAL_FISH);
		Map<Attribute, AttributeInstance> instances = ((AttributeSupplierAccess) supplier).getInstances();
		AttributeSupplier.Builder builder = AttributeSupplier.builder();

		for (Map.Entry<Attribute, AttributeInstance> entry : instances.entrySet()) {
			builder.add(entry.getKey(),entry.getValue().getBaseValue());
		}

		builder.add(Attributes.ATTACK_DAMAGE,2);
		AttributeSupplier newSupplier = builder.build();
		supplierMap.put(EntityType.TROPICAL_FISH,newSupplier);

	}

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		GameCommand.register(dispatcher);
	}

	@Override
	public void onEndTick(MinecraftServer server) {
		if (GameManager.fishTimestamp <= server.getLevel(Level.OVERWORLD).getGameTime() && !GameManager.fishLocations.isEmpty()) {
			for (BlockPos pos : GameManager.fishLocations) {
				Util.summonAttackingFish(server.getLevel(Level.OVERWORLD),new Vec3(pos.getX(),pos.getY(),pos.getZ()));
			}
			GameManager.fishLocations.clear();
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> interact(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == Items.PUFFERFISH || stack.getItem() == Items.TROPICAL_FISH) {
			if (!world.isClientSide) {
				Util.spawnFishFromItem(stack,world,player.blockPosition());
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.pass(stack);
	}
}
