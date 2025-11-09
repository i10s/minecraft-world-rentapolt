package com.rentapolt.world;

import com.rentapolt.RentapoltMod;
import com.rentapolt.registry.RentapoltBlocks;
import com.rentapolt.registry.RentapoltWorldgen;
import com.rentapolt.world.feature.StructureBuilders;
import com.rentapolt.world.feature.StructureGenerator;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public final class RentapoltStructureSpawner {
    private static final Set<String> GENERATED = ConcurrentHashMap.newKeySet();
    private static final StructureGenerator CITY = StructureBuilders.city();
    private static final StructureGenerator PRAIRIE = StructureBuilders.prairie();
    private static final StructureGenerator MUTANT = StructureBuilders.mutantTower();
    private static final StructureGenerator BUNKER = StructureBuilders.bunker();
    private static final StructureGenerator RUINED = StructureBuilders.ruinedBuilding();
    private static final StructureGenerator FLOATING = StructureBuilders.floatingIsland();

    private RentapoltStructureSpawner() {}

    public static void register() {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            if (world.getRegistryKey() != World.OVERWORLD) {
                return;
            }
            ChunkPos pos = chunk.getPos();
            String chunkId = world.getRegistryKey().getValue() + ":" + pos.x + ":" + pos.z;
            if (!GENERATED.add(chunkId)) {
                return;
            }
            Random random = Random.create(pos.toLong());
            int centerX = pos.getStartX() + 8;
            int centerZ = pos.getStartZ() + 8;
            BlockPos surface = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(centerX, world.getBottomY(), centerZ));
            net.minecraft.registry.entry.RegistryEntry<Biome> biomeEntry = world.getBiome(surface);
            biomeEntry.getKey().ifPresent(key -> {
                if (key.equals(RentapoltWorldgen.CITY)) {
                    generateWithChance(world, surface, random, CITY, 0.35F);
                    scatterEnergy(world, surface, random, 6);
                    // Rare floating islands near cities
                    generateWithChance(world, surface, random, FLOATING, 0.02F);
                } else if (key.equals(RentapoltWorldgen.PRAIRIE)) {
                    generateWithChance(world, surface, random, PRAIRIE, 0.25F);
                    // Very rare floating islands in prairie
                    generateWithChance(world, surface, random, FLOATING, 0.01F);
                } else if (key.equals(RentapoltWorldgen.MUTANT_ZONE)) {
                    generateWithChance(world, surface, random, MUTANT, 0.3F);
                    scatterEnergy(world, surface, random, 4);
                    // Ruined buildings common in mutant zone
                    generateWithChance(world, surface, random, RUINED, 0.4F);
                } else if (key.equals(RentapoltWorldgen.SECRET_BUNKER)) {
                    generateWithChance(world, surface, random, BUNKER, 0.2F);
                }
            });
        });
    }

    private static void generateWithChance(ServerWorld world, BlockPos origin, Random random, StructureGenerator generator, float chance) {
        if (random.nextFloat() <= chance) {
            generator.generate(world, origin, random);
        }
    }

    private static void scatterEnergy(ServerWorld world, BlockPos origin, Random random, int count) {
        BlockState energy = RentapoltBlocks.ENERGY_BLOCK.getDefaultState();
        for (int i = 0; i < count; i++) {
            BlockPos pos = origin.add(random.nextBetween(-6, 6), 0, random.nextBetween(-6, 6));
            BlockPos top = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, pos);
            world.setBlockState(top, energy, Block.NOTIFY_LISTENERS);
        }
    }
}
