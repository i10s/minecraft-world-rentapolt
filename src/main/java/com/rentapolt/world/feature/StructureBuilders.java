package com.rentapolt.world.feature;

import com.rentapolt.RentapoltMod;
import com.rentapolt.entity.boss.AncientPhoenixEntity;
import com.rentapolt.entity.boss.MegaMutantEntity;
import com.rentapolt.entity.boss.ShadowKingEntity;
import com.rentapolt.registry.RentapoltBlocks;
import com.rentapolt.registry.RentapoltEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;

public final class StructureBuilders {
    private StructureBuilders() {}
    
    // Building types for varied interior designs
    private enum BuildingType {
        SHOP,
        RESTAURANT,
        BANK,
        HOTEL,
        OFFICE,
        APARTMENT
    }

    // Material palettes for variety
    private static final BlockState[] CITY_WALL_MATERIALS = {
        Blocks.QUARTZ_BLOCK.getDefaultState(),
        Blocks.SMOOTH_QUARTZ.getDefaultState(),
        Blocks.WHITE_CONCRETE.getDefaultState(),
        Blocks.LIGHT_GRAY_CONCRETE.getDefaultState(),
        Blocks.GRAY_CONCRETE.getDefaultState(),
        Blocks.WHITE_TERRACOTTA.getDefaultState(),
        Blocks.LIGHT_GRAY_TERRACOTTA.getDefaultState()
    };
    
    private static final BlockState[] CITY_BASE_MATERIALS = {
        Blocks.GRAY_CONCRETE.getDefaultState(),
        Blocks.BLACK_CONCRETE.getDefaultState(),
        Blocks.STONE_BRICKS.getDefaultState(),
        Blocks.POLISHED_ANDESITE.getDefaultState()
    };
    
    private static final BlockState[] CITY_WINDOW_MATERIALS = {
        Blocks.GLASS.getDefaultState(),
        Blocks.TINTED_GLASS.getDefaultState(),
        Blocks.GRAY_STAINED_GLASS.getDefaultState(),
        Blocks.LIGHT_BLUE_STAINED_GLASS.getDefaultState()
    };
    
    private static final BlockState[] CITY_DECORATIONS = {
        Blocks.SEA_LANTERN.getDefaultState(),
        Blocks.GLOWSTONE.getDefaultState(),
        Blocks.REDSTONE_LAMP.getDefaultState(),
        Blocks.SHROOMLIGHT.getDefaultState()
    };
    
    private static final BlockState[] PRAIRIE_WOODS = {
        Blocks.OAK_PLANKS.getDefaultState(),
        Blocks.SPRUCE_PLANKS.getDefaultState(),
        Blocks.BIRCH_PLANKS.getDefaultState(),
        Blocks.ACACIA_PLANKS.getDefaultState()
    };
    
    private static final BlockState[] PRAIRIE_DECORATIONS = {
        Blocks.HAY_BLOCK.getDefaultState(),
        Blocks.PUMPKIN.getDefaultState(),
        Blocks.MELON.getDefaultState(),
        Blocks.COMPOSTER.getDefaultState()
    };
    
    private static final BlockState[] TOWER_MATERIALS = {
        Blocks.DEEPSLATE_BRICKS.getDefaultState(),
        Blocks.POLISHED_DEEPSLATE.getDefaultState(),
        Blocks.DEEPSLATE_TILES.getDefaultState(),
        Blocks.BLACKSTONE.getDefaultState(),
        Blocks.POLISHED_BLACKSTONE.getDefaultState()
    };
    
    private static final BlockState[] ROAD_MATERIALS = {
        Blocks.STONE_BRICKS.getDefaultState(),
        Blocks.POLISHED_ANDESITE.getDefaultState(),
        Blocks.SMOOTH_STONE.getDefaultState(),
        Blocks.COBBLESTONE.getDefaultState()
    };
    
    private static final BlockState[] FURNITURE_BLOCKS = {
        Blocks.CRAFTING_TABLE.getDefaultState(),
        Blocks.FURNACE.getDefaultState(),
        Blocks.BOOKSHELF.getDefaultState(),
        Blocks.BARREL.getDefaultState()
    };

    public static StructureGenerator city() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            
            // Randomize materials
            BlockState baseMaterial = CITY_BASE_MATERIALS[random.nextInt(CITY_BASE_MATERIALS.length)];
            BlockState wallMaterial = CITY_WALL_MATERIALS[random.nextInt(CITY_WALL_MATERIALS.length)];
            BlockState windowMaterial = CITY_WINDOW_MATERIALS[random.nextInt(CITY_WINDOW_MATERIALS.length)];
            BlockState decoration = CITY_DECORATIONS[random.nextInt(CITY_DECORATIONS.length)];
            
            // EPIC NYC-Style - GIANT SKYSCRAPERS!
            int height;
            float heightRoll = random.nextFloat();
            if (heightRoll < 0.10f) {
                // 10% - MEGA SKYSCRAPERS (One World Trade Center style) - UP TO 80 BLOCKS!
                height = random.nextBetween(60, 80);
            } else if (heightRoll < 0.25f) {
                // 15% - Super skyscrapers (Empire State style)
                height = random.nextBetween(45, 60);
            } else if (heightRoll < 0.40f) {
                // 15% - Large skyscrapers
                height = random.nextBetween(35, 45);
            } else if (heightRoll < 0.60f) {
                // 20% - Tall buildings
                height = random.nextBetween(25, 35);
            } else if (heightRoll < 0.80f) {
                // 20% - Medium buildings
                height = random.nextBetween(15, 25);
            } else {
                // 20% - Low buildings (shops)
                height = random.nextBetween(5, 15);
            }
            
            // Wider base for taller buildings
            int baseWidth = 6;
            if (height > 60) {
                baseWidth = 12; // MEGA WIDTH for super towers
            } else if (height > 45) {
                baseWidth = 10;
            } else if (height > 30) {
                baseWidth = 8;
            } else if (height > 20) {
                baseWidth = 7;
            }
            
            // Wide NYC-style streets
            BlockState roadMaterial = ROAD_MATERIALS[random.nextInt(ROAD_MATERIALS.length)];
            layRoad(world, base, roadMaterial, baseWidth + 4);
            
            // Building pad and structure
            layPad(world, base, baseMaterial, baseWidth);
            
            // Determine building type based on height
            BuildingType buildingType;
            if (height < 10) {
                // Short buildings are shops/restaurants
                buildingType = random.nextBoolean() ? BuildingType.SHOP : BuildingType.RESTAURANT;
            } else if (height < 25) {
                // Medium buildings are banks/hotels
                buildingType = random.nextBoolean() ? BuildingType.BANK : BuildingType.HOTEL;
            } else if (height < 45) {
                // Tall buildings are offices
                buildingType = BuildingType.OFFICE;
            } else {
                // Super tall are luxury apartments
                buildingType = BuildingType.APARTMENT;
            }
            
            buildTowerWithInterior(world, base.up(), wallMaterial, windowMaterial, height, random, buildingType);
            placeLoot(world, base.add(0, 1, 0), RentapoltMod.id("chests/city_house"), random);
            
            // EPIC ROOFTOP FEATURES!
            
            // Giant antennas on mega skyscrapers
            if (height > 50) {
                if (random.nextFloat() < 0.9f) {
                    int spireHeight = random.nextBetween(8, 15);
                    addEpicSpire(world, base.up(height), decoration, spireHeight, random);
                }
            } else if (height > 30) {
                if (random.nextFloat() < 0.7f) {
                    addSkyscraperSpire(world, base.up(height), decoration, random.nextBetween(4, 10));
                }
            }
            
            // Helipads on tall buildings
            if (height > 25 && random.nextFloat() < 0.5f) {
                addHelipad(world, base.up(height), random);
            }
            
            // Rooftop pools on luxury buildings
            if (height > 40 && random.nextFloat() < 0.4f) {
                addRooftopPool(world, base.up(height), random);
            }
            
            // Colorful LED lights on mega buildings
            if (height > 35) {
                addColorfulLights(world, base, height, baseWidth, random);
            }
            
            // Decoraciones base
            addDecoration(world, base.up(), decoration);
            if (random.nextFloat() < 0.6f) {
                addFlag(world, base.up(height), random);
            }
            
            // NYC-style street furniture (MORE DECORATION)
            if (random.nextFloat() < 0.9f) {
                // Street lamps (very common)
                addStreetLamp(world, base.add(baseWidth + 3, 1, 0), decoration);
                addStreetLamp(world, base.add(-(baseWidth + 3), 1, 0), decoration);
                addStreetLamp(world, base.add(0, 1, baseWidth + 3), decoration);
                addStreetLamp(world, base.add(0, 1, -(baseWidth + 3)), decoration);
            }
            
            // Traffic lights at intersections
            if (random.nextFloat() < 0.7f) {
                addTrafficLight(world, base.add(baseWidth + 4, 1, baseWidth + 4), random);
            }
            
            // Crosswalks
            if (random.nextFloat() < 0.8f) {
                addCrosswalk(world, base.add(baseWidth + 2, 1, 0), Direction.NORTH, random);
                addCrosswalk(world, base.add(0, 1, baseWidth + 2), Direction.EAST, random);
            }
            
            // Fire hydrants
            if (random.nextFloat() < 0.5f) {
                world.setBlockState(base.add(baseWidth + 2, 1, 2), Blocks.RED_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(baseWidth + 2, 2, 2), Blocks.RED_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            
            // Benches and trash cans
            if (random.nextFloat() < 0.4f) {
                world.setBlockState(base.add(baseWidth + 2, 1, -2), Blocks.OAK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(baseWidth + 2, 1, -3), Blocks.CAULDRON.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            
            // Mailbox (blue concrete)
            if (random.nextFloat() < 0.3f) {
                world.setBlockState(base.add(baseWidth + 2, 1, 4), Blocks.BLUE_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(baseWidth + 2, 2, 4), Blocks.BLUE_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            
            // Subway entrance (rare but cool!)
            if (random.nextFloat() < 0.1f && height > 20) {
                addSubwayEntrance(world, base.add(baseWidth + 5, 1, 0), random);
            }
            
            // Parking meters
            if (random.nextFloat() < 0.5f) {
                world.setBlockState(base.add(-(baseWidth + 2), 1, 3), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(-(baseWidth + 2), 2, 3), Blocks.STONE_BUTTON.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            
            // Urban trees on sidewalks
            if (random.nextFloat() < 0.3f) {
                addUrbanTree(world, base.add(baseWidth + 2, 1, 0), random);
            }
            
            return true;
        };
    }
    
    // Epic antenna with blinking lights
    private static void addEpicSpire(ServerWorld world, BlockPos base, BlockState material, int height, Random random) {
        // Main iron tower
        for (int y = 0; y < height; y++) {
            BlockPos pos = base.up(y);
            world.setBlockState(pos, Blocks.IRON_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
            
            // Platforms every 3 blocks
            if (y % 3 == 0 && y > 0) {
                world.setBlockState(pos.north(), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(pos.south(), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(pos.east(), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(pos.west(), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
        
        // Light ball at the top (beacon style)
        BlockPos top = base.up(height);
        world.setBlockState(top, Blocks.BEACON.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.up(), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Red warning lights around
        world.setBlockState(top.north(), Blocks.REDSTONE_LAMP.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.south(), Blocks.REDSTONE_LAMP.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.east(), Blocks.REDSTONE_LAMP.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.west(), Blocks.REDSTONE_LAMP.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    // Rooftop pool
    private static void addRooftopPool(ServerWorld world, BlockPos base, Random random) {
        // 4x4 pool
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos pos = base.add(x, 0, z);
                if (x == -2 || x == 2 || z == -2 || z == 2) {
                    // Pool edge
                    world.setBlockState(pos, Blocks.QUARTZ_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
                } else {
                    // Water
                    world.setBlockState(pos, Blocks.WATER.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
        
        // Lounge chairs
        world.setBlockState(base.add(3, 0, 0), Blocks.CYAN_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(3, 0, 1), Blocks.CYAN_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Umbrella
        world.setBlockState(base.add(-3, 0, 0), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-3, 1, 0), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-3, 2, 0), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-3, 2, 1), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-3, 2, -1), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-4, 2, 0), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-2, 2, 0), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    // Colorful LED lights on facades
    private static void addColorfulLights(ServerWorld world, BlockPos base, int height, int width, Random random) {
        BlockState[] colorfulLights = {
            Blocks.SEA_LANTERN.getDefaultState(),
            Blocks.GLOWSTONE.getDefaultState(),
            Blocks.REDSTONE_LAMP.getDefaultState(),
            Blocks.SHROOMLIGHT.getDefaultState(),
            RentapoltBlocks.ENERGY_BLOCK.getDefaultState()
        };
        
        // Vertical lights every 5 blocks
        for (int y = 5; y < height; y += 5) {
            BlockState light = colorfulLights[random.nextInt(colorfulLights.length)];
            world.setBlockState(base.add(width, y, 0), light, Block.NOTIFY_LISTENERS);
            world.setBlockState(base.add(-width, y, 0), light, Block.NOTIFY_LISTENERS);
            world.setBlockState(base.add(0, y, width), light, Block.NOTIFY_LISTENERS);
            world.setBlockState(base.add(0, y, -width), light, Block.NOTIFY_LISTENERS);
        }
        
        // Light line at the top
        for (int i = -width; i <= width; i++) {
            if (random.nextFloat() < 0.3f) {
                BlockState light = colorfulLights[random.nextInt(colorfulLights.length)];
                world.setBlockState(base.add(i, height - 1, width), light, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(i, height - 1, -width), light, Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    // Decorative urban tree
    private static void addUrbanTree(ServerWorld world, BlockPos base, Random random) {
        // Planter
        world.setBlockState(base, Blocks.STONE_BRICKS.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.north(), Blocks.STONE_BRICK_WALL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.south(), Blocks.STONE_BRICK_WALL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.east(), Blocks.STONE_BRICK_WALL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.west(), Blocks.STONE_BRICK_WALL.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Small trunk
        world.setBlockState(base.up(), Blocks.OAK_LOG.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.up(2), Blocks.OAK_LOG.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Leaves
        BlockPos top = base.up(3);
        world.setBlockState(top, Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.north(), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.south(), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.east(), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.west(), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.up(), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    // Helper method for skyscraper spires
    private static void addSkyscraperSpire(ServerWorld world, BlockPos base, BlockState material, int height) {
        for (int y = 0; y < height; y++) {
            BlockPos pos = base.up(y);
            world.setBlockState(pos, Blocks.IRON_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
            // Blinking light on top
            if (y == height - 1) {
                world.setBlockState(pos.up(), Blocks.REDSTONE_LAMP.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    // Helper method for helipads
    private static void addHelipad(ServerWorld world, BlockPos base, Random random) {
        BlockState helipadMaterial = Blocks.LIGHT_GRAY_CONCRETE.getDefaultState();
        BlockState yellowLine = Blocks.YELLOW_CONCRETE.getDefaultState();
        
        // 7x7 helipad platform
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                world.setBlockState(base.add(x, 0, z), helipadMaterial, Block.NOTIFY_LISTENERS);
            }
        }
        
        // Yellow "H" marker
        for (int i = -2; i <= 2; i++) {
            world.setBlockState(base.add(-1, 0, i), yellowLine, Block.NOTIFY_LISTENERS);
            world.setBlockState(base.add(1, 0, i), yellowLine, Block.NOTIFY_LISTENERS);
            world.setBlockState(base.add(i, 0, 0), yellowLine, Block.NOTIFY_LISTENERS);
        }
        
        // Landing lights
        world.setBlockState(base.add(-3, 0, -3), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(3, 0, -3), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(-3, 0, 3), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(3, 0, 3), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
    }

    public static StructureGenerator prairie() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            
            // Randomize wood type
            BlockState woodMaterial = PRAIRIE_WOODS[random.nextInt(PRAIRIE_WOODS.length)];
            BlockState decoration = PRAIRIE_DECORATIONS[random.nextInt(PRAIRIE_DECORATIONS.length)];
            
            // Varied house sizes
            int width = random.nextBetween(4, 6);
            int height = random.nextBetween(3, 5);
            int depth = random.nextBetween(4, 6);
            
            layPad(world, base, woodMaterial, Math.max(width, depth) / 2 + 1);
            buildBox(world, base.up(), woodMaterial, Blocks.GLASS_PANE.getDefaultState(), width, height, depth);
            placeLoot(world, base.add(0, 1, 0), RentapoltMod.id("chests/prairie_home"), random);
            
            // More decorative scatter
            scatter(world, base.up(), decoration, random, random.nextBetween(4, 8));
            if (random.nextFloat() < 0.4f) {
                addFence(world, base.up(), width, depth, Blocks.OAK_FENCE.getDefaultState());
            }
            
            return true;
        };
    }

    public static StructureGenerator mutantTower() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            
            // Randomize tower materials
            BlockState towerMaterial = TOWER_MATERIALS[random.nextInt(TOWER_MATERIALS.length)];
            BlockState baseMaterial = Blocks.POLISHED_DEEPSLATE.getDefaultState();
            
            // Taller, more imposing towers
            int height = random.nextBetween(12, 24);
            
            layPad(world, base, baseMaterial, 5);
            buildTower(world, base.up(), towerMaterial, Blocks.CRYING_OBSIDIAN.getDefaultState(), height);
            world.setBlockState(base.up(2), RentapoltBlocks.ENERGY_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
            placeLoot(world, base.add(0, 2, 0), RentapoltMod.id("chests/mutant_tower"), random);
            
            // Add spikes/decorations
            addSpikes(world, base.up(height), random);
            
            // Spawn Mega Mutant boss at top (20% chance for rare boss encounter)
            if (random.nextFloat() < 0.2f) {
                BlockPos bossSpawn = base.up(height + 2);
                MegaMutantEntity boss = new MegaMutantEntity(RentapoltEntities.MEGA_MUTANT, world);
                boss.refreshPositionAndAngles(bossSpawn.getX() + 0.5, bossSpawn.getY(), bossSpawn.getZ() + 0.5, 0.0F, 0.0F);
                boss.initialize(world, world.getLocalDifficulty(bossSpawn), SpawnReason.STRUCTURE, null, null);
                world.spawnEntity(boss);
            }
            
            return true;
        };
    }

    public static StructureGenerator bunker() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin).down(4);
            
            // Main bunker room
            buildBox(world, base, Blocks.IRON_BLOCK.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), 5, 4, 5);
            placeLoot(world, base.add(0, 1, 0), RentapoltMod.id("chests/bunker"), random);
            world.setBlockState(base.add(0, -1, 0), RentapoltBlocks.EXPLOSIVE_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
            
            // Add tunnels (60% chance)
            if (random.nextFloat() < 0.6f) {
                Direction tunnelDir = Direction.Type.HORIZONTAL.stream()
                    .skip(random.nextInt(4))
                    .findFirst()
                    .orElse(Direction.NORTH);
                buildTunnel(world, base, tunnelDir, random.nextBetween(8, 16), random);
            }
            
            // Secret room (40% chance)
            if (random.nextFloat() < 0.4f) {
                BlockPos secretRoom = base.add(0, -6, 10);
                buildBox(world, secretRoom, Blocks.IRON_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 4, 3, 4);
                placeLoot(world, secretRoom.add(0, 1, 0), RentapoltMod.id("chests/bunker"), random);
                // Tunnel to secret room
                buildTunnel(world, base.add(0, -2, 3), Direction.SOUTH, 8, random);
                
                // Spawn Shadow King in secret room (50% chance if secret room exists)
                if (random.nextFloat() < 0.5f) {
                    BlockPos bossSpawn = secretRoom.add(0, 1, 0);
                    ShadowKingEntity boss = new ShadowKingEntity(RentapoltEntities.SHADOW_KING, world);
                    boss.refreshPositionAndAngles(bossSpawn.getX() + 0.5, bossSpawn.getY(), bossSpawn.getZ() + 0.5, 0.0F, 0.0F);
                    boss.initialize(world, world.getLocalDifficulty(bossSpawn), SpawnReason.STRUCTURE, null, null);
                    world.spawnEntity(boss);
                }
            }
            
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
    
    // New decoration helpers
    private static void addFlag(ServerWorld world, BlockPos top, Random random) {
        BlockState[] banners = {
            Blocks.WHITE_BANNER.getDefaultState(),
            Blocks.RED_BANNER.getDefaultState(),
            Blocks.BLUE_BANNER.getDefaultState(),
            Blocks.YELLOW_BANNER.getDefaultState()
        };
        
        world.setBlockState(top, Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(top.up(), banners[random.nextInt(banners.length)], Block.NOTIFY_LISTENERS);
    }
    
    private static void addFence(ServerWorld world, BlockPos start, int width, int depth, BlockState fence) {
        for (int x = -width / 2 - 1; x <= width / 2 + 1; x++) {
            world.setBlockState(start.add(x, 0, -depth / 2 - 1), fence, Block.NOTIFY_LISTENERS);
            world.setBlockState(start.add(x, 0, depth / 2 + 1), fence, Block.NOTIFY_LISTENERS);
        }
        for (int z = -depth / 2 - 1; z <= depth / 2 + 1; z++) {
            world.setBlockState(start.add(-width / 2 - 1, 0, z), fence, Block.NOTIFY_LISTENERS);
            world.setBlockState(start.add(width / 2 + 1, 0, z), fence, Block.NOTIFY_LISTENERS);
        }
    }
    
    private static void addSpikes(ServerWorld world, BlockPos top, Random random) {
        BlockState spike = Blocks.POINTED_DRIPSTONE.getDefaultState();
        int spikeCount = random.nextBetween(3, 6);
        
        for (int i = 0; i < spikeCount; i++) {
            int x = random.nextBetween(-2, 2);
            int z = random.nextBetween(-2, 2);
            world.setBlockState(top.add(x, 0, z), Blocks.OBSIDIAN.getDefaultState(), Block.NOTIFY_LISTENERS);
            world.setBlockState(top.add(x, 1, z), spike, Block.NOTIFY_LISTENERS);
        }
    }
    
    // New helper methods for enhanced structures
    
    private static void layRoad(ServerWorld world, BlockPos center, BlockState roadMaterial, int radius) {
        // Create cross-shaped roads
        for (int i = -radius; i <= radius; i++) {
            world.setBlockState(center.add(i, 1, 0), roadMaterial, Block.NOTIFY_LISTENERS);
            world.setBlockState(center.add(0, 1, i), roadMaterial, Block.NOTIFY_LISTENERS);
        }
    }
    
    private static void buildTowerWithInterior(ServerWorld world, BlockPos start, BlockState wall, 
                                               BlockState window, int height, Random random, BuildingType buildingType) {
        // Determine building size based on height
        int width = 6;
        if (height > 60) width = 12;
        else if (height > 45) width = 10;
        else if (height > 30) width = 8;
        else if (height > 20) width = 7;
        
        int halfWidth = width / 2;
        
        // Floor material
        BlockState floorMaterial = Blocks.POLISHED_ANDESITE.getDefaultState();
        BlockState stairBlock = Blocks.STONE_BRICK_STAIRS.getDefaultState();
        
        // Build each floor (3 blocks per floor)
        for (int floor = 0; floor < height / 3; floor++) {
            int floorY = floor * 3;
            
            // Floor slab
            for (int x = -halfWidth; x <= halfWidth; x++) {
                for (int z = -halfWidth; z <= halfWidth; z++) {
                    world.setBlockState(start.add(x, floorY, z), floorMaterial, Block.NOTIFY_LISTENERS);
                }
            }
            
            // Walls with windows
            for (int y = 1; y <= 3; y++) {
                for (int x = -halfWidth; x <= halfWidth; x++) {
                    for (int z = -halfWidth; z <= halfWidth; z++) {
                        BlockPos pos = start.add(x, floorY + y, z);
                        boolean isEdge = Math.abs(x) == halfWidth || Math.abs(z) == halfWidth;
                        
                        if (isEdge) {
                            // Windows on floor 2 (y=2) every 2 blocks
                            if (y == 2 && ((x + z) % 2 == 0)) {
                                world.setBlockState(pos, window, Block.NOTIFY_LISTENERS);
                            } else {
                                world.setBlockState(pos, wall, Block.NOTIFY_LISTENERS);
                            }
                        } else {
                            // Interior is air
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
            
            // Add entrance door on ground floor
            if (floor == 0) {
                BlockPos doorPos = start.add(halfWidth, 1, 0);
                world.setBlockState(doorPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(doorPos.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                // Glass door frame
                world.setBlockState(doorPos.add(-1, 0, 0), Blocks.GLASS_PANE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(doorPos.add(1, 0, 0), Blocks.GLASS_PANE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(doorPos.add(-1, 1, 0), Blocks.GLASS_PANE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(doorPos.add(1, 1, 0), Blocks.GLASS_PANE.getDefaultState(), Block.NOTIFY_LISTENERS);
                
                // Awning/sign based on building type
                BlockPos signPos = doorPos.up(2);
                BlockState awningColor = getAwningColor(buildingType);
                world.setBlockState(signPos.add(-1, 0, 0), awningColor, Block.NOTIFY_LISTENERS);
                world.setBlockState(signPos, awningColor, Block.NOTIFY_LISTENERS);
                world.setBlockState(signPos.add(1, 0, 0), awningColor, Block.NOTIFY_LISTENERS);
            }
            
            // Interior details - furnish based on building type
            if (floor == 0) {
                // Ground floor gets special treatment
                furnishGroundFloor(world, start.add(0, floorY + 1, 0), halfWidth, buildingType, random);
            } else if (floor > 0 && random.nextFloat() < 0.6f) {
                // Upper floors
                furnishFloor(world, start.add(0, floorY + 1, 0), halfWidth, buildingType, random);
            }
            
            // Central staircase connecting floors
            if (floor < height / 3 - 1) {
                BlockPos stairBase = start.add(0, floorY + 1, 0);
                // Spiral staircase
                world.setBlockState(stairBase, stairBlock, Block.NOTIFY_LISTENERS);
                world.setBlockState(stairBase.add(1, 1, 0), stairBlock, Block.NOTIFY_LISTENERS);
                world.setBlockState(stairBase.add(1, 2, 1), stairBlock, Block.NOTIFY_LISTENERS);
                world.setBlockState(stairBase.add(0, 3, 1), stairBlock, Block.NOTIFY_LISTENERS);
                
                // Stair supports
                world.setBlockState(stairBase.add(-1, 0, -1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(stairBase.add(-1, 1, -1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(stairBase.add(-1, 2, -1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            
            // Hallway lights
            if (floor > 0) {
                world.setBlockState(start.add(-halfWidth + 2, floorY + 2, 0), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(start.add(halfWidth - 2, floorY + 2, 0), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
        
        // Elevator shaft for mega buildings (>40 blocks)
        if (height > 40) {
            int elevatorX = -halfWidth + 1;
            int elevatorZ = -halfWidth + 1;
            
            for (int y = 0; y < height; y++) {
                // 2x2 elevator shaft
                world.setBlockState(start.add(elevatorX, y, elevatorZ), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(start.add(elevatorX + 1, y, elevatorZ), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(start.add(elevatorX, y, elevatorZ + 1), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(start.add(elevatorX + 1, y, elevatorZ + 1), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                
                // Elevator platform every 12 blocks
                if (y % 12 == 0) {
                    world.setBlockState(start.add(elevatorX, y, elevatorZ), Blocks.SMOOTH_STONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(start.add(elevatorX + 1, y, elevatorZ), Blocks.SMOOTH_STONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(start.add(elevatorX, y, elevatorZ + 1), Blocks.SMOOTH_STONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(start.add(elevatorX + 1, y, elevatorZ + 1), Blocks.SMOOTH_STONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }
    
    private static BlockState getAwningColor(BuildingType type) {
        return switch (type) {
            case SHOP -> Blocks.CYAN_WOOL.getDefaultState();
            case RESTAURANT -> Blocks.RED_WOOL.getDefaultState();
            case BANK -> Blocks.YELLOW_WOOL.getDefaultState();
            case HOTEL -> Blocks.PURPLE_WOOL.getDefaultState();
            case OFFICE -> Blocks.BLUE_WOOL.getDefaultState();
            case APARTMENT -> Blocks.GREEN_WOOL.getDefaultState();
        };
    }
    
    private static void furnishGroundFloor(ServerWorld world, BlockPos center, int halfWidth, BuildingType type, Random random) {
        switch (type) {
            case SHOP -> {
                // Shop counters and display cases
                for (int x = -halfWidth + 2; x <= halfWidth - 2; x += 2) {
                    world.setBlockState(center.add(x, 0, -halfWidth + 2), Blocks.SPRUCE_PLANKS.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(center.add(x, 1, -halfWidth + 2), Blocks.CHEST.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                // Mannequin (armor stand represented by fence)
                world.setBlockState(center.add(2, 0, 2), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            case RESTAURANT -> {
                // Tables and chairs
                for (int i = 0; i < 4; i++) {
                    int x = random.nextBetween(-halfWidth + 2, halfWidth - 2);
                    int z = random.nextBetween(-halfWidth + 2, halfWidth - 2);
                    world.setBlockState(center.add(x, 0, z), Blocks.OAK_PLANKS.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(center.add(x + 1, 0, z), Blocks.OAK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(center.add(x - 1, 0, z), Blocks.OAK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                // Kitchen area
                world.setBlockState(center.add(-halfWidth + 1, 0, -halfWidth + 1), Blocks.FURNACE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(center.add(-halfWidth + 2, 0, -halfWidth + 1), Blocks.CRAFTING_TABLE.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            case BANK -> {
                // Teller counters
                for (int z = -1; z <= 1; z++) {
                    world.setBlockState(center.add(-halfWidth + 2, 0, z), Blocks.IRON_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(center.add(-halfWidth + 2, 1, z), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                // Vault (iron door)
                world.setBlockState(center.add(-halfWidth + 1, 0, -halfWidth + 1), Blocks.IRON_DOOR.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(center.add(-halfWidth + 1, 1, -halfWidth + 1), Blocks.IRON_DOOR.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            case HOTEL -> {
                // Reception desk
                world.setBlockState(center.add(0, 0, -halfWidth + 2), Blocks.DARK_OAK_PLANKS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(center.add(1, 0, -halfWidth + 2), Blocks.DARK_OAK_PLANKS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(center.add(-1, 0, -halfWidth + 2), Blocks.DARK_OAK_PLANKS.getDefaultState(), Block.NOTIFY_LISTENERS);
                // Lobby seating
                world.setBlockState(center.add(2, 0, 2), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(center.add(-2, 0, 2), Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            default -> {
                // Office or apartment lobby
                world.setBlockState(center.add(0, 0, -halfWidth + 2), Blocks.BOOKSHELF.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    private static void furnishFloor(ServerWorld world, BlockPos center, int halfWidth, BuildingType type, Random random) {
        int furnitureCount = random.nextBetween(2, 5);
        
        for (int i = 0; i < furnitureCount; i++) {
            int fx = random.nextBetween(-halfWidth + 1, halfWidth - 1);
            int fz = random.nextBetween(-halfWidth + 1, halfWidth - 1);
            BlockPos furniturePos = center.add(fx, 0, fz);
            
            switch (type) {
                case OFFICE -> {
                    // Desks and computers
                    if (random.nextBoolean()) {
                        world.setBlockState(furniturePos, Blocks.CRAFTING_TABLE.getDefaultState(), Block.NOTIFY_LISTENERS);
                        world.setBlockState(furniturePos.add(1, 0, 0), Blocks.OAK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else {
                        world.setBlockState(furniturePos, Blocks.BOOKSHELF.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
                case APARTMENT -> {
                    // Apartment furniture
                    float roll = random.nextFloat();
                    if (roll < 0.3f) {
                        // Bed
                        world.setBlockState(furniturePos, Blocks.RED_BED.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else if (roll < 0.6f) {
                        // Kitchen
                        world.setBlockState(furniturePos, Blocks.FURNACE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else {
                        // Couch
                        world.setBlockState(furniturePos, Blocks.BLUE_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
                        world.setBlockState(furniturePos.add(1, 0, 0), Blocks.BLUE_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
                case HOTEL -> {
                    // Hotel rooms with beds
                    if (random.nextFloat() < 0.7f) {
                        world.setBlockState(furniturePos, Blocks.WHITE_BED.getDefaultState(), Block.NOTIFY_LISTENERS);
                        world.setBlockState(furniturePos.add(1, 0, 0), Blocks.CRAFTING_TABLE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
                default -> {
                    // Generic office furniture
                    world.setBlockState(furniturePos, FURNITURE_BLOCKS[random.nextInt(FURNITURE_BLOCKS.length)], Block.NOTIFY_LISTENERS);
                }
            }
        }
    }
    
    private static void addStreetLamp(ServerWorld world, BlockPos base, BlockState lightBlock) {
        // Pole
        for (int i = 0; i < 4; i++) {
            world.setBlockState(base.up(i), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
        }
        // Light on top
        world.setBlockState(base.up(4), lightBlock, Block.NOTIFY_LISTENERS);
    }
    
    private static void addTrafficLight(ServerWorld world, BlockPos base, Random random) {
        // Pole
        for (int i = 0; i < 5; i++) {
            world.setBlockState(base.up(i), Blocks.DARK_OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        }
        
        // Light housing (horizontal arm)
        BlockPos lightPos = base.up(4);
        world.setBlockState(lightPos.north(), Blocks.BLACK_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(lightPos.north(2), Blocks.BLACK_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Three lights (red, yellow, green)
        world.setBlockState(lightPos.north().up(), Blocks.RED_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(lightPos.north(), Blocks.YELLOW_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(lightPos.north().down(), Blocks.LIME_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    private static void addCrosswalk(ServerWorld world, BlockPos base, Direction direction, Random random) {
        // White stripes across the road
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) { // Striped pattern
                if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                    for (int x = -1; x <= 1; x++) {
                        world.setBlockState(base.add(x, 0, i), Blocks.WHITE_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                } else {
                    for (int z = -1; z <= 1; z++) {
                        world.setBlockState(base.add(i, 0, z), Blocks.WHITE_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }
    
    private static void addSubwayEntrance(ServerWorld world, BlockPos base, Random random) {
        // Entrance structure (iconic NYC subway entrance)
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                // Green roof
                if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                    world.setBlockState(base.add(x, 3, z), Blocks.GREEN_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
                // Glass walls
                if ((Math.abs(x) == 2 || Math.abs(z) == 2) && !(x == 0 && z == 2)) {
                    world.setBlockState(base.add(x, 1, z), Blocks.GLASS_PANE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(base.add(x, 2, z), Blocks.GLASS_PANE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
        
        // Subway sign
        world.setBlockState(base.add(0, 2, -2), Blocks.GREEN_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(base.add(0, 2, -3), Blocks.WHITE_CONCRETE.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Stairs going down
        BlockPos stairStart = base.add(0, 0, 0);
        for (int i = 0; i < 8; i++) {
            world.setBlockState(stairStart.add(0, -i, i), Blocks.STONE_BRICK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
            // Side walls
            world.setBlockState(stairStart.add(-1, -i, i), Blocks.STONE_BRICKS.getDefaultState(), Block.NOTIFY_LISTENERS);
            world.setBlockState(stairStart.add(1, -i, i), Blocks.STONE_BRICKS.getDefaultState(), Block.NOTIFY_LISTENERS);
        }
        
        // Underground platform area
        BlockPos platform = stairStart.down(8);
        for (int x = -3; x <= 3; x++) {
            for (int z = 0; z <= 10; z++) {
                for (int y = 0; y < 4; y++) {
                    BlockPos pos = platform.add(x, y, z);
                    if (y == 0) {
                        world.setBlockState(pos, Blocks.SMOOTH_STONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else if (Math.abs(x) == 3) {
                        world.setBlockState(pos, Blocks.STONE_BRICKS.getDefaultState(), Block.NOTIFY_LISTENERS);
                    } else {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
                
                // Platform lights every 3 blocks
                if (z % 3 == 0) {
                    world.setBlockState(platform.add(0, 3, z), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
        
        // Subway tracks
        for (int z = 0; z <= 10; z++) {
            world.setBlockState(platform.add(-2, 0, z), Blocks.RAIL.getDefaultState(), Block.NOTIFY_LISTENERS);
            world.setBlockState(platform.add(2, 0, z), Blocks.RAIL.getDefaultState(), Block.NOTIFY_LISTENERS);
        }
    }
    
    private static void buildTunnel(ServerWorld world, BlockPos start, Direction direction, int length, Random random) {
        BlockState tunnelWall = Blocks.STONE_BRICKS.getDefaultState();
        BlockState tunnelFloor = Blocks.POLISHED_ANDESITE.getDefaultState();
        
        for (int i = 0; i < length; i++) {
            BlockPos pos = start.offset(direction, i);
            
            // 3x3 tunnel
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy < 3; dy++) {
                    BlockPos tunnelPos = pos.add(
                        direction.getAxis() == Direction.Axis.Z ? dx : 0,
                        dy,
                        direction.getAxis() == Direction.Axis.X ? dx : 0
                    );
                    
                    if (dy == 0) {
                        world.setBlockState(tunnelPos, tunnelFloor, Block.NOTIFY_LISTENERS);
                    } else if (Math.abs(dx) == 1) {
                        world.setBlockState(tunnelPos, tunnelWall, Block.NOTIFY_LISTENERS);
                    } else {
                        world.setBlockState(tunnelPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
            }
            
            // Add torches every 4 blocks
            if (i % 4 == 0) {
                world.setBlockState(pos.up(2), Blocks.TORCH.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    // New structure generators
    
    public static StructureGenerator ruinedBuilding() {
        return (world, origin, random) -> {
            BlockPos base = top(world, origin);
            
            // Damaged/ruined materials
            BlockState ruinedWall = random.nextBoolean() 
                ? Blocks.CRACKED_STONE_BRICKS.getDefaultState()
                : Blocks.MOSSY_STONE_BRICKS.getDefaultState();
            
            int height = random.nextBetween(3, 8); // Shorter, damaged buildings
            
            // Crater around building
            for (int x = -8; x <= 8; x++) {
                for (int z = -8; z <= 8; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance < 6 && random.nextFloat() < 0.7f) {
                        int depth = (int)(3 - distance / 2);
                        for (int d = 0; d < depth; d++) {
                            world.setBlockState(base.add(x, -d, z), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
            
            // Partial building with missing blocks
            for (int y = 0; y < height; y++) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        if (random.nextFloat() > 0.3f) { // 70% chance block exists
                            BlockPos pos = base.add(x, y, z);
                            boolean edge = Math.abs(x) == 2 || Math.abs(z) == 2;
                            if (edge) {
                                world.setBlockState(pos, ruinedWall, Block.NOTIFY_LISTENERS);
                            }
                        }
                    }
                }
            }
            
            // Scattered debris
            for (int i = 0; i < 10; i++) {
                BlockPos debrisPos = base.add(
                    random.nextBetween(-5, 5), 
                    0, 
                    random.nextBetween(-5, 5)
                );
                world.setBlockState(debrisPos, ruinedWall, Block.NOTIFY_LISTENERS);
            }
            
            return true;
        };
    }
    
    public static StructureGenerator floatingIsland() {
        return (world, origin, random) -> {
            BlockPos base = origin.up(40 + random.nextBetween(0, 20)); // Float high in sky
            
            // Build island base
            BlockState islandMaterial = Blocks.GRASS_BLOCK.getDefaultState();
            BlockState islandCore = Blocks.STONE.getDefaultState();
            
            int radius = random.nextBetween(6, 10);
            
            // Spherical island shape
            for (int x = -radius; x <= radius; x++) {
                for (int y = -3; y <= 2; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        double distance = Math.sqrt(x * x + y * y * 2 + z * z);
                        if (distance < radius) {
                            BlockPos pos = base.add(x, y, z);
                            if (y == 2 && distance < radius - 1) {
                                world.setBlockState(pos, islandMaterial, Block.NOTIFY_LISTENERS);
                            } else if (y < 2) {
                                world.setBlockState(pos, islandCore, Block.NOTIFY_LISTENERS);
                            }
                        }
                    }
                }
            }
            
            // Phoenix nest on top (if phoenix is meant to spawn here)
            BlockPos nestPos = base.up(3);
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    world.setBlockState(nestPos.add(x, 0, z), Blocks.HAY_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
            
            // Special loot chest
            placeLoot(world, nestPos.up(), RentapoltMod.id("chests/mutant_tower"), random);
            
            // Spawn Ancient Phoenix boss (15% chance for rare encounter)
            if (random.nextFloat() < 0.15f) {
                BlockPos bossSpawn = nestPos.up(3);
                AncientPhoenixEntity boss = new AncientPhoenixEntity(RentapoltEntities.ANCIENT_PHOENIX, world);
                boss.refreshPositionAndAngles(bossSpawn.getX() + 0.5, bossSpawn.getY(), bossSpawn.getZ() + 0.5, 0.0F, 0.0F);
                boss.initialize(world, world.getLocalDifficulty(bossSpawn), SpawnReason.STRUCTURE, null, null);
                world.spawnEntity(boss);
            }
            
            // Decorative tree
            world.setBlockState(nestPos.add(3, 0, 0), Blocks.OAK_LOG.getDefaultState(), Block.NOTIFY_LISTENERS);
            for (int i = 1; i <= 4; i++) {
                world.setBlockState(nestPos.add(3, i, 0), Blocks.OAK_LOG.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    world.setBlockState(nestPos.add(3 + x, 4, z), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(nestPos.add(3 + x, 5, z), Blocks.OAK_LEAVES.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
            
            return true;
        };
    }
}
