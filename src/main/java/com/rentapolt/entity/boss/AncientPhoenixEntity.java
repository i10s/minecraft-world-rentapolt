package com.rentapolt.entity.boss;

import com.rentapolt.entity.RentapoltHostileEntity;
import com.rentapolt.entity.config.RentapoltMobConfig;
import com.rentapolt.entity.config.RentapoltMobConfig.SpecialAbility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Ancient Phoenix Boss - spawns on floating islands
 * Features:
 * - 400 HP
 * - Flying combat
 * - Fire immunity requirement (takes no damage unless attacker has fire resistance)
 * - Drops phoenix egg on death
 * - Firestorm attack
 */
public class AncientPhoenixEntity extends RentapoltHostileEntity {
    private static final RentapoltMobConfig PHOENIX_CONFIG = 
        new RentapoltMobConfig(400.0F, 12.0F, 0.3D, 50.0D, SpecialAbility.FIRE_AURA, true, 2.5F, 3.5F);
    
    private final ServerBossBar bossBar;
    private int fireballCooldown = 0;
    private int firestormCooldown = 0;
    private boolean isAscending = false;
    private int ascensionTimer = 0;

    public AncientPhoenixEntity(EntityType<? extends AncientPhoenixEntity> type, World world) {
        super(type, world);
        this.configure(PHOENIX_CONFIG);
        this.bossBar = new ServerBossBar(
            Text.literal("Ancient Phoenix"),
            BossBar.Color.YELLOW,
            BossBar.Style.PROGRESS
        );
        this.setNoGravity(true); // Flying entity
    }

    public static DefaultAttributeContainer.Builder createPhoenixAttributes() {
        return createAttributes(PHOENIX_CONFIG)
            .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6D);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.getWorld().isClient) {
            // Update boss bar
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            
            // Flying behavior
            if (isAscending) {
                ascensionTimer--;
                this.addVelocity(0, 0.1D, 0);
                if (ascensionTimer <= 0) {
                    isAscending = false;
                }
            } else {
                // Maintain altitude
                if (this.isOnGround()) {
                    this.addVelocity(0, 0.3D, 0);
                }
            }
            
            // Fireball attack
            if (fireballCooldown > 0) {
                fireballCooldown--;
            } else {
                shootFireball();
                fireballCooldown = 40; // 2 seconds
            }
            
            // Firestorm attack (low HP)
            if (this.getHealth() / this.getMaxHealth() < 0.5f) {
                if (firestormCooldown > 0) {
                    firestormCooldown--;
                } else {
                    performFirestorm();
                    firestormCooldown = 200; // 10 seconds
                }
            }
        }
    }

    private void shootFireball() {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        
        Vec3d direction = target.getPos().subtract(this.getPos()).normalize();
        SmallFireballEntity fireball = new SmallFireballEntity(
            this.getWorld(),
            this,
            direction.x,
            direction.y,
            direction.z
        );
        
        fireball.setPosition(this.getX(), this.getEyeY(), this.getZ());
        this.getWorld().spawnEntity(fireball);
        this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
    }

    private void performFirestorm() {
        // Ascend first
        isAscending = true;
        ascensionTimer = 40; // 2 seconds of ascending
        
        // Rain fire down in circle pattern
        for (int i = 0; i < 16; i++) {
            double angle = (Math.PI * 2 * i) / 16;
            double radius = 8.0D;
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;
            
            Vec3d firePos = this.getPos().add(offsetX, 0, offsetZ);
            SmallFireballEntity fireball = new SmallFireballEntity(
                this.getWorld(),
                this,
                0, -1.0D, 0
            );
            fireball.setPosition(firePos.x, this.getY() + 5, firePos.z);
            this.getWorld().spawnEntity(fireball);
        }
        
        this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 2.0F, 0.8F);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        // Phoenix requires fire resistance to damage
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity living) {
            boolean hasFireResistance = living.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
            if (!hasFireResistance) {
                // Reflect damage
                living.setOnFireFor(5);
                living.damage(this.getDamageSources().inFire(), amount * 0.5f);
                return false;
            }
        }
        
        return super.damage(source, amount);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        
        // Drop phoenix egg
        this.dropItem(Items.DRAGON_EGG);
    }

    @Override
    public boolean isFireImmune() {
        return true;
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
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BLAZE_DEATH;
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
