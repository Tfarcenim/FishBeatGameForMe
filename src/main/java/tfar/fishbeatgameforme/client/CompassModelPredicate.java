package tfar.fishbeatgameforme.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CompassModelPredicate {

    public static final ItemPropertyFunction FUNCTION = new ItemPropertyFunction() {
        private final CompassWobble wobble = new CompassWobble();
        private final CompassWobble wobbleRandom = new CompassWobble();

        public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
            Entity entity = livingEntity != null ? livingEntity : itemStack.getEntityRepresentation();
            if (entity == null) {
                return 0.0F;
            } else {
                if (clientLevel == null && entity.level instanceof ClientLevel) {
                    clientLevel = (ClientLevel) entity.level;
                }

                BlockPos blockPos = CompassItem.isLodestoneCompass(itemStack) ? this.getLodestonePosition(clientLevel, itemStack.getOrCreateTag()) : this.getSpawnPosition(clientLevel);
                long l = clientLevel.getGameTime();
                if (blockPos != null && !(entity.position().distanceToSqr((double) blockPos.getX() + 0.5D, entity.position().y(), (double) blockPos.getZ() + 0.5D) < 9.999999747378752E-6D)) {
                    boolean bl = livingEntity instanceof Player && ((Player) livingEntity).isLocalPlayer();
                    double e = 0.0D;
                    if (bl) {
                        e = livingEntity.yRot;
                    } else if (entity instanceof ItemFrame) {
                        e = this.getFrameRotation((ItemFrame) entity);
                    } else if (entity instanceof ItemEntity) {
                        e = 180.0F - ((ItemEntity) entity).getSpin(0.5F) / 6.2831855F * 360.0F;
                    } else if (livingEntity != null) {
                        e = livingEntity.yBodyRot;
                    }

                    e = Mth.positiveModulo(e / 360.0D, 1.0D);
                    double f = this.getAngleTo(Vec3.atCenterOf(blockPos), entity) / 6.2831854820251465D;
                    double h;
                    if (bl) {
                        if (this.wobble.shouldUpdate(l)) {
                            this.wobble.update(l, 0.5D - (e - 0.25D));
                        }

                        h = f + this.wobble.rotation;
                    } else {
                        h = 0.5D - (e - 0.25D - f);
                    }

                    return Mth.positiveModulo((float) h, 1.0F);
                } else {
                    if (this.wobbleRandom.shouldUpdate(l)) {
                        this.wobbleRandom.update(l, Math.random());
                    }

                    double d = this.wobbleRandom.rotation + (double) ((float) itemStack.hashCode() / 2.14748365E9F);
                    return Mth.positiveModulo((float) d, 1.0F);
                }
            }
        }

        @Nullable
        private BlockPos getSpawnPosition(ClientLevel clientLevel) {
            return clientLevel.dimensionType().natural() ? clientLevel.getSharedSpawnPos() : null;
        }

        @Nullable
        private BlockPos getLodestonePosition(Level level, CompoundTag compoundTag) {
            boolean bl = compoundTag.contains("LodestonePos");
            boolean bl2 = compoundTag.contains("LodestoneDimension");
            if (bl && bl2) {
                Optional<ResourceKey<Level>> optional = CompassItem.getLodestoneDimension(compoundTag);
                if (optional.isPresent() && level.dimension() == optional.get()) {
                    return NbtUtils.readBlockPos(compoundTag.getCompound("LodestonePos"));
                }
            }

            return null;
        }

        private double getFrameRotation(ItemFrame itemFrame) {
            Direction direction = itemFrame.getDirection();
            int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
            return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + itemFrame.getRotation() * 45 + i);
        }

        private double getAngleTo(Vec3 vec3, Entity entity) {
            return Math.atan2(vec3.z() - entity.getZ(), vec3.x() - entity.getX());
        }
    };

    static class CompassWobble {
        private double rotation;
        private double deltaRotation;
        private long lastUpdateTick;

        private CompassWobble() {
        }

        private boolean shouldUpdate(long l) {
            return this.lastUpdateTick != l;
        }

        private void update(long l, double d) {
            this.lastUpdateTick = l;
            double e = d - this.rotation;
            e = Mth.positiveModulo(e + 0.5D, 1.0D) - 0.5D;
            this.deltaRotation += e * 0.1D;
            this.deltaRotation *= 0.8D;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0D);
        }
    }

}
