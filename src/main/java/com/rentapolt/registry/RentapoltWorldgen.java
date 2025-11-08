package com.rentapolt.registry;

import com.rentapolt.RentapoltMod;

import com.rentapolt.world.RentapoltStructureSpawner;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public final class RentapoltWorldgen {
    public static final RegistryKey<Biome> CITY = RegistryKey.of(RegistryKeys.BIOME, RentapoltMod.id("city"));
    public static final RegistryKey<Biome> PRAIRIE = RegistryKey.of(RegistryKeys.BIOME, RentapoltMod.id("prairie"));
    public static final RegistryKey<Biome> MUTANT_ZONE = RegistryKey.of(RegistryKeys.BIOME, RentapoltMod.id("mutant_zone"));
    public static final RegistryKey<Biome> SECRET_BUNKER = RegistryKey.of(RegistryKeys.BIOME, RentapoltMod.id("secret_bunker"));

    private RentapoltWorldgen() {}

    public static void register() {
        RentapoltStructureSpawner.register();
        addSpawns();
    }

    private static void addSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(CITY), net.minecraft.entity.SpawnGroup.MONSTER,
                RentapoltEntities.MUTANT_CREEPER, 25, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(CITY), net.minecraft.entity.SpawnGroup.MONSTER,
                RentapoltEntities.FIRE_GOLEM, 15, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(PRAIRIE), net.minecraft.entity.SpawnGroup.CREATURE,
                RentapoltEntities.LION, 20, 2, 4);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(PRAIRIE), net.minecraft.entity.SpawnGroup.CREATURE,
                RentapoltEntities.ELEPHANT, 8, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(MUTANT_ZONE), net.minecraft.entity.SpawnGroup.MONSTER,
                RentapoltEntities.MUTANT_ZOMBIE, 30, 2, 4);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(MUTANT_ZONE), net.minecraft.entity.SpawnGroup.MONSTER,
                RentapoltEntities.PLASMA_BEAST, 15, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(MUTANT_ZONE, SECRET_BUNKER), net.minecraft.entity.SpawnGroup.MONSTER,
                RentapoltEntities.SHADOW_SERPENT, 18, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(SECRET_BUNKER), net.minecraft.entity.SpawnGroup.MONSTER,
                RentapoltEntities.MUTANT_CREEPER, 20, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(CITY, PRAIRIE), net.minecraft.entity.SpawnGroup.CREATURE,
                RentapoltEntities.PHOENIX, 6, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(CITY, PRAIRIE), net.minecraft.entity.SpawnGroup.CREATURE,
                RentapoltEntities.GRIFFIN, 4, 1, 2);
    }
}
