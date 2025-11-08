package com.rentapolt.world.feature;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

@FunctionalInterface
public interface StructureGenerator {
    boolean generate(ServerWorld world, BlockPos origin, Random random);
}
