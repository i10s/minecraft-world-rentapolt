package com.rentapolt.item;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum RentapoltArmorMaterials implements ArmorMaterial {
    PHOENIX("phoenix", 45, new int[]{4, 8, 7, 4}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 3.0F, 0.05F,
            () -> Ingredient.EMPTY),
    SPEED("speed", 30, new int[]{3, 6, 6, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2.0F, 0.02F,
            () -> Ingredient.EMPTY),
    FLIGHT("flight", 28, new int[]{3, 7, 6, 3}, 28, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, 0.02F,
            () -> Ingredient.EMPTY),
    INVISIBILITY("invisibility", 25, new int[]{2, 5, 5, 2}, 30, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 0.0F,
            () -> Ingredient.EMPTY),
    HEAVY("heavy", 60, new int[]{5, 9, 8, 5}, 12, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.5F, 0.3F,
            () -> Ingredient.EMPTY);

    private static final Map<ArmorItem.Type, Integer> BASE_DURABILITY = new EnumMap<>(ArmorItem.Type.class);

    static {
        BASE_DURABILITY.put(ArmorItem.Type.BOOTS, 13);
        BASE_DURABILITY.put(ArmorItem.Type.LEGGINGS, 15);
        BASE_DURABILITY.put(ArmorItem.Type.CHESTPLATE, 16);
        BASE_DURABILITY.put(ArmorItem.Type.HELMET, 11);
    }

    private final String name;
    private final int durabilityMultiplier;
    private final Map<ArmorItem.Type, Integer> protection;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    RentapoltArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability,
                            SoundEvent equipSound, float toughness, float knockbackResistance,
                            Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
        this.protection = new EnumMap<>(ArmorItem.Type.class);
        this.protection.put(ArmorItem.Type.HELMET, protectionAmounts[0]);
        this.protection.put(ArmorItem.Type.CHESTPLATE, protectionAmounts[1]);
        this.protection.put(ArmorItem.Type.LEGGINGS, protectionAmounts[2]);
        this.protection.put(ArmorItem.Type.BOOTS, protectionAmounts[3]);
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY.get(type) * durabilityMultiplier;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return protection.getOrDefault(type, 0);
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
