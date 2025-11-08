package com.rentapolt.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class TeleporterBlock extends Block {
    private static final int RANGE = 100;

    public TeleporterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (!world.isClient && entity instanceof LivingEntity living) {
            Random random = world.getRandom();
            double targetX = pos.getX() + random.nextBetween(-RANGE, RANGE);
            double targetZ = pos.getZ() + random.nextBetween(-RANGE, RANGE);
            int surfaceY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) targetX, (int) targetZ);
            double targetY = surfaceY + 1;
            living.requestTeleport(targetX + 0.5D, targetY, targetZ + 0.5D);
            world.playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 1.0F, 1.2F);
        } else {
            spawnParticles(world, pos);
        }
    }

    private void spawnParticles(World world, BlockPos pos) {
        for (int i = 0; i < 5; i++) {
            double ox = world.random.nextDouble();
            double oy = world.random.nextDouble();
            double oz = world.random.nextDouble();
            world.addParticle(ParticleTypes.PORTAL, pos.getX() + ox, pos.getY() + oy, pos.getZ() + oz, 0, 0.3D, 0);
        }
    }
}
