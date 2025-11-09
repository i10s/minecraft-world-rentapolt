package com.rentapolt.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

/**
 * AI goal for coordinated flanking attacks - entities position themselves around the target
 */
public class FlankingAttackGoal extends Goal {
    private final MobEntity attacker;
    private final Class<? extends MobEntity> allyClass;
    private final double searchRadius;
    private final double flankDistance;
    private Vec3d flankPosition;
    private int repositionTimer;

    public FlankingAttackGoal(MobEntity attacker, Class<? extends MobEntity> allyClass, double searchRadius, double flankDistance) {
        this.attacker = attacker;
        this.allyClass = allyClass;
        this.searchRadius = searchRadius;
        this.flankDistance = flankDistance;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.attacker.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        
        // Check if there are allies nearby
        long allyCount = this.attacker.getWorld().getEntitiesByClass(
            this.allyClass,
            Box.of(this.attacker.getPos(), 1.0D, 1.0D, 1.0D).expand(this.searchRadius),
            ally -> ally != this.attacker && ally.getTarget() == target
        ).size();
        
        return allyCount > 0; // Only flank if there are allies
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity target = this.attacker.getTarget();
        return target != null && target.isAlive() && 
               this.attacker.squaredDistanceTo(target) < this.searchRadius * this.searchRadius;
    }

    @Override
    public void start() {
        this.repositionTimer = 0;
        calculateFlankPosition();
    }

    @Override
    public void tick() {
        LivingEntity target = this.attacker.getTarget();
        if (target == null) return;
        
        this.repositionTimer--;
        if (this.repositionTimer <= 0) {
            calculateFlankPosition();
            this.repositionTimer = 60; // Reposition every 3 seconds
        }
        
        // Move to flank position
        if (this.flankPosition != null) {
            double distanceToFlank = this.attacker.getPos().squaredDistanceTo(this.flankPosition);
            if (distanceToFlank > 4.0D) { // Not at position yet
                this.attacker.getNavigation().startMovingTo(this.flankPosition.x, this.flankPosition.y, this.flankPosition.z, 1.0D);
            }
        }
        
        // Always look at target
        this.attacker.getLookControl().lookAt(target, 30.0F, 30.0F);
    }

    private void calculateFlankPosition() {
        LivingEntity target = this.attacker.getTarget();
        if (target == null) return;
        
        // Get all allies attacking the same target
        List<? extends MobEntity> allies = this.attacker.getWorld().getEntitiesByClass(
            this.allyClass,
            Box.of(this.attacker.getPos(), 1.0D, 1.0D, 1.0D).expand(this.searchRadius),
            ally -> ally.getTarget() == target
        );
        
        // Calculate flank angle based on position in group
        int groupSize = allies.size() + 1; // +1 for this entity
        int myIndex = 0;
        for (int i = 0; i < allies.size(); i++) {
            if (allies.get(i).getId() < this.attacker.getId()) {
                myIndex++;
            }
        }
        
        // Distribute entities around target in a circle
        double anglePerEntity = (Math.PI * 2) / groupSize;
        double myAngle = anglePerEntity * myIndex;
        
        Vec3d targetPos = target.getPos();
        double x = targetPos.x + Math.cos(myAngle) * this.flankDistance;
        double z = targetPos.z + Math.sin(myAngle) * this.flankDistance;
        
        this.flankPosition = new Vec3d(x, targetPos.y, z);
    }

    @Override
    public void stop() {
        this.flankPosition = null;
        this.attacker.getNavigation().stop();
    }
}
