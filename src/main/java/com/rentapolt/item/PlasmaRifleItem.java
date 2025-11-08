package com.rentapolt.item;

import com.rentapolt.registry.RentapoltSounds;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlasmaRifleItem extends Item {
    public PlasmaRifleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            Vec3d look = user.getRotationVec(1.0F);
            SmallFireballEntity plasma = new SmallFireballEntity(world, user,
                    look.x * 0.5D, look.y * 0.5D, look.z * 0.5D);
            plasma.setPos(user.getX(), user.getEyeY(), user.getZ());
            world.spawnEntity(plasma);
            world.playSound(null, user.getBlockPos(), RentapoltSounds.PLASMA_BEAM, SoundCategory.PLAYERS, 1.0F, 1.3F);
            stack.damage(1, user, e -> e.sendEquipmentBreakStatus(hand == Hand.MAIN_HAND
                    ? net.minecraft.entity.EquipmentSlot.MAINHAND : net.minecraft.entity.EquipmentSlot.OFFHAND));
        }
        return TypedActionResult.success(stack, world.isClient());
    }
}
