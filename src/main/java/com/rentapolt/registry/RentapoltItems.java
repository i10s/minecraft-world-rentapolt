package com.rentapolt.registry;

import java.util.ArrayList;
import java.util.List;

import com.rentapolt.RentapoltMod;
import com.rentapolt.registry.RentapoltBlocks;
import com.rentapolt.item.ExplosiveHammerItem;
import com.rentapolt.item.FireSwordItem;
import com.rentapolt.item.LightningBowItem;
import com.rentapolt.item.PlasmaRifleItem;
import com.rentapolt.item.RentapoltArmorMaterials;
import com.rentapolt.item.RentapoltToolMaterials;
import com.rentapolt.item.ShadowScytheItem;
import com.rentapolt.item.TeleportationDaggerItem;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RentapoltItems {
    private static final List<Item> CREATIVE_ORDER = new ArrayList<>();

    public static final Item RENTAPOLT_CORE = register("rentapolt_core", new Item(settings()));
    public static final Item PHOENIX_FEATHER = register("phoenix_feather", new Item(settings()));
    public static final Item SHADOW_ESSENCE = register("shadow_essence", new Item(settings()));

    public static final Item FIRE_SWORD = register("fire_sword",
            new FireSwordItem(RentapoltToolMaterials.RENTAPOLT_ALLOY, 6, -2.2F, settings().maxCount(1)));
    public static final Item LIGHTNING_BOW = register("lightning_bow", new LightningBowItem(settings().maxCount(1).maxDamage(768)));
    public static final Item TELEPORTATION_DAGGER = register("teleportation_dagger",
            new TeleportationDaggerItem(RentapoltToolMaterials.RENTAPOLT_ALLOY, 3, -1.8F, settings().maxCount(1)));
    public static final Item EXPLOSIVE_HAMMER = register("explosive_hammer",
            new ExplosiveHammerItem(RentapoltToolMaterials.RENTAPOLT_ALLOY, 9, -3.4F, settings().maxCount(1)));
    public static final Item PLASMA_RIFLE = register("plasma_rifle", new PlasmaRifleItem(settings().maxCount(1).maxDamage(512)));
    public static final Item SHADOW_SCYTHE = register("shadow_scythe",
            new ShadowScytheItem(RentapoltToolMaterials.RENTAPOLT_ALLOY, 7, -2.6F, settings().maxCount(1)));

    public static final Item ENERGY_BLOCK_ITEM = registerBlockItem("energy_block", RentapoltBlocks.ENERGY_BLOCK);
    public static final Item TELEPORTER_BLOCK_ITEM = registerBlockItem("teleporter_block", RentapoltBlocks.TELEPORTER_BLOCK);
    public static final Item EXPLOSIVE_BLOCK_ITEM = registerBlockItem("explosive_block", RentapoltBlocks.EXPLOSIVE_BLOCK);
    public static final Item CITY_GLOW_BLOCK_ITEM = registerBlockItem("city_glow_block", RentapoltBlocks.CITY_GLOW_BLOCK);

    public static final Item PHOENIX_HELMET = register("phoenix_helmet",
            new ArmorItem(RentapoltArmorMaterials.PHOENIX, ArmorItem.Type.HELMET, settings()));
    public static final Item PHOENIX_CHESTPLATE = register("phoenix_chestplate",
            new ArmorItem(RentapoltArmorMaterials.PHOENIX, ArmorItem.Type.CHESTPLATE, settings()));
    public static final Item PHOENIX_LEGGINGS = register("phoenix_leggings",
            new ArmorItem(RentapoltArmorMaterials.PHOENIX, ArmorItem.Type.LEGGINGS, settings()));
    public static final Item PHOENIX_BOOTS = register("phoenix_boots",
            new ArmorItem(RentapoltArmorMaterials.PHOENIX, ArmorItem.Type.BOOTS, settings()));

    public static final Item SPEED_HELMET = register("speed_helmet",
            new ArmorItem(RentapoltArmorMaterials.SPEED, ArmorItem.Type.HELMET, settings()));
    public static final Item SPEED_CHESTPLATE = register("speed_chestplate",
            new ArmorItem(RentapoltArmorMaterials.SPEED, ArmorItem.Type.CHESTPLATE, settings()));
    public static final Item SPEED_LEGGINGS = register("speed_leggings",
            new ArmorItem(RentapoltArmorMaterials.SPEED, ArmorItem.Type.LEGGINGS, settings()));
    public static final Item SPEED_BOOTS = register("speed_boots",
            new ArmorItem(RentapoltArmorMaterials.SPEED, ArmorItem.Type.BOOTS, settings()));

    public static final Item FLIGHT_HELMET = register("flight_helmet",
            new ArmorItem(RentapoltArmorMaterials.FLIGHT, ArmorItem.Type.HELMET, settings()));
    public static final Item FLIGHT_CHESTPLATE = register("flight_chestplate",
            new ArmorItem(RentapoltArmorMaterials.FLIGHT, ArmorItem.Type.CHESTPLATE, settings()));
    public static final Item FLIGHT_LEGGINGS = register("flight_leggings",
            new ArmorItem(RentapoltArmorMaterials.FLIGHT, ArmorItem.Type.LEGGINGS, settings()));
    public static final Item FLIGHT_BOOTS = register("flight_boots",
            new ArmorItem(RentapoltArmorMaterials.FLIGHT, ArmorItem.Type.BOOTS, settings()));

    public static final Item INVISIBILITY_HELMET = register("invisibility_helmet",
            new ArmorItem(RentapoltArmorMaterials.INVISIBILITY, ArmorItem.Type.HELMET, settings()));
    public static final Item INVISIBILITY_CHESTPLATE = register("invisibility_chestplate",
            new ArmorItem(RentapoltArmorMaterials.INVISIBILITY, ArmorItem.Type.CHESTPLATE, settings()));
    public static final Item INVISIBILITY_LEGGINGS = register("invisibility_leggings",
            new ArmorItem(RentapoltArmorMaterials.INVISIBILITY, ArmorItem.Type.LEGGINGS, settings()));
    public static final Item INVISIBILITY_BOOTS = register("invisibility_boots",
            new ArmorItem(RentapoltArmorMaterials.INVISIBILITY, ArmorItem.Type.BOOTS, settings()));

    public static final Item HEAVY_HELMET = register("heavy_helmet",
            new ArmorItem(RentapoltArmorMaterials.HEAVY, ArmorItem.Type.HELMET, settings()));
    public static final Item HEAVY_CHESTPLATE = register("heavy_chestplate",
            new ArmorItem(RentapoltArmorMaterials.HEAVY, ArmorItem.Type.CHESTPLATE, settings()));
    public static final Item HEAVY_LEGGINGS = register("heavy_leggings",
            new ArmorItem(RentapoltArmorMaterials.HEAVY, ArmorItem.Type.LEGGINGS, settings()));
    public static final Item HEAVY_BOOTS = register("heavy_boots",
            new ArmorItem(RentapoltArmorMaterials.HEAVY, ArmorItem.Type.BOOTS, settings()));

    public static final ArmorSet PHOENIX_SET = new ArmorSet(PHOENIX_HELMET, PHOENIX_CHESTPLATE, PHOENIX_LEGGINGS, PHOENIX_BOOTS);
    public static final ArmorSet SPEED_SET = new ArmorSet(SPEED_HELMET, SPEED_CHESTPLATE, SPEED_LEGGINGS, SPEED_BOOTS);
    public static final ArmorSet FLIGHT_SET = new ArmorSet(FLIGHT_HELMET, FLIGHT_CHESTPLATE, FLIGHT_LEGGINGS, FLIGHT_BOOTS);
    public static final ArmorSet INVISIBILITY_SET = new ArmorSet(INVISIBILITY_HELMET, INVISIBILITY_CHESTPLATE, INVISIBILITY_LEGGINGS, INVISIBILITY_BOOTS);
    public static final ArmorSet HEAVY_SET = new ArmorSet(HEAVY_HELMET, HEAVY_CHESTPLATE, HEAVY_LEGGINGS, HEAVY_BOOTS);

    public static final Item MUTANT_CREEPER_SPAWN_EGG = register("mutant_creeper_spawn_egg",
            new SpawnEggItem(RentapoltEntities.MUTANT_CREEPER, 0x228B22, 0x111111, settings()));
    public static final Item MUTANT_ZOMBIE_SPAWN_EGG = register("mutant_zombie_spawn_egg",
            new SpawnEggItem(RentapoltEntities.MUTANT_ZOMBIE, 0x669966, 0x330033, settings()));
    public static final Item FIRE_GOLEM_SPAWN_EGG = register("fire_golem_spawn_egg",
            new SpawnEggItem(RentapoltEntities.FIRE_GOLEM, 0xFF6600, 0x660000, settings()));
    public static final Item PLASMA_BEAST_SPAWN_EGG = register("plasma_beast_spawn_egg",
            new SpawnEggItem(RentapoltEntities.PLASMA_BEAST, 0x00FFFF, 0x222244, settings()));
    public static final Item SHADOW_SERPENT_SPAWN_EGG = register("shadow_serpent_spawn_egg",
            new SpawnEggItem(RentapoltEntities.SHADOW_SERPENT, 0x111133, 0x660066, settings()));
    public static final Item LION_SPAWN_EGG = register("lion_spawn_egg",
            new SpawnEggItem(RentapoltEntities.LION, 0xCC9900, 0x663300, settings()));
    public static final Item ELEPHANT_SPAWN_EGG = register("elephant_spawn_egg",
            new SpawnEggItem(RentapoltEntities.ELEPHANT, 0xAAAAAA, 0x333333, settings()));
    public static final Item PHOENIX_SPAWN_EGG = register("phoenix_spawn_egg",
            new SpawnEggItem(RentapoltEntities.PHOENIX, 0xFF0000, 0xFFFF66, settings()));
    public static final Item GRIFFIN_SPAWN_EGG = register("griffin_spawn_egg",
            new SpawnEggItem(RentapoltEntities.GRIFFIN, 0x996633, 0xFFFFFF, settings()));

    public static final ItemGroup RENTAPOLT_GROUP = Registry.register(Registries.ITEM_GROUP,
            RentapoltMod.id("rentapolt"),
            FabricItemGroup.builder().icon(() -> new ItemStack(FIRE_SWORD))
                    .displayName(Text.translatable("itemgroup.rentapolt"))
                    .entries((enabledFeatures, entries) -> CREATIVE_ORDER.forEach(entries::add))
                    .build());

    public static void register() {
        // Intentionally left blank; static initialiser handles registration.
    }

    private static Item register(String name, Item item) {
        Identifier id = RentapoltMod.id(name);
        Registry.register(Registries.ITEM, id, item);
        CREATIVE_ORDER.add(item);
        return item;
    }

    private static Item registerBlockItem(String name, Block block) {
        return register(name, new BlockItem(block, settings()));
    }

    private static Item.Settings settings() {
        return new Item.Settings();
    }

    public record ArmorSet(Item helmet, Item chestplate, Item leggings, Item boots) {}
}
