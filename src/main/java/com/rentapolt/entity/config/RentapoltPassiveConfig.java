package com.rentapolt.entity.config;

public record RentapoltPassiveConfig(float maxHealth, double movementSpeed, double followRange,
                                     PassiveAbility ability, float width, float height) {
    public enum PassiveAbility {
        STRENGTH_AURA,
        RESISTANCE_AURA,
        FIRE_IMMUNITY_AURA,
        SKY_GRACE
    }
}
