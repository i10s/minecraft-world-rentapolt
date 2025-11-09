package com.rentapolt.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;

/**
 * AI goal for protecting specific entity types (e.g., lions protect elephants)
 */
public class ProtectAllyGoal extends Goal {
    private final MobEntity protector;
    private final Class<? extends AnimalEntity> allyClass;
    private final double searchRadius;
    private final float attackRange;
    private LivingEntity attacker;
    private AnimalEntity allyBeingAttacked;
    private int checkCooldown;

    public ProtectAllyGoal(MobEntity protector, Class<? extends AnimalEntity> allyClass, double searchRadius, float attackRange) {
        this.protector = protector;
        this.allyClass = allyClass;
        this.searchRadius = searchRadius;
        this.attackRange = attackRange;
        this.setControls(EnumSet.of(Control.TARGET, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.checkCooldown > 0) {
            this.checkCooldown--;
            return false;
        }
        
        this.checkCooldown = 10; // Check every half second
        
        // Find nearby allies
        List<? extends AnimalEntity> allies = this.protector.getWorld().getEntitiesByClass(
            this.allyClass,
            Box.of(this.protector.getPos(), 1.0D, 1.0D, 1.0D).expand(this.searchRadius),
            ally -> ally.isAlive()
        );
        
        // Check if any ally is being attacked
        for (AnimalEntity ally : allies) {
            LivingEntity recentAttacker = ally.getAttacker();
            if (recentAttacker != null && recentAttacker.isAlive() && 
                this.protector.squaredDistanceTo(recentAttacker) < this.searchRadius * this.searchRadius) {
                this.allyBeingAttacked = ally;
                this.attacker = recentAttacker;
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.attacker != null && 
               this.attacker.isAlive() && 
               this.protector.squaredDistanceTo(this.attacker) < this.searchRadius * this.searchRadius * 2;
    }

    @Override
    public void start() {
        this.protector.setTarget(this.attacker);
        this.protector.getNavigation().startMovingTo(this.attacker, 1.2D);
    }

    @Override
    public void stop() {
        this.attacker = null;
        this.allyBeingAttacked = null;
    }

    @Override
    public void tick() {
        if (this.attacker != null) {
            this.protector.getLookControl().lookAt(this.attacker, 30.0F, 30.0F);
            
            double distanceToAttacker = this.protector.squaredDistanceTo(this.attacker);
            if (distanceToAttacker < this.attackRange * this.attackRange) {
                // In attack range - let the attack goal handle it
                this.protector.getNavigation().stop();
            } else {
                // Move closer
                this.protector.getNavigation().startMovingTo(this.attacker, 1.2D);
            }
        }
    }
}
