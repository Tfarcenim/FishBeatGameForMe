package tfar.fishbeatgameforme.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.fishbeatgameforme.FishBeatGameForMe;

import java.util.Arrays;

@EnvironmentInterfaces({@EnvironmentInterface(
        value = EnvType.CLIENT,
        itf = ItemSupplier.class
)})
public class WaterboltEntity extends AbstractHurtingProjectile implements ItemSupplier {

    double baseDamage = 5;
    double knockback = 1;

    public WaterboltEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    //@Environment(EnvType.CLIENT)
    public WaterboltEntity(Level level, double d, double e, double f, double g, double h, double i) {
        super(FishBeatGameForMe.WATER_BOLT, d, e, f, g, h, i, level);
    }

    public WaterboltEntity(double d, double e, double f, double g, double h, double i, Level level) {
        super(FishBeatGameForMe.WATER_BOLT, d, e, f, g, h, i, level);
    }

    public WaterboltEntity(LivingEntity livingEntity, double d, double e, double f, Level level) {
        super(FishBeatGameForMe.WATER_BOLT, livingEntity, d, e, f, level);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SPLASH;
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.baseDamage, 0.0D, 2.147483647E9D));


        long l = this.random.nextInt(i / 2 + 2);
        i = (int)Math.min(l + (long)i, 2147483647L);

        Entity entity2 = this.getOwner();
        DamageSource damageSource2;
        if (entity2 == null) {
            damageSource2 = waterbolt(this, this);
        } else {
            damageSource2 = waterbolt(this, entity2);
            if (entity2 instanceof LivingEntity) {
                ((LivingEntity)entity2).setLastHurtMob(entity);
            }
        }

        boolean bl = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getRemainingFireTicks();
        if (this.isOnFire() && !bl) {
            entity.setSecondsOnFire(5);
        }

        if (entity.hurt(damageSource2, (float)i)) {
            if (bl) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;

                if (this.knockback > 0) {
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.knockback * 0.6D);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingEntity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (!this.level.isClientSide && entity2 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, entity2);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity2, livingEntity);
                }

             //   this.doPostHurtEffects(livingEntity);
                if (entity2 != null && livingEntity != entity2 && livingEntity instanceof Player && entity2 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity2).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }

           // this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        } else {
            entity.setRemainingFireTicks(j);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.yRot += 180.0F;
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                this.remove();
            }
        }

    }

    public static DamageSource waterbolt(WaterboltEntity abstractArrow, @Nullable Entity entity) {
        return (new IndirectEntityDamageSource("waterbolt", abstractArrow, entity)).setProjectile();
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.BLUE_CONCRETE);
    }
}
