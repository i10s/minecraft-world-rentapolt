package com.rentapolt.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireSwordItem extends SwordItem {
    public FireSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(net.minecraft.item.ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(8);
        target.damage(attacker.getDamageSources().onFire(), 4.0F);
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(net.minecraft.entity.EquipmentSlot.MAINHAND));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<net.minecraft.item.ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<net.minecraft.item.ItemStack> result = super.use(world, user, hand);
        if (!world.isClient) {
            user.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE, 200, 0));
        }
        return result;
    }
}
