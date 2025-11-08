package com.rentapolt.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.rentapolt.entity.config.RentapoltMobConfig;
import com.rentapolt.entity.config.RentapoltMobConfig.SpecialAbility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.entity.SpawnReason;

public class RentapoltHostileEntity extends HostileEntity {
    private static final RentapoltMobConfig DEFAULT =
            new RentapoltMobConfig(30.0F, 6.0F, 0.28D, 20.0D, SpecialAbility.TOXIC_CLOUD, false, 0.7F, 2.2F);

    private RentapoltMobConfig config = DEFAULT;

    public RentapoltHostileEntity(EntityType<? extends RentapoltHostileEntity> type, World world) {
        super(type, world);
    }

    public RentapoltHostileEntity configure(@NotNull RentapoltMobConfig config) {
        this.config = config;
        this.calculateDimensions();
        this.setPersistent();
        return this;
    }

    public static DefaultAttributeContainer.Builder createAttributes(RentapoltMobConfig config) {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, config.maxHealth())
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, config.attackDamage())
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, config.movementSpeed())
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, config.followRange());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 16.0F));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, RentapoltPassiveEntity.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            applyAmbientAbility();
        }
    }

    private void applyAmbientAbility() {
        switch (config.ability()) {
            case FIRE_AURA -> {
                if (this.age % 40 == 0) {
                    igniteNearby(3.5D);
                }
            }
            case TOXIC_CLOUD -> {
                if (this.age % 60 == 0) {
                    affectNearby(StatusEffects.POISON, 120, 0, 4.0D);
                    affectNearby(StatusEffects.NAUSEA, 120, 0, 4.0D);
                }
            }
            case MINI_EXPLOSION -> {
                LivingEntity target = this.getTarget();
                if (target != null && this.age % 45 == 0 && this.squaredDistanceTo(target) < 9) {
                    this.getWorld().createExplosion(this, target.getX(), target.getBodyY(0.5D), target.getZ(), 1.6F,
                            World.ExplosionSourceType.MOB);
                }
            }
            case PLASMA_SHOT -> {
                LivingEntity target = this.getTarget();
                if (target != null && this.age % 50 == 0) {
                    Vec3d dir = target.getPos().subtract(this.getPos()).normalize();
                    SmallFireballEntity fireball = new SmallFireballEntity(this.getWorld(), this,
                            dir.x, dir.y + 0.1D, dir.z);
                    fireball.refreshPositionAfterTeleport(this.getX(), this.getBodyY(0.5D), this.getZ());
                    this.getWorld().spawnEntity(fireball);
                }
            }
            default -> {
            }
        }
    }

    private void igniteNearby(double radius) {
        List<LivingEntity> victims = this.getWorld().getEntitiesByClass(LivingEntity.class,
                Box.of(this.getPos(), 1.0D, 1.0D, 1.0D).expand(radius), entity -> entity != this);
        for (LivingEntity living : victims) {
            living.setOnFireFor(4);
        }
    }

    private void affectNearby(StatusEffect effect, int duration, int amplifier, double radius) {
        List<PlayerEntity> victims = this.getWorld().getEntitiesByClass(PlayerEntity.class,
                Box.of(this.getPos(), 1.0D, 1.0D, 1.0D).expand(radius), entity -> true);
        for (PlayerEntity player : victims) {
            player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, true, true));
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean hurt = super.damage(source, amount);
        if (hurt && config.ability() == SpecialAbility.SHADOW_CURSE && source.getAttacker() instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 0));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 0));
        }
        return hurt;
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean hit = super.tryAttack(target);
        if (hit && !this.getWorld().isClient && target instanceof LivingEntity living) {
            switch (config.ability()) {
                case FIRE_AURA -> living.setOnFireFor(5);
                case LIGHTNING_STRIKE -> {
                    LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(this.getWorld());
                    if (lightning != null) {
                        lightning.refreshPositionAfterTeleport(living.getX(), living.getBodyY(0.5D), living.getZ());
                        this.getWorld().spawnEntity(lightning);
                    }
                }
                case SHADOW_CURSE -> {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80, 1));
                }
                default -> {
                }
            }
        }
        return hit;
    }

    @Override
    public boolean isFireImmune() {
        return config.fireImmune() || super.isFireImmune();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 EntityData entityData, net.minecraft.nbt.NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(config.maxHealth());
        this.setHealth(config.maxHealth());
        this.calculateDimensions();
        return data;
    }
}
