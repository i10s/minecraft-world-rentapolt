package com.rentapolt.entity.boss;

import com.rentapolt.entity.RentapoltHostileEntity;
import com.rentapolt.entity.config.RentapoltMobConfig;
import com.rentapolt.entity.config.RentapoltMobConfig.SpecialAbility;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Shadow King Boss - spawns in bunker secret rooms
 * Features:
 * - 450 HP
 * - Teleportation (like enderman but aggressive)
 * - Shadow clone ability
 * - Blindness aura
 * - Invisibility phases
 */
public class ShadowKingEntity extends RentapoltHostileEntity {
    private static final RentapoltMobConfig SHADOW_KING_CONFIG = 
        new RentapoltMobConfig(450.0F, 18.0F, 0.32D, 45.0D, SpecialAbility.PLASMA_SHOT, true, 2.0F, 3.0F);
    
    private final ServerBossBar bossBar;
    private int teleportCooldown = 0;
    private int invisibilityCooldown = 0;
    private boolean isInvisible = false;
    private int invisibilityDuration = 0;

    public ShadowKingEntity(EntityType<? extends ShadowKingEntity> type, World world) {
        super(type, world);
        this.configure(SHADOW_KING_CONFIG);
        this.bossBar = new ServerBossBar(
            Text.literal("Shadow King"),
            BossBar.Color.PURPLE,
            BossBar.Style.PROGRESS
        );
    }

    public static DefaultAttributeContainer.Builder createShadowKingAttributes() {
        return createAttributes(SHADOW_KING_CONFIG)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D)
            .add(EntityAttributes.GENERIC_ARMOR, 10.0D);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.getWorld().isClient) {
            // Update boss bar
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            
            // Invisibility management
            if (isInvisible) {
                invisibilityDuration--;
                if (invisibilityDuration <= 0) {
                    exitInvisibility();
                }
                // Spawn shadow particles
                spawnShadowParticles();
            } else if (invisibilityCooldown > 0) {
                invisibilityCooldown--;
            } else if (this.getHealth() / this.getMaxHealth() < 0.6f) {
                enterInvisibility();
            }
            
            // Teleport behavior
            if (teleportCooldown > 0) {
                teleportCooldown--;
            } else {
                attemptTeleport();
                teleportCooldown = 80; // 4 seconds
            }
            
            // Blindness aura
            if (this.age % 60 == 0) { // Every 3 seconds
                applyBlindnessAura();
            }
        }
    }

    private void enterInvisibility() {
        isInvisible = true;
        invisibilityDuration = 100; // 5 seconds
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 100, 0, false, false));
        this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.8F);
        
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                ParticleTypes.SMOKE,
                this.getX(), this.getY() + 1, this.getZ(),
                20, 0.5D, 1.0D, 0.5D, 0.05D
            );
        }
    }

    private void exitInvisibility() {
        isInvisible = false;
        invisibilityCooldown = 200; // 10 seconds cooldown
        this.removeStatusEffect(StatusEffects.INVISIBILITY);
        this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.2F);
        
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                ParticleTypes.PORTAL,
                this.getX(), this.getY() + 1, this.getZ(),
                30, 0.5D, 1.0D, 0.5D, 0.1D
            );
        }
    }

    private void spawnShadowParticles() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                ParticleTypes.SMOKE,
                this.getX(), this.getY() + 0.5, this.getZ(),
                2, 0.3D, 0.5D, 0.3D, 0.01D
            );
        }
    }

    private void attemptTeleport() {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        
        // Teleport near target (behind them for backstab)
        Vec3d targetPos = target.getPos();
        Vec3d targetLook = target.getRotationVector();
        Vec3d behindTarget = targetPos.subtract(targetLook.multiply(3.0D));
        
        BlockPos teleportPos = new BlockPos((int)behindTarget.x, (int)behindTarget.y, (int)behindTarget.z);
        
        // Validate teleport location
        if (this.getWorld().getBlockState(teleportPos).isAir() && 
            this.getWorld().getBlockState(teleportPos.up()).isAir()) {
            
            // Teleport effects
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    this.getX(), this.getY() + 1, this.getZ(),
                    20, 0.5D, 1.0D, 0.5D, 0.1D
                );
            }
            
            this.teleport(behindTarget.x, behindTarget.y, behindTarget.z);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                    ParticleTypes.PORTAL,
                    this.getX(), this.getY() + 1, this.getZ(),
                    20, 0.5D, 1.0D, 0.5D, 0.1D
                );
            }
        }
    }

    private void applyBlindnessAura() {
        List<LivingEntity> nearby = this.getWorld().getEntitiesByClass(
            LivingEntity.class,
            Box.of(this.getPos(), 1.0D, 1.0D, 1.0D).expand(10.0D),
            entity -> entity != this && entity.isAlive()
        );
        
        for (LivingEntity entity : nearby) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // 30% chance to teleport away when hit
        if (!this.getWorld().isClient && this.random.nextFloat() < 0.3f && teleportCooldown == 0) {
            attemptTeleport();
            teleportCooldown = 40; // Reset cooldown
        }
        
        return super.damage(source, amount);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMAN_DEATH;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }
}
