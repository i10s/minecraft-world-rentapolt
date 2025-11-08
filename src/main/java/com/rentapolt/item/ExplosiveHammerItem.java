package com.rentapolt.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ExplosiveHammerItem extends SwordItem {
    public ExplosiveHammerItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        ActionResult result = super.useOnEntity(stack, user, entity, hand);
        if (!user.getWorld().isClient) {
            entity.getWorld().createExplosion(user, entity.getX(), entity.getBodyY(0.5D), entity.getZ(), 2.3F,
                    World.ExplosionSourceType.MOB);
            stack.damage(2, user, e -> e.sendEquipmentBreakStatus(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        }
        return result;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient) {
            target.takeKnockback(1.5F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public net.minecraft.util.TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        net.minecraft.util.TypedActionResult<ItemStack> result = super.use(world, user, hand);
        if (!world.isClient) {
            world.createExplosion(user, user.getX(), user.getBodyY(0.5D), user.getZ(), 2.6F, World.ExplosionSourceType.MOB);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.2F, 0.9F);
            ItemStack stack = result.getValue();
            stack.damage(3, user, e -> e.sendEquipmentBreakStatus(hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        }
        return result;
    }
}
