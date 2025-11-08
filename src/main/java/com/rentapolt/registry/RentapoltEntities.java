package com.rentapolt.registry;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.math.random.Random;

import com.rentapolt.RentapoltMod;
import com.rentapolt.entity.RentapoltHostileEntity;
import com.rentapolt.entity.RentapoltPassiveEntity;
import com.rentapolt.entity.config.RentapoltMobConfig;
import com.rentapolt.entity.config.RentapoltPassiveConfig;
import com.rentapolt.entity.config.RentapoltMobConfig.SpecialAbility;
import com.rentapolt.entity.config.RentapoltPassiveConfig.PassiveAbility;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;

public class RentapoltEntities {
    public static final Map<EntityType<RentapoltHostileEntity>, RentapoltMobConfig> HOSTILE_CONFIGS = new HashMap<>();
    public static final Map<EntityType<RentapoltPassiveEntity>, RentapoltPassiveConfig> PASSIVE_CONFIGS = new HashMap<>();

    public static EntityType<RentapoltHostileEntity> MUTANT_CREEPER;
    public static EntityType<RentapoltHostileEntity> MUTANT_ZOMBIE;
    public static EntityType<RentapoltHostileEntity> FIRE_GOLEM;
    public static EntityType<RentapoltHostileEntity> PLASMA_BEAST;
    public static EntityType<RentapoltHostileEntity> SHADOW_SERPENT;

    public static EntityType<RentapoltPassiveEntity> LION;
    public static EntityType<RentapoltPassiveEntity> ELEPHANT;
    public static EntityType<RentapoltPassiveEntity> PHOENIX;
    public static EntityType<RentapoltPassiveEntity> GRIFFIN;

    public static void register() {
        MUTANT_CREEPER = registerHostile("mutant_creeper",
                new RentapoltMobConfig(40.0F, 8.0F, 0.32D, 24.0D, SpecialAbility.MINI_EXPLOSION, false, 0.9F, 1.8F));
        MUTANT_ZOMBIE = registerHostile("mutant_zombie",
                new RentapoltMobConfig(45.0F, 7.0F, 0.31D, 24.0D, SpecialAbility.TOXIC_CLOUD, false, 0.8F, 2.1F));
        FIRE_GOLEM = registerHostile("fire_golem",
                new RentapoltMobConfig(60.0F, 10.0F, 0.25D, 28.0D, SpecialAbility.FIRE_AURA, true, 1.3F, 3.3F));
        PLASMA_BEAST = registerHostile("plasma_beast",
                new RentapoltMobConfig(52.0F, 9.0F, 0.28D, 26.0D, SpecialAbility.PLASMA_SHOT, true, 1.2F, 2.4F));
        SHADOW_SERPENT = registerHostile("shadow_serpent",
                new RentapoltMobConfig(38.0F, 8.0F, 0.35D, 30.0D, SpecialAbility.SHADOW_CURSE, false, 1.1F, 1.8F));

        LION = registerPassive("lion",
                new RentapoltPassiveConfig(30.0F, 0.35D, 24.0D, PassiveAbility.STRENGTH_AURA, 1.0F, 1.6F));
        ELEPHANT = registerPassive("elephant",
                new RentapoltPassiveConfig(55.0F, 0.25D, 20.0D, PassiveAbility.RESISTANCE_AURA, 1.5F, 2.8F));
        PHOENIX = registerPassive("phoenix",
                new RentapoltPassiveConfig(32.0F, 0.4D, 26.0D, PassiveAbility.FIRE_IMMUNITY_AURA, 1.0F, 1.8F));
        GRIFFIN = registerPassive("griffin",
                new RentapoltPassiveConfig(36.0F, 0.38D, 26.0D, PassiveAbility.SKY_GRACE, 1.2F, 2.0F));
    }

    private static EntityType<RentapoltHostileEntity> registerHostile(String name, RentapoltMobConfig config) {
        Identifier id = RentapoltMod.id(name);
        FabricEntityTypeBuilder<RentapoltHostileEntity> builder = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER,
                (EntityType<RentapoltHostileEntity> entityType, World world) ->
                        new RentapoltHostileEntity(entityType, world).configure(config))
                .dimensions(EntityDimensions.changing(config.width(), config.height()))
                .trackRangeBlocks(96);
        if (config.fireImmune()) {
            builder.fireImmune();
        }
        EntityType<RentapoltHostileEntity> type = Registry.register(Registries.ENTITY_TYPE, id, builder.build());
        HOSTILE_CONFIGS.put(type, config);
        FabricDefaultAttributeRegistry.register(type, RentapoltHostileEntity.createAttributes(config));
        SpawnRestriction.register(type, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                RentapoltEntities::canSpawnHostile);
        return type;
    }

    private static EntityType<RentapoltPassiveEntity> registerPassive(String name, RentapoltPassiveConfig config) {
        Identifier id = RentapoltMod.id(name);
        EntityType<RentapoltPassiveEntity> type = Registry.register(Registries.ENTITY_TYPE, id,
                FabricEntityTypeBuilder.<RentapoltPassiveEntity>create(SpawnGroup.CREATURE,
                                (EntityType<RentapoltPassiveEntity> entityType, World world) ->
                                        new RentapoltPassiveEntity(entityType, world).configure(config))
                        .dimensions(EntityDimensions.changing(config.width(), config.height()))
                        .trackRangeBlocks(80)
                        .build());
        PASSIVE_CONFIGS.put(type, config);
        FabricDefaultAttributeRegistry.register(type, RentapoltPassiveEntity.createAttributes(config));
        SpawnRestriction.register(type, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                RentapoltEntities::canSpawnPassive);
        return type;
    }

    private static boolean canSpawnHostile(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason,
                                           BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL
                && HostileEntity.canSpawnInDark(type, world, spawnReason, pos, random);
    }

    private static boolean canSpawnPassive(EntityType<? extends MobEntity> type, ServerWorldAccess world,
                                           SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBaseLightLevel(pos, 0) > 7 && MobEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }
}
