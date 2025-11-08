package com.rentapolt.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class LightningBowItem extends BowItem {
    public LightningBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (!world.isClient && user instanceof PlayerEntity player) {
            HitResult hitResult = player.raycast(64.0D, 0.0F, false);
            double x = player.getX();
            double y = player.getEyeY();
            double z = player.getZ();
            if (hitResult instanceof EntityHitResult entityHit) {
                Entity entity = entityHit.getEntity();
                x = entity.getX();
                y = entity.getBodyY(0.5D);
                z = entity.getZ();
            } else if (hitResult instanceof BlockHitResult blockHit) {
                x = blockHit.getPos().x;
                y = blockHit.getPos().y;
                z = blockHit.getPos().z;
            }
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            if (lightning != null) {
                lightning.refreshPositionAfterTeleport(x, y, z);
                world.spawnEntity(lightning);
                EquipmentSlot slot = user.getActiveHand() == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.damage(1, user, e -> e.sendEquipmentBreakStatus(slot));
            }
        }
    }
}
