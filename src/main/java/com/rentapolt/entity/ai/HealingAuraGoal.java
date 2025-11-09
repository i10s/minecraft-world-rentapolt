package com.rentapolt.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;

/**
 * AI goal for healing nearby friendly entities
 */
public class HealingAuraGoal extends Goal {
    private final MobEntity healer;
    private final double radius;
    private final int healAmount;
    private final int cooldown;
    private int healTimer;

    public HealingAuraGoal(MobEntity healer, double radius, int healAmount, int cooldown) {
        this.healer = healer;
        this.radius = radius;
        this.healAmount = healAmount;
        this.cooldown = cooldown;
        this.healTimer = cooldown;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.healTimer > 0) {
            this.healTimer--;
            return false;
        }
        
        // Check if there are injured entities nearby
        List<LivingEntity> nearby = this.healer.getWorld().getEntitiesByClass(
            LivingEntity.class,
            Box.of(this.healer.getPos(), 1.0D, 1.0D, 1.0D).expand(this.radius),
            entity -> entity != this.healer && 
                     entity.isAlive() && 
                     (entity instanceof PlayerEntity || entity instanceof AnimalEntity) &&
                     entity.getHealth() < entity.getMaxHealth()
        );
        
        return !nearby.isEmpty();
    }

    @Override
    public boolean shouldContinue() {
        return false; // One-time heal
    }

    @Override
    public void start() {
        // Heal nearby friendly entities
        List<LivingEntity> nearby = this.healer.getWorld().getEntitiesByClass(
            LivingEntity.class,
            Box.of(this.healer.getPos(), 1.0D, 1.0D, 1.0D).expand(this.radius),
            entity -> entity != this.healer && 
                     entity.isAlive() && 
                     (entity instanceof PlayerEntity || entity instanceof AnimalEntity)
        );
        
        for (LivingEntity entity : nearby) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(this.healAmount);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0));
                
                // Spawn healing particles
                if (this.healer.getWorld() instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                        ParticleTypes.HEART,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        5, 0.5D, 0.5D, 0.5D, 0.1D
                    );
                }
            }
        }
        
        this.healTimer = this.cooldown;
    }
}
