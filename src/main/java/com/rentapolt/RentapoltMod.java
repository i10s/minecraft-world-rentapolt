package com.rentapolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rentapolt.registry.RentapoltBlocks;
import com.rentapolt.registry.RentapoltEntities;
import com.rentapolt.registry.RentapoltItems;
import com.rentapolt.registry.RentapoltSounds;
import com.rentapolt.registry.RentapoltWorldgen;
import com.rentapolt.util.ArmorAbilityHandler;

import net.fabricmc.api.ModInitializer;
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
        LOGGER.info("Rentapolt world booted with endless chaos!");
    }
}
