package tfar.fishbeatgameforme;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tfar.fishbeatgameforme.item.LoyaltyTridentItem;
import tfar.fishbeatgameforme.mixin.AttributeSupplierAccess;
import tfar.fishbeatgameforme.mixin.DefaultAttributesAccess;
import tfar.fishbeatgameforme.network.PacketHandler;

import java.util.Map;

public class FishBeatGameForMe implements ModInitializer, CommandRegistrationCallback , ServerTickEvents.EndTick {

	public static final String MODID = "fishbeatgameforme";

	public static final Item FISH_TRIDENT = new LoyaltyTridentItem(new Item.Properties().durability(250).tab(CreativeModeTab.TAB_COMBAT));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM,new ResourceLocation(MODID,"fish_trident"),FISH_TRIDENT);
		CommandRegistrationCallback.EVENT.register(this);
		ServerTickEvents.END_SERVER_TICK.register(this);
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
}
