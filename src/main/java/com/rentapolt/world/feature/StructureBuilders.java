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
            
            // === 5% CHANCE TO SPAWN ICONIC NYC SKYSCRAPER ===
            if (random.nextFloat() < 0.05f) {
                int iconicChoice = random.nextInt(5);
                BlockState roadMaterial = ROAD_MATERIALS[random.nextInt(ROAD_MATERIALS.length)];
                BlockState baseMaterial = CITY_BASE_MATERIALS[random.nextInt(CITY_BASE_MATERIALS.length)];
                
                switch (iconicChoice) {
                    case 0: // Empire State Building
                        layRoad(world, base, roadMaterial, 24);
                        layPad(world, base, baseMaterial, 20);
                        buildEmpireStateBuilding(world, base, random);
                        RentapoltMod.LOGGER.info("Generated EMPIRE STATE BUILDING at {}", base);
                        return true;
                    
                    case 1: // One World Trade Center
                        layRoad(world, base, roadMaterial, 39);
                        layPad(world, base, baseMaterial, 35);
                        buildOneWorldTradeCenter(world, base, random);
                        RentapoltMod.LOGGER.info("Generated ONE WORLD TRADE CENTER at {}", base);
                        return true;
                    
                    case 2: // Chrysler Building
                        layRoad(world, base, roadMaterial, 16);
                        layPad(world, base, baseMaterial, 12);
                        buildChryslerBuilding(world, base, random);
                        RentapoltMod.LOGGER.info("Generated CHRYSLER BUILDING at {}", base);
                        return true;
                    
                    case 3: // Flatiron Building
                        layRoad(world, base, roadMaterial, 34);
                        layPad(world, base, baseMaterial, 30);
                        buildFlatironBuilding(world, base, random);
                        RentapoltMod.LOGGER.info("Generated FLATIRON BUILDING at {}", base);
                        return true;
                    
                    case 4: // 432 Park Avenue
                        layRoad(world, base, roadMaterial, 10);
                        layPad(world, base, baseMaterial, 6);
                        build432ParkAvenue(world, base, random);
                        RentapoltMod.LOGGER.info("Generated 432 PARK AVENUE at {}", base);
                        return true;
                }
            }
            
            // === NORMAL PROCEDURAL BUILDING ===
            
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
            
            // ========== SUPER NYC REALISTIC FEATURES ==========
            
            // Yellow taxis (70% chance)
            if (random.nextFloat() < 0.7f) {
                addYellowTaxi(world, base.add(baseWidth + 3, 1, -5), random);
            }
            
            // Hot dog / pretzel stands (40% chance)
            if (random.nextFloat() < 0.4f) {
                addStreetVendorCart(world, base.add(-(baseWidth + 3), 1, 4), random);
            }
            
            // Fire escape stairs on buildings (80% on medium+ buildings)
            if (height > 15 && random.nextFloat() < 0.8f) {
                addFireEscape(world, base, height, baseWidth, random);
            }
            
            // AC units in windows (buildings > 20 blocks)
            if (height > 20) {
                addACUnits(world, base, height, baseWidth, random);
            }
            
            // Water tower on roof (60% on buildings > 25 blocks)
            if (height > 25 && random.nextFloat() < 0.6f) {
                addWaterTower(world, base.up(height), random);
            }
            
            // Trash bags and dumpsters (very common in NYC!)
            if (random.nextFloat() < 0.6f) {
                addTrashPile(world, base.add(baseWidth + 2, 1, -5), random);
            }
            
            // Newspaper boxes (50% chance)
            if (random.nextFloat() < 0.5f) {
                addNewspaperBox(world, base.add(-(baseWidth + 2), 1, -4), random);
            }
            
            // Awnings over storefronts (ground floor only, 70%)
            if (height < 20 && random.nextFloat() < 0.7f) {
                addStoreAwning(world, base.add(baseWidth / 2, 3, 0), random);
            }
            
            // Graffiti on lower walls (30% chance)
            if (random.nextFloat() < 0.3f) {
                addGraffiti(world, base, baseWidth, random);
            }
            
            // Delivery trucks (less common)
            if (random.nextFloat() < 0.3f) {
                addDeliveryTruck(world, base.add(baseWidth + 5, 1, 8), random);
            }
            
            // Construction scaffolding (10% - work in progress!)
            if (random.nextFloat() < 0.1f && height > 30) {
                addScaffolding(world, base, height, baseWidth, random);
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
    
    // ==================== SUPER NYC REALISTIC FEATURES ====================
    
    /**
     * Yellow NYC taxi cab
     */
    private static void addYellowTaxi(ServerWorld world, BlockPos pos, Random random) {
        BlockState yellow = Blocks.YELLOW_CONCRETE.getDefaultState();
        BlockState black = Blocks.BLACK_CONCRETE.getDefaultState();
        BlockState glass = Blocks.GLASS.getDefaultState();
        
        // Car body (3x2x2)
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 2; z++) {
                world.setBlockState(pos.add(x, 0, z), yellow, Block.NOTIFY_LISTENERS);
            }
        }
        
        // Windows
        world.setBlockState(pos.add(1, 1, 0), glass, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(1, 1, 1), glass, Block.NOTIFY_LISTENERS);
        
        // Wheels (black concrete)
        world.setBlockState(pos.add(0, -1, 0), black, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(0, -1, 1), black, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(2, -1, 0), black, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(2, -1, 1), black, Block.NOTIFY_LISTENERS);
        
        // Taxi light on roof
        world.setBlockState(pos.add(1, 1, 0), Blocks.YELLOW_STAINED_GLASS.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    /**
     * Hot dog / pretzel vendor cart
     */
    private static void addStreetVendorCart(ServerWorld world, BlockPos pos, Random random) {
        // Cart base (red and white stripe)
        world.setBlockState(pos, Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(1, 0, 0), Blocks.WHITE_WOOL.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Cart top
        world.setBlockState(pos.up(), Blocks.IRON_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(1, 1, 0), Blocks.IRON_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Umbrella pole
        world.setBlockState(pos.add(0, 2, 0), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(0, 3, 0), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        
        // Umbrella (red/white)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockState umbrellaColor = (x + z) % 2 == 0 ? 
                    Blocks.RED_WOOL.getDefaultState() : Blocks.WHITE_WOOL.getDefaultState();
                world.setBlockState(pos.add(x, 4, z), umbrellaColor, Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    /**
     * Fire escape stairs zigzagging down building side
     */
    private static void addFireEscape(ServerWorld world, BlockPos base, int height, int width, Random random) {
        BlockState ironBars = Blocks.IRON_BARS.getDefaultState();
        BlockState ironBlock = Blocks.IRON_BLOCK.getDefaultState();
        
        // Pick a side
        int side = random.nextInt(4);
        int x = 0, z = 0;
        
        switch(side) {
            case 0: x = width; z = 0; break;      // East
            case 1: x = -width; z = 0; break;     // West
            case 2: x = 0; z = width; break;      // South
            case 3: x = 0; z = -width; break;     // North
        }
        
        // Build platforms and ladders every 4 floors
        for (int y = 2; y < height - 2; y += 4) {
            BlockPos platform = base.add(x, y, z);
            
            // Platform
            for (int px = -1; px <= 1; px++) {
                for (int pz = -1; pz <= 1; pz++) {
                    world.setBlockState(platform.add(px, 0, pz), ironBlock, Block.NOTIFY_LISTENERS);
                }
            }
            
            // Railings
            for (int px = -1; px <= 1; px++) {
                world.setBlockState(platform.add(px, 1, -1), ironBars, Block.NOTIFY_LISTENERS);
                world.setBlockState(platform.add(px, 1, 1), ironBars, Block.NOTIFY_LISTENERS);
            }
            for (int pz = -1; pz <= 1; pz++) {
                world.setBlockState(platform.add(-1, 1, pz), ironBars, Block.NOTIFY_LISTENERS);
                world.setBlockState(platform.add(1, 1, pz), ironBars, Block.NOTIFY_LISTENERS);
            }
            
            // Ladder connecting to next platform
            if (y + 4 < height - 2) {
                for (int ly = 1; ly < 4; ly++) {
                    world.setBlockState(platform.add(0, ly, 0), Blocks.LADDER.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }
    
    /**
     * Air conditioning units sticking out of windows
     */
    private static void addACUnits(ServerWorld world, BlockPos base, int height, int width, Random random) {
        BlockState ac = Blocks.IRON_BLOCK.getDefaultState();
        
        // Add AC units on multiple floors
        for (int floor = 5; floor < height; floor += random.nextBetween(3, 6)) {
            // Random side
            if (random.nextBoolean()) {
                // East or West side
                int x = random.nextBoolean() ? width : -width;
                int z = random.nextBetween(-width + 2, width - 2);
                world.setBlockState(base.add(x, floor, z), ac, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(x + (x > 0 ? 1 : -1), floor, z), ac, Block.NOTIFY_LISTENERS);
            } else {
                // North or South side
                int z = random.nextBoolean() ? width : -width;
                int x = random.nextBetween(-width + 2, width - 2);
                world.setBlockState(base.add(x, floor, z), ac, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(x, floor, z + (z > 0 ? 1 : -1)), ac, Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    /**
     * Iconic NYC wooden water tower on roof
     */
    private static void addWaterTower(ServerWorld world, BlockPos roofPos, Random random) {
        BlockState wood = Blocks.OAK_PLANKS.getDefaultState();
        BlockState darkWood = Blocks.SPRUCE_PLANKS.getDefaultState();
        
        // Support legs
        for (int i = 0; i < 4; i++) {
            world.setBlockState(roofPos.add(i == 0 || i == 1 ? -1 : 1, i, i == 0 || i == 2 ? -1 : 1), 
                Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        }
        
        // Tank base (cylinder approximation)
        for (int y = 4; y < 8; y++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (x*x + z*z <= 4) { // Circular
                        BlockState plank = (y % 2 == 0) ? wood : darkWood;
                        world.setBlockState(roofPos.add(x, y, z), plank, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
        
        // Conical roof
        world.setBlockState(roofPos.add(0, 8, 0), darkWood, Block.NOTIFY_LISTENERS);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(roofPos.add(x, 8, z), darkWood, Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    /**
     * NYC trash bags (very common!)
     */
    private static void addTrashPile(ServerWorld world, BlockPos pos, Random random) {
        int bags = random.nextBetween(2, 5);
        
        for (int i = 0; i < bags; i++) {
            int xOff = random.nextBetween(-1, 1);
            int zOff = random.nextBetween(-1, 1);
            
            BlockState bagColor = random.nextBoolean() ? 
                Blocks.BLACK_WOOL.getDefaultState() : Blocks.GRAY_WOOL.getDefaultState();
            
            world.setBlockState(pos.add(xOff, 0, zOff), bagColor, Block.NOTIFY_LISTENERS);
            
            // Some bags stacked
            if (random.nextFloat() < 0.3f) {
                world.setBlockState(pos.add(xOff, 1, zOff), bagColor, Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    /**
     * Newspaper vending box
     */
    private static void addNewspaperBox(ServerWorld world, BlockPos pos, Random random) {
        // Box body (blue or red)
        BlockState boxColor = random.nextBoolean() ? 
            Blocks.BLUE_CONCRETE.getDefaultState() : Blocks.RED_CONCRETE.getDefaultState();
        
        world.setBlockState(pos, boxColor, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.up(), boxColor, Block.NOTIFY_LISTENERS);
        
        // Glass front
        world.setBlockState(pos.add(1, 0, 0), Blocks.GLASS.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(1, 1, 0), Blocks.GLASS.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    /**
     * Store awning (fabric over storefront)
     */
    private static void addStoreAwning(ServerWorld world, BlockPos pos, Random random) {
        BlockState[] awningColors = {
            Blocks.RED_WOOL.getDefaultState(),
            Blocks.GREEN_WOOL.getDefaultState(),
            Blocks.BLUE_WOOL.getDefaultState(),
            Blocks.YELLOW_WOOL.getDefaultState(),
            Blocks.WHITE_WOOL.getDefaultState()
        };
        
        BlockState color = awningColors[random.nextInt(awningColors.length)];
        
        // Horizontal awning extending out
        for (int x = -2; x <= 2; x++) {
            for (int z = 0; z < 2; z++) {
                world.setBlockState(pos.add(x, 0, z), color, Block.NOTIFY_LISTENERS);
            }
        }
        
        // Support poles
        world.setBlockState(pos.add(-2, -1, 1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(-2, -2, 1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(2, -1, 1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(2, -2, 1), Blocks.OAK_FENCE.getDefaultState(), Block.NOTIFY_LISTENERS);
    }
    
    /**
     * Graffiti on walls (colored concrete patterns)
     */
    private static void addGraffiti(ServerWorld world, BlockPos base, int width, Random random) {
        BlockState[] colors = {
            Blocks.LIME_CONCRETE.getDefaultState(),
            Blocks.CYAN_CONCRETE.getDefaultState(),
            Blocks.MAGENTA_CONCRETE.getDefaultState(),
            Blocks.ORANGE_CONCRETE.getDefaultState()
        };
        
        // Pick a wall
        int side = random.nextInt(4);
        int x = 0, z = 0;
        
        switch(side) {
            case 0: x = width; break;
            case 1: x = -width; break;
            case 2: z = width; break;
            case 3: z = -width; break;
        }
        
        // Random graffiti pattern (2-4 blocks high, 3-5 wide)
        int graffitiHeight = random.nextBetween(2, 4);
        int graffitiWidth = random.nextBetween(3, 5);
        
        for (int gy = 0; gy < graffitiHeight; gy++) {
            for (int gx = 0; gx < graffitiWidth; gx++) {
                if (random.nextFloat() < 0.6f) { // Not all blocks filled
                    BlockState color = colors[random.nextInt(colors.length)];
                    if (side < 2) {
                        world.setBlockState(base.add(x, gy + 2, gx - graffitiWidth/2), color, Block.NOTIFY_LISTENERS);
                    } else {
                        world.setBlockState(base.add(gx - graffitiWidth/2, gy + 2, z), color, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }
    
    /**
     * White delivery truck
     */
    private static void addDeliveryTruck(ServerWorld world, BlockPos pos, Random random) {
        BlockState white = Blocks.WHITE_CONCRETE.getDefaultState();
        BlockState black = Blocks.BLACK_CONCRETE.getDefaultState();
        BlockState glass = Blocks.GLASS.getDefaultState();
        
        // Truck body (bigger than taxi - 4x3x3)
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 3; z++) {
                    world.setBlockState(pos.add(x, y, z), white, Block.NOTIFY_LISTENERS);
                }
            }
        }
        
        // Cab (front)
        world.setBlockState(pos.add(0, 2, 0), glass, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(0, 2, 1), glass, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(0, 2, 2), glass, Block.NOTIFY_LISTENERS);
        
        // Wheels
        world.setBlockState(pos.add(0, -1, 0), black, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(0, -1, 2), black, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(3, -1, 0), black, Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.add(3, -1, 2), black, Block.NOTIFY_LISTENERS);
    }
    
    /**
     * Construction scaffolding on building
     */
    private static void addScaffolding(ServerWorld world, BlockPos base, int height, int width, Random random) {
        BlockState scaffold = Blocks.SCAFFOLDING.getDefaultState();
        
        // Pick a side
        int side = random.nextInt(4);
        int x = 0, z = 0;
        
        switch(side) {
            case 0: x = width + 1; break;
            case 1: x = -(width + 1); break;
            case 2: z = width + 1; break;
            case 3: z = -(width + 1); break;
        }
        
        // Build scaffolding from ground to random height
        int scaffoldHeight = random.nextBetween(height / 2, height - 5);
        
        for (int y = 0; y < scaffoldHeight; y++) {
            for (int offset = -2; offset <= 2; offset++) {
                BlockPos scaffoldPos = side < 2 ? 
                    base.add(x, y, offset) : 
                    base.add(offset, y, z);
                world.setBlockState(scaffoldPos, scaffold, Block.NOTIFY_LISTENERS);
            }
        }
    }

    // ========== ICONIC NYC SKYSCRAPERS ==========

    /**
     * Add floors and basic interior structure to iconic buildings
     */
    private static void addIconicBuildingFloors(ServerWorld world, BlockPos base, int width, int height, Random random) {
        BlockState floorMaterial = Blocks.SMOOTH_STONE.getDefaultState();
        BlockState stairBlock = Blocks.STONE_BRICK_STAIRS.getDefaultState();
        
        // Add floors every 4 blocks (one floor = 4 blocks high)
        for (int y = 0; y < height; y += 4) {
            // Floor slab
            for (int x = -width; x <= width; x++) {
                for (int z = -width; z <= width; z++) {
                    world.setBlockState(base.add(x, y, z), floorMaterial, Block.NOTIFY_LISTENERS);
                }
            }
            
            // Central elevator shaft (2x2)
            if (height > 40) {
                world.setBlockState(base.add(0, y + 1, 0), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(1, y + 1, 0), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(0, y + 1, 1), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(1, y + 1, 1), Blocks.IRON_BARS.getDefaultState(), Block.NOTIFY_LISTENERS);
                
                for (int i = 1; i < 4; i++) {
                    world.setBlockState(base.add(0, y + i, 0), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(base.add(1, y + i, 0), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(base.add(0, y + i, 1), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.setBlockState(base.add(1, y + i, 1), Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
            
            // Staircase (corner spiral)
            if (y < height - 4) {
                int stairX = -width + 2;
                int stairZ = -width + 2;
                world.setBlockState(base.add(stairX, y + 1, stairZ), stairBlock, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(stairX + 1, y + 2, stairZ), stairBlock, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(stairX + 1, y + 3, stairZ + 1), stairBlock, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(stairX, y + 4, stairZ + 1), stairBlock, Block.NOTIFY_LISTENERS);
            }
            
            // Some office furniture every few floors
            if (y % 12 == 0 && random.nextFloat() < 0.4f) {
                int furnitureX = random.nextBetween(-width + 3, width - 3);
                int furnitureZ = random.nextBetween(-width + 3, width - 3);
                world.setBlockState(base.add(furnitureX, y + 1, furnitureZ), Blocks.CRAFTING_TABLE.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(furnitureX + 1, y + 1, furnitureZ), Blocks.OAK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
            
            // Lights on some floors
            if (y % 8 == 0) {
                world.setBlockState(base.add(-width + 2, y + 2, 0), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(width - 2, y + 2, 0), Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }
    }

    /**
     * Empire State Building - Art Deco masterpiece with stepped crown and antenna
     * Height: ~102 floors (380m to roof, 443m with antenna)
     */
    private static void buildEmpireStateBuilding(ServerWorld world, BlockPos base, Random random) {
        BlockState limestone = Blocks.QUARTZ_BLOCK.getDefaultState();
        BlockState windows = Blocks.LIGHT_GRAY_STAINED_GLASS_PANE.getDefaultState();
        BlockState metalTop = Blocks.IRON_BLOCK.getDefaultState();
        BlockState lights = Blocks.SEA_LANTERN.getDefaultState();

        // Base section (40x40, 20 floors)
        for (int y = 0; y < 80; y++) {
            for (int x = -20; x <= 20; x++) {
                for (int z = -20; z <= 20; z++) {
                    // Only build walls, not solid blocks
                    boolean isWall = Math.abs(x) == 20 || Math.abs(z) == 20;
                    if (isWall) {
                        if ((x + z + y) % 3 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), limestone, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
        // Add floors and interior to base section
        addIconicBuildingFloors(world, base, 19, 80, random);

        // Mid section (30x30, 30 floors)
        for (int y = 80; y < 200; y++) {
            for (int x = -15; x <= 15; x++) {
                for (int z = -15; z <= 15; z++) {
                    boolean isWall = Math.abs(x) == 15 || Math.abs(z) == 15;
                    if (isWall) {
                        if ((x + z + y) % 3 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), limestone, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
        // Add floors to mid section
        addIconicBuildingFloors(world, base.add(0, 80, 0), 14, 120, random);

        // Upper stepped section (20x20, 20 floors)
        for (int y = 200; y < 280; y++) {
            for (int x = -10; x <= 10; x++) {
                for (int z = -10; z <= 10; z++) {
                    boolean isWall = Math.abs(x) == 10 || Math.abs(z) == 10;
                    if (isWall) {
                        if ((x + z + y) % 2 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), limestone, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }

        // Crown with setbacks (Art Deco stepped design)
        for (int y = 280; y < 320; y++) {
            int size = 10 - (y - 280) / 8;
            for (int x = -size; x <= size; x++) {
                for (int z = -size; z <= size; z++) {
                    boolean isEdge = Math.abs(x) == size || Math.abs(z) == size;
                    if (isEdge) {
                        world.setBlockState(base.add(x, y, z), metalTop, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }

        // Antenna/spire
        for (int y = 320; y < 380; y++) {
            world.setBlockState(base.add(0, y, 0), metalTop, Block.NOTIFY_LISTENERS);
            if (y % 10 == 0) {
                world.setBlockState(base.add(0, y, 0), lights, Block.NOTIFY_LISTENERS);
            }
        }
    }

    /**
     * One World Trade Center - Modern glass tower with triangular chamfered edges
     * Height: 104 floors (541m with spire)
     */
    private static void buildOneWorldTradeCenter(ServerWorld world, BlockPos base, Random random) {
        BlockState glass = Blocks.LIGHT_BLUE_STAINED_GLASS.getDefaultState();
        BlockState blueGlass = Blocks.BLUE_STAINED_GLASS.getDefaultState();
        BlockState spire = Blocks.IRON_BLOCK.getDefaultState();

        // Square base that rotates to create chamfered square at top
        for (int y = 0; y < 400; y++) {
            // Gradual rotation effect - base is 35x35, top becomes diamond
            int baseSize = 35 - y / 40;
            
            for (int x = -baseSize; x <= baseSize; x++) {
                for (int z = -baseSize; z <= baseSize; z++) {
                    // Create chamfered corners - only walls
                    boolean isInBounds = Math.abs(x) + Math.abs(z) <= baseSize * 1.4;
                    boolean isWall = Math.abs(x) == baseSize || Math.abs(z) == baseSize;
                    
                    if (isInBounds && isWall) {
                        if ((x + z + y) % 3 == 0) {
                            world.setBlockState(base.add(x, y, z), blueGlass, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), glass, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
        
        // Add floors and interior
        addIconicBuildingFloors(world, base, 30, 400, random);

        // Spire (124m additional height in real building)
        for (int y = 400; y < 480; y++) {
            int spireSize = (480 - y) / 20;
            for (int x = -spireSize; x <= spireSize; x++) {
                for (int z = -spireSize; z <= spireSize; z++) {
                    boolean isEdge = Math.abs(x) == spireSize || Math.abs(z) == spireSize;
                    if (spireSize <= 1 || isEdge) {
                        world.setBlockState(base.add(x, y, z), spire, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    /**
     * Chrysler Building - Art Deco crown with distinctive stainless steel arches
     * Height: 77 floors (319m)
     */
    private static void buildChryslerBuilding(ServerWorld world, BlockPos base, Random random) {
        BlockState brick = Blocks.LIGHT_GRAY_TERRACOTTA.getDefaultState();
        BlockState windows = Blocks.CYAN_STAINED_GLASS_PANE.getDefaultState();
        BlockState steel = Blocks.IRON_BLOCK.getDefaultState();

        // Main tower (25x25, 60 floors)
        for (int y = 0; y < 240; y++) {
            for (int x = -12; x <= 12; x++) {
                for (int z = -12; z <= 12; z++) {
                    boolean isWall = Math.abs(x) == 12 || Math.abs(z) == 12;
                    if (isWall) {
                        if ((x + z + y) % 2 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), brick, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
        
        // Add floors and interior to main tower
        addIconicBuildingFloors(world, base, 11, 240, random);

        // Iconic Art Deco crown with triangular arches (7 tiers)
        for (int tier = 0; tier < 7; tier++) {
            int y = 240 + tier * 10;
            int size = (int)(12 - tier * 1.5);
            
            for (int x = -size; x <= size; x++) {
                for (int z = -size; z <= size; z++) {
                    // Create arched windows in crown - only edges
                    boolean isEdge = Math.abs(x) == size || Math.abs(z) == size;
                    if (isEdge) {
                        if ((x + z) % 2 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), steel, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
            
            // Triangular arch detail
            for (int i = 0; i < 10; i++) {
                world.setBlockState(base.add(size - i/2, y + i, 0), steel, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(-size + i/2, y + i, 0), steel, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(0, y + i, size - i/2), steel, Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(0, y + i, -size + i/2), steel, Block.NOTIFY_LISTENERS);
            }
        }

        // Spire
        for (int y = 310; y < 350; y++) {
            world.setBlockState(base.add(0, y, 0), steel, Block.NOTIFY_LISTENERS);
        }
    }

    /**
     * Flatiron Building - Iconic triangular "wedge" shape
     * Height: 22 floors (87m)
     */
    private static void buildFlatironBuilding(ServerWorld world, BlockPos base, Random random) {
        BlockState terracotta = Blocks.ORANGE_TERRACOTTA.getDefaultState();
        BlockState windows = Blocks.WHITE_STAINED_GLASS_PANE.getDefaultState();
        BlockState frame = Blocks.BROWN_CONCRETE.getDefaultState();

        // Triangular footprint - iconic wedge shape
        for (int y = 0; y < 88; y++) {
            // North-pointing triangle
            for (int x = 0; x < 30; x++) {
                int maxZ = (30 - x) / 2; // Creates triangle
                for (int z = -maxZ; z <= maxZ; z++) {
                    // Only build walls, not solid
                    boolean isWall = Math.abs(z) == maxZ || x == 0 || x == 29;
                    if (isWall) {
                        if ((x + z + y) % 3 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), terracotta, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
        
        // Add floors - special handling for triangular shape
        BlockState floorMaterial = Blocks.SMOOTH_STONE.getDefaultState();
        for (int y = 0; y < 88; y += 4) {
            for (int x = 0; x < 30; x++) {
                int maxZ = (30 - x) / 2;
                for (int z = -maxZ; z <= maxZ; z++) {
                    world.setBlockState(base.add(x, y, z), floorMaterial, Block.NOTIFY_LISTENERS);
                }
            }
            // Staircase
            if (y < 84) {
                world.setBlockState(base.add(15, y + 1, 0), Blocks.STONE_BRICK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(15, y + 2, 1), Blocks.STONE_BRICK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
                world.setBlockState(base.add(15, y + 3, 0), Blocks.STONE_BRICK_STAIRS.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        }

        // Decorative cornice at top
        for (int x = 0; x < 30; x++) {
            int maxZ = (30 - x) / 2;
            for (int z = -maxZ; z <= maxZ; z++) {
                world.setBlockState(base.add(x, 88, z), frame, Block.NOTIFY_LISTENERS);
            }
        }
    }

    /**
     * 432 Park Avenue - Ultra-thin residential tower with grid of square windows
     * Height: 96 floors (426m)
     */
    private static void build432ParkAvenue(ServerWorld world, BlockPos base, Random random) {
        BlockState darkFrame = Blocks.BLACK_CONCRETE.getDefaultState();
        BlockState windows = Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.getDefaultState();

        // Extremely slender profile - 12x12 footprint, very tall
        for (int y = 0; y < 426; y++) {
            // Skip mechanical floors (void every 96 floors)
            if (y % 96 == 0 && y > 0) {
                continue;
            }
            
            for (int x = -6; x <= 6; x++) {
                for (int z = -6; z <= 6; z++) {
                    // Only build walls with grid pattern
                    boolean isWall = Math.abs(x) == 6 || Math.abs(z) == 6;
                    if (isWall) {
                        if (x % 2 == 0 && z % 2 == 0) {
                            world.setBlockState(base.add(x, y, z), windows, Block.NOTIFY_LISTENERS);
                        } else {
                            world.setBlockState(base.add(x, y, z), darkFrame, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }
        }
        
        // Add floors and interior
        addIconicBuildingFloors(world, base, 5, 426, random);
    }
}
