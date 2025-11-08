package com.rentapolt.world.feature;

import com.rentapolt.RentapoltMod;
import com.rentapolt.registry.RentapoltBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;

public final class StructureBuilders {
    private StructureBuilders() {}

    public static StructureGenerator city() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            layPad(world, base, Blocks.GRAY_CONCRETE.getDefaultState(), 6);
            buildTower(world, base.up(), Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.GLASS.getDefaultState(), random.nextBetween(6, 14));
            placeLoot(world, base.add(0, 1, 0), RentapoltMod.id("chests/city_house"), random);
            addDecoration(world, base.up(), Blocks.SEA_LANTERN.getDefaultState());
            return true;
        };
    }

    public static StructureGenerator prairie() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            layPad(world, base, Blocks.OAK_PLANKS.getDefaultState(), 4);
            buildBox(world, base.up(), Blocks.OAK_PLANKS.getDefaultState(), Blocks.GLASS_PANE.getDefaultState(), 4, 4, 4);
            placeLoot(world, base.add(0, 1, 0), RentapoltMod.id("chests/prairie_home"), random);
            scatter(world, base.up(), Blocks.HAY_BLOCK.getDefaultState(), random, 6);
            return true;
        };
    }

    public static StructureGenerator mutantTower() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            layPad(world, base, Blocks.POLISHED_DEEPSLATE.getDefaultState(), 5);
            buildTower(world, base.up(), Blocks.DEEPSLATE_BRICKS.getDefaultState(), Blocks.CRYING_OBSIDIAN.getDefaultState(), random.nextBetween(8, 16));
            world.setBlockState(base.up(2), RentapoltBlocks.ENERGY_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
            placeLoot(world, base.add(0, 2, 0), RentapoltMod.id("chests/mutant_tower"), random);
            return true;
        };
    }

    public static StructureGenerator bunker() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin).down(4);
            buildBox(world, base, Blocks.IRON_BLOCK.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), 5, 4, 5);
            placeLoot(world, base.add(0, 1, 0), RentapoltMod.id("chests/bunker"), random);
            world.setBlockState(base.add(0, -1, 0), RentapoltBlocks.EXPLOSIVE_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
            return true;
        };
    }

    private static BlockPos top(ServerWorld world, BlockPos origin) {
        return world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, origin).down();
    }

    private static void layPad(ServerWorld world, BlockPos center, BlockState state, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                world.setBlockState(center.add(x, 1, z), state, Block.NOTIFY_LISTENERS);
            }
        }
    }

    private static void buildTower(ServerWorld world, BlockPos start, BlockState wall, BlockState window, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos pos = start.add(x, y, z);
                    boolean edge = Math.abs(x) == 2 || Math.abs(z) == 2;
                    BlockState state = edge ? wall : Blocks.AIR.getDefaultState();
                    if (!edge && y % 2 == 0) {
                        state = window;
                    }
                    world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    private static void buildBox(ServerWorld world, BlockPos start, BlockState wall, BlockState window,
                                 int width, int height, int depth) {
        for (int y = 0; y < height; y++) {
            for (int x = -width / 2; x <= width / 2; x++) {
                for (int z = -depth / 2; z <= depth / 2; z++) {
                    BlockPos pos = start.add(x, y, z);
                    boolean edge = Math.abs(x) == width / 2 || Math.abs(z) == depth / 2 || y == 0 || y == height - 1;
                    world.setBlockState(pos, edge ? wall : Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
        addWindows(world, start.up(), window, width, depth);
    }

    private static void addWindows(ServerWorld world, BlockPos start, BlockState window, int width, int depth) {
        for (int x = -width / 2 + 1; x <= width / 2 - 1; x++) {
            world.setBlockState(start.add(x, 1, depth / 2), window, Block.NOTIFY_LISTENERS);
            world.setBlockState(start.add(x, 1, -depth / 2), window, Block.NOTIFY_LISTENERS);
        }
    }

    private static void addDecoration(ServerWorld world, BlockPos start, BlockState block) {
        for (int x = -2; x <= 2; x += 2) {
            for (int z = -2; z <= 2; z += 2) {
                world.setBlockState(start.add(x, 0, z), block, Block.NOTIFY_LISTENERS);
            }
        }
    }

    private static void scatter(ServerWorld world, BlockPos start, BlockState block, Random random, int count) {
        for (int i = 0; i < count; i++) {
            BlockPos pos = start.add(random.nextBetween(-3, 3), 0, random.nextBetween(-3, 3));
            world.setBlockState(pos, block, Block.NOTIFY_LISTENERS);
        }
    }

    private static void placeLoot(ServerWorld world, BlockPos pos, Identifier loot, Random random) {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.NORTH), Block.NOTIFY_LISTENERS);
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof LootableContainerBlockEntity chest) {
            chest.setLootTable(loot, random.nextLong());
        }
    }
}
