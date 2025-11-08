package com.rentapolt.registry;

import com.rentapolt.RentapoltMod;
import com.rentapolt.block.CityGlowBlock;
import com.rentapolt.block.EnergyBlock;
import com.rentapolt.block.ExplosiveBlock;
import com.rentapolt.block.TeleporterBlock;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class RentapoltBlocks {
    public static final Block ENERGY_BLOCK = register("energy_block",
            new EnergyBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)
                    .luminance(15)));

    public static final Block TELEPORTER_BLOCK = register("teleporter_block",
            new TeleporterBlock(FabricBlockSettings.create()
                    .mapColor(MapColor.PURPLE)
                    .nonOpaque()
                    .noCollision()
                    .luminance(10)
                    .pistonBehavior(PistonBehavior.IGNORE)));

    public static final Block EXPLOSIVE_BLOCK = register("explosive_block",
            new ExplosiveBlock(FabricBlockSettings.copyOf(Blocks.TNT)
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .luminance(5)));

    public static final Block CITY_GLOW_BLOCK = register("city_glow_block",
            new CityGlowBlock(FabricBlockSettings.copyOf(Blocks.GLASS)
                    .strength(0.3F)
                    .luminance(12)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.GLASS)));

    public static void register() {
        // Trigger class loading
    }

    private static Block register(String name, Block block) {
        Identifier id = RentapoltMod.id(name);
        return Registry.register(Registries.BLOCK, id, block);
    }
}
