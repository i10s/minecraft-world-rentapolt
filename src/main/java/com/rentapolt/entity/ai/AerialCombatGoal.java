package com.rentapolt.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

/**
 * AI goal for aerial combat - circle at altitude, dive-bomb targets, return to sky
 */
public class AerialCombatGoal extends Goal {
    private final MobEntity flyer;
    private final double cruisingAltitude;
    private final double diveSpeed;
    private final double riseSpeed;
    private final int diveCooldown;
    private int diveTimer;
    private boolean isDiving;
    private Vec3d circleCenter;
    private float circleAngle;

    public AerialCombatGoal(MobEntity flyer, double cruisingAltitude, double diveSpeed, double riseSpeed, int diveCooldown) {
        this.flyer = flyer;
        this.cruisingAltitude = cruisingAltitude;
        this.diveSpeed = diveSpeed;
        this.riseSpeed = riseSpeed;
        this.diveCooldown = diveCooldown;
        this.diveTimer = diveCooldown;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.flyer.getTarget() != null || this.flyer.isOnGround();
    }

    @Override
    public boolean shouldContinue() {
        return true; // Always active for flying entities
    }

    @Override
    public void start() {
        this.circleCenter = this.flyer.getPos();
        this.circleAngle = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.flyer.getTarget();
        double currentAltitude = this.flyer.getY();
        
        if (this.flyer.isOnGround()) {
            // Take off immediately
            this.flyer.addVelocity(0, 0.5D, 0);
            this.flyer.velocityModified = true;
            return;
        }
        
        if (target != null && target.isAlive()) {
            // Combat mode
            double targetAltitude = target.getY() + this.cruisingAltitude;
            
            if (this.diveTimer > 0) {
                this.diveTimer--;
            }
            
            if (!isDiving && currentAltitude >= targetAltitude && this.diveTimer == 0) {
                // Start dive attack
                isDiving = true;
                this.diveTimer = this.diveCooldown;
            }
            
            if (isDiving) {
                // Dive towards target
                Vec3d targetPos = target.getPos();
                Vec3d direction = targetPos.subtract(this.flyer.getPos()).normalize();
                this.flyer.setVelocity(direction.multiply(this.diveSpeed));
                this.flyer.velocityModified = true;
                
                // Stop diving when close or at ground level
                if (this.flyer.squaredDistanceTo(target) < 9.0D || currentAltitude < target.getY() + 3) {
                    isDiving = false;
                }
            } else {
                // Rise back to cruising altitude
                if (currentAltitude < targetAltitude) {
                    Vec3d currentVel = this.flyer.getVelocity();
                    this.flyer.setVelocity(currentVel.x * 0.8, this.riseSpeed, currentVel.z * 0.8);
                    this.flyer.velocityModified = true;
                } else {
                    // Circle above target
                    circleAboveTarget(target);
                }
            }
        } else {
            // No target - maintain cruising altitude and circle
            double desiredAltitude = this.circleCenter.y + this.cruisingAltitude;
            
            if (currentAltitude < desiredAltitude - 2) {
                // Rise
                Vec3d currentVel = this.flyer.getVelocity();
                this.flyer.setVelocity(currentVel.x * 0.9, this.riseSpeed * 0.7, currentVel.z * 0.9);
                this.flyer.velocityModified = true;
            } else if (currentAltitude > desiredAltitude + 2) {
                // Descend
                Vec3d currentVel = this.flyer.getVelocity();
                this.flyer.setVelocity(currentVel.x * 0.9, -this.riseSpeed * 0.5, currentVel.z * 0.9);
                this.flyer.velocityModified = true;
            } else {
                // Circle at altitude
                circleAtPosition(this.circleCenter);
            }
        }
    }

    private void circleAboveTarget(LivingEntity target) {
        this.circleAngle += 0.05F; // Circle speed
        double radius = 8.0D;
        
        Vec3d targetPos = target.getPos();
        double x = targetPos.x + Math.cos(this.circleAngle) * radius;
        double z = targetPos.z + Math.sin(this.circleAngle) * radius;
        double y = targetPos.y + this.cruisingAltitude;
        
        Vec3d circlePos = new Vec3d(x, y, z);
        Vec3d direction = circlePos.subtract(this.flyer.getPos()).normalize();
        
        this.flyer.setVelocity(direction.multiply(0.3));
        this.flyer.velocityModified = true;
        this.flyer.getLookControl().lookAt(target, 30.0F, 30.0F);
    }

    private void circleAtPosition(Vec3d center) {
        this.circleAngle += 0.03F;
        double radius = 10.0D;
        
        double x = center.x + Math.cos(this.circleAngle) * radius;
        double z = center.z + Math.sin(this.circleAngle) * radius;
        double y = center.y + this.cruisingAltitude;
        
        Vec3d circlePos = new Vec3d(x, y, z);
        Vec3d direction = circlePos.subtract(this.flyer.getPos()).normalize();
        
        this.flyer.setVelocity(direction.multiply(0.2));
        this.flyer.velocityModified = true;
    }
}
