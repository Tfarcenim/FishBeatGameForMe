package tfar.fishbeatgameforme.item;

import draylar.identity.Identity;
import draylar.identity.cca.UnlockedIdentitiesComponent;
import draylar.identity.registry.Components;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.fishbeatgameforme.Hooks;
import tfar.fishbeatgameforme.ModUtil;
import tfar.fishbeatgameforme.entity.WaterboltEntity;

import java.util.List;

public class SpecialCompassItem extends DiggerItem {


    public SpecialCompassItem(float f, float g, Tier tier, Properties properties) {
        super(f, g, tier, null, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack stack = player.getItemInHand(interactionHand);

        if (!level.isClientSide) {
            int xpLevel = stack.hasTag() ? stack.getTag().getInt("xp") : 0;
            if (xpLevel > 2) {//level 4 or better

                Vec3 vec = player.getLookAngle();

                double x = vec.x;
                double y = vec.y;
                double z = vec.z;
                WaterboltEntity waterboltEntity = new WaterboltEntity(player, x, y, z, level);
                waterboltEntity.setPos(waterboltEntity.getX(), player.getY(0.5D) + 0.5D, waterboltEntity.getZ());

                level.addFreshEntity(waterboltEntity);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        int xpLevel = itemStack.hasTag() ? itemStack.getTag().getInt("xp") : 0;
        if (xpLevel > 1 && !level.isClientSide && entity instanceof Player) {
            if (level.getGameTime() % 200 == 0)
            ModUtil.summonAttackingFish((Player)entity);

            if (xpLevel > 2) {
                grantFish((ServerPlayer)entity);
            }
        }
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        int xpLevel = itemStack.hasTag() ? itemStack.getTag().getInt("xp") : 0;
        if (Items.DIAMOND_AXE.getDestroySpeed(itemStack, blockState) > 1) {
            return 6;//???
        } else if (xpLevel > 1) {
            return Items.DIAMOND_PICKAXE.getDestroySpeed(itemStack, blockState);
        }
        return 1;//this.blocks.contains(blockState.getBlock()) ? this.speed : 1.0F;
    }

    private static void grantFish(ServerPlayer player) {
        UnlockedIdentitiesComponent unlocked = Components.UNLOCKED_IDENTITIES.get(player);
        EntityType<?> entity = EntityType.TROPICAL_FISH;
        if (!unlocked.has(entity)) {
            boolean result = unlocked.unlock(entity);
            if (result && Identity.CONFIG.logCommands) {
                player.displayClientMessage(new TranslatableComponent("identity.unlock_entity", new TranslatableComponent(entity.getDescriptionId())), true);
              //  source.displayClientMessage(new TranslatableComponent("identity.grant_success", new Object[]{new TranslatableComponent(entity.getDescriptionId()), player.getDisplayName()}), true);
            }
        } else if (Identity.CONFIG.logCommands) {
          //  source.displayClientMessage(new TranslatableComponent("identity.already_has", new Object[]{player.getDisplayName(), new TranslatableComponent(entity.getDescriptionId())}), true);
        }

    }

    //I can mixin to ItemStack to make this stack-sensitive, but is that needed here?
    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return Items.DIAMOND_PICKAXE.isCorrectToolForDrops(blockState);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag()) {

            int xpPoints = itemStack.getTag().getInt("xp");

            int levels = itemStack.getTag().getInt("level");;

            list.add(new TextComponent("Level: "+ (levels + 1)));

            if (levels < Hooks.UPGRADEABLES.size()) {

                list.add(new TextComponent("Progress: " + xpPoints + " /"+ Hooks.UPGRADEABLES.get(levels).getRight()));
            }
        }
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (blockState.is(BlockTags.LOGS)) {
            badVeinmine(level,blockPos);
        }
        return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
    }

    private static void badVeinmine(Level level,BlockPos start) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(start.above());
        while (level.getBlockState(mutable).is(BlockTags.LOGS)) {
            level.destroyBlock(mutable,true);
            mutable.move(Direction.UP);
        }
    }
}
