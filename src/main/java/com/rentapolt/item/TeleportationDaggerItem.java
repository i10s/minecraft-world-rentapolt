package com.rentapolt.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.Heightmap;

public class TeleportationDaggerItem extends SwordItem {
    public TeleportationDaggerItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 40);
        if (!world.isClient) {
            HitResult hit = user.raycast(16.0D, 0.0F, false);
            Vec3d target = hit.getPos();
            if (hit.getType() == HitResult.Type.BLOCK) {
                Vec3d offset = user.getRotationVec(1.0F).normalize().multiply(1.5D);
                target = target.subtract(offset);
            }
            Mutable mutable = BlockPos.ofFloored(target).mutableCopy();
            int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable.getX(), mutable.getZ());
            while (!world.getBlockState(mutable).isAir() && mutable.getY() < topY) {
                mutable.move(Direction.UP);
            }
            Vec3d destination = new Vec3d(mutable.getX() + 0.5D, mutable.getY(), mutable.getZ() + 0.5D);
            user.requestTeleport(destination.x, destination.y, destination.z);
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.2F);
            stack.damage(2, user, entity -> entity.sendEquipmentBreakStatus(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient) {
            target.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.SLOWNESS, 100, 1));
        }
        return super.postHit(stack, target, attacker);
    }
}
