package com.rentapolt.entity.config;

public record RentapoltMobConfig(float maxHealth, float attackDamage, double movementSpeed, double followRange,
                                 SpecialAbility ability, boolean fireImmune, float width, float height) {
    public enum SpecialAbility {
        FIRE_AURA,
        LIGHTNING_STRIKE,
        TOXIC_CLOUD,
        MINI_EXPLOSION,
        SHADOW_CURSE,
        PLASMA_SHOT
    }
}
