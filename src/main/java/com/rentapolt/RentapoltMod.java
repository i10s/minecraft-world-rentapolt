package com.rentapolt;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rentapolt.registry.RentapoltBlocks;
import com.rentapolt.registry.RentapoltEntities;
import com.rentapolt.registry.RentapoltItems;
import com.rentapolt.registry.RentapoltSounds;
import com.rentapolt.registry.RentapoltWorldgen;
import com.rentapolt.util.ArmorAbilityHandler;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

/**
 * Entry point for the Rentapolt mod. Wires together registries and world logic.
 */
public class RentapoltMod implements ModInitializer {
    public static final String MOD_ID = "rentapolt";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

	@Override
	public void onInitialize() {
		RentapoltSounds.register();
		RentapoltBlocks.register();
		RentapoltEntities.register();
		RentapoltItems.register();
		RentapoltWorldgen.register();
		ArmorAbilityHandler.register();
		logRegistrySnapshot();
		LOGGER.info("Rentapolt world booted with endless chaos!");
	}

	private void logRegistrySnapshot() {
		RegistryWrapper.WrapperLookup lookup = BuiltinRegistries.createWrapperLookup();
		var biomeWrapper = lookup.getWrapperOrThrow(RegistryKeys.BIOME);
		var dimWrapper = lookup.getWrapperOrThrow(RegistryKeys.DIMENSION_TYPE);
		var biomes = biomeWrapper.streamEntries()
				.map(entry -> entry.registryKey().getValue())
				.filter(id -> id.getNamespace().equals(MOD_ID))
				.map(Identifier::toString)
				.collect(Collectors.toList());
		var dimTypes = dimWrapper.streamEntries()
				.map(entry -> entry.registryKey().getValue())
				.filter(id -> id.getNamespace().equals(MOD_ID))
				.map(Identifier::toString)
				.collect(Collectors.toList());
		LOGGER.info("Biomes registered [{}]: {}", biomes.size(), String.join(", ", biomes));
		LOGGER.info("Dimension types registered [{}]: {}", dimTypes.size(), String.join(", ", dimTypes));
	}
}
