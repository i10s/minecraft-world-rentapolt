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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

/**
 * Mega Mutant Boss - spawns in Mutant Zone
 * Features:
 * - 500 HP
 * - Multiple attack phases
 * - Summons minions at 50% HP
 * - Enrage mode at 25% HP
 */
public class MegaMutantEntity extends RentapoltHostileEntity {
    private static final RentapoltMobConfig MEGA_MUTANT_CONFIG = 
        new RentapoltMobConfig(500.0F, 15.0F, 0.25D, 40.0D, SpecialAbility.TOXIC_CLOUD, true, 2.0F, 3.0F);
    
    private final ServerBossBar bossBar;
    private boolean phase2Triggered = false;
    private boolean phase3Triggered = false;
    private int attackCooldown = 0;

    public MegaMutantEntity(EntityType<? extends MegaMutantEntity> type, World world) {
        super(type, world);
        this.configure(MEGA_MUTANT_CONFIG);
        this.bossBar = new ServerBossBar(
            Text.literal("Mega Mutant"),
            BossBar.Color.GREEN,
            BossBar.Style.PROGRESS
        );
        this.bossBar.setDarkenSky(true);
    }

    public static DefaultAttributeContainer.Builder createMegaMutantAttributes() {
        return createAttributes(MEGA_MUTANT_CONFIG)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8D)
            .add(EntityAttributes.GENERIC_ARMOR, 15.0D);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.getWorld().isClient) {
            // Update boss bar
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            
            // Phase transitions
            float healthPercent = this.getHealth() / this.getMaxHealth();
            
            // Phase 2: 50% HP - Summon minions
            if (healthPercent <= 0.5f && !phase2Triggered) {
                triggerPhase2();
            }
            
            // Phase 3: 25% HP - Enrage
            if (healthPercent <= 0.25f && !phase3Triggered) {
                triggerPhase3();
            }
            
            // Special attacks
            if (attackCooldown > 0) {
                attackCooldown--;
            } else {
                performSpecialAttack();
                attackCooldown = 100; // 5 seconds between specials
            }
        }
    }

    private void triggerPhase2() {
        phase2Triggered = true;
        this.bossBar.setColor(BossBar.Color.YELLOW);
        
        // Summon minions (would need to implement minion spawning)
        // For now, just apply massive toxic cloud
        List<LivingEntity> nearby = this.getWorld().getEntitiesByClass(
            LivingEntity.class,
            Box.of(this.getPos(), 1.0D, 1.0D, 1.0D).expand(15.0D),
            entity -> entity != this && !(entity instanceof MegaMutantEntity)
        );
        
        for (LivingEntity entity : nearby) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 2));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1));
        }
        
        this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 2.0F, 0.8F);
    }

    private void triggerPhase3() {
        phase3Triggered = true;
        this.bossBar.setColor(BossBar.Color.RED);
        
        // Enrage: increased speed and damage
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(25.0F);
        
        // Heal slightly
        this.heal(50.0F);
        
        this.playSound(SoundEvents.ENTITY_WARDEN_ROAR, 2.0F, 0.6F);
    }

    private void performSpecialAttack() {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        
        double distance = this.squaredDistanceTo(target);
        
        if (distance < 100) { // Within 10 blocks
            // Ground slam attack
            List<LivingEntity> victims = this.getWorld().getEntitiesByClass(
                LivingEntity.class,
                Box.of(this.getPos(), 1.0D, 1.0D, 1.0D).expand(6.0D),
                entity -> entity != this
            );
            
            for (LivingEntity victim : victims) {
                victim.damage(this.getDamageSources().mobAttack(this), 10.0F);
                victim.addVelocity(0, 0.8D, 0);
                victim.velocityModified = true;
            }
            
            this.getWorld().createExplosion(
                this,
                this.getX(), this.getY(), this.getZ(),
                3.0F,
                World.ExplosionSourceType.MOB
            );
        }
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
        return SoundEvents.ENTITY_RAVAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_RAVAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RAVAGER_DEATH;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false; // Bosses don't despawn
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }
}
