package com.rentapolt.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;

/**
 * Pack hunting AI goal - entities attack the same target when nearby pack members are fighting
 */
public class PackHuntingGoal extends Goal {
    private final MobEntity mob;
    private final Class<? extends MobEntity> packMateClass;
    private final double searchRadius;
    private final float packBonusDamage;
    private LivingEntity sharedTarget;
    private int targetCheckCooldown;

    public PackHuntingGoal(MobEntity mob, Class<? extends MobEntity> packMateClass, double searchRadius, float packBonusDamage) {
        this.mob = mob;
        this.packMateClass = packMateClass;
        this.searchRadius = searchRadius;
        this.packBonusDamage = packBonusDamage;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (this.targetCheckCooldown > 0) {
            this.targetCheckCooldown--;
            return false;
        }
        
        this.targetCheckCooldown = 20; // Check every second
        
        // Look for nearby pack mates who are fighting
        List<? extends MobEntity> packMates = this.mob.getWorld().getEntitiesByClass(
            this.packMateClass,
            Box.of(this.mob.getPos(), 1.0D, 1.0D, 1.0D).expand(this.searchRadius),
            mate -> mate != this.mob && mate.getTarget() != null && mate.isAlive()
        );
        
        if (!packMates.isEmpty()) {
            // Join the fight - target what pack mates are targeting
            for (MobEntity mate : packMates) {
                LivingEntity mateTarget = mate.getTarget();
                if (mateTarget != null && mateTarget.isAlive() && this.mob.canSee(mateTarget)) {
                    this.sharedTarget = mateTarget;
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.sharedTarget != null && 
               this.sharedTarget.isAlive() && 
               this.mob.squaredDistanceTo(this.sharedTarget) < 400.0D; // Within 20 blocks
    }

    @Override
    public void start() {
        this.mob.setTarget(this.sharedTarget);
    }

    @Override
    public void stop() {
        this.sharedTarget = null;
    }

    @Override
    public void tick() {
        if (this.sharedTarget != null) {
            this.mob.getLookControl().lookAt(this.sharedTarget, 30.0F, 30.0F);
            
            // Count nearby pack mates attacking same target
            long packSize = this.mob.getWorld().getEntitiesByClass(
                this.packMateClass,
                Box.of(this.mob.getPos(), 1.0D, 1.0D, 1.0D).expand(10.0D),
                mate -> mate.getTarget() == this.sharedTarget
            ).size();
            
            // Apply pack bonus if multiple pack members attacking (handled in entity damage)
            if (packSize >= 2) {
                // Pack bonus will be applied in the entity's damage method
                // Store pack size in entity data for damage calculation
            }
        }
    }

    public int getPackSize() {
        if (this.sharedTarget == null) return 1;
        
        return (int) this.mob.getWorld().getEntitiesByClass(
            this.packMateClass,
            Box.of(this.mob.getPos(), 1.0D, 1.0D, 1.0D).expand(10.0D),
            mate -> mate.getTarget() == this.sharedTarget
        ).size();
    }
}
