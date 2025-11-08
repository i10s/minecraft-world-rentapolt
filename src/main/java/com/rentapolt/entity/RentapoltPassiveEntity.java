package com.rentapolt.entity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.rentapolt.entity.config.RentapoltPassiveConfig;
import com.rentapolt.entity.config.RentapoltPassiveConfig.PassiveAbility;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class RentapoltPassiveEntity extends AnimalEntity {
    private static final RentapoltPassiveConfig DEFAULT =
            new RentapoltPassiveConfig(20.0F, 0.25D, 16.0D, PassiveAbility.STRENGTH_AURA, 0.9F, 1.8F);
    private RentapoltPassiveConfig config = DEFAULT;

    public RentapoltPassiveEntity(EntityType<? extends RentapoltPassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public RentapoltPassiveEntity configure(@NotNull RentapoltPassiveConfig config) {
        this.config = config;
        this.calculateDimensions();
        return this;
    }

    public static DefaultAttributeContainer.Builder createAttributes(RentapoltPassiveConfig config) {
        return AnimalEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, config.maxHealth())
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, config.movementSpeed())
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, config.followRange());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && this.age % 40 == 0) {
            applyAura();
        }
    }

    private void applyAura() {
        StatusEffect effect = switch (config.ability()) {
            case STRENGTH_AURA -> StatusEffects.STRENGTH;
            case RESISTANCE_AURA -> StatusEffects.RESISTANCE;
            case FIRE_IMMUNITY_AURA -> StatusEffects.FIRE_RESISTANCE;
            case SKY_GRACE -> StatusEffects.SLOW_FALLING;
        };

        List<PlayerEntity> players = this.getWorld().getEntitiesByClass(PlayerEntity.class,
                Box.of(this.getPos(), 1.0D, 1.0D, 1.0D).expand(6.0D), entity -> true);
        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(effect, 120, 0, true, true));
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.GOLDEN_CARROT) || stack.isFood();
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        RentapoltPassiveEntity child = (RentapoltPassiveEntity) this.getType().create(world);
        if (child != null) {
            child.configure(this.config);
        }
        return child;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FOX_DEATH;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (config.ability() == PassiveAbility.SKY_GRACE && !player.isSneaking()) {
            player.startRiding(this, true);
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player, hand);
    }
}
