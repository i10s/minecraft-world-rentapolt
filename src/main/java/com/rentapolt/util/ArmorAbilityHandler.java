package com.rentapolt.util;

import com.rentapolt.registry.RentapoltItems;
import com.rentapolt.registry.RentapoltItems.ArmorSet;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public final class ArmorAbilityHandler {
    private ArmorAbilityHandler() {}

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server ->
                server.getPlayerManager().getPlayerList().forEach(ArmorAbilityHandler::applyAbilities));
    }

    private static void applyAbilities(ServerPlayerEntity player) {
        handlePhoenix(player);
        handleSpeed(player);
        handleFlight(player);
        handleInvisibility(player);
        handleHeavy(player);
    }

    private static void handlePhoenix(ServerPlayerEntity player) {
        if (hasFullSet(player, RentapoltItems.PHOENIX_SET)) {
            player.setFireTicks(0);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 220, 0, true, true));
        }
    }

    private static void handleSpeed(ServerPlayerEntity player) {
        if (hasFullSet(player, RentapoltItems.SPEED_SET)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 220, 1, true, true));
        }
    }

    private static void handleFlight(ServerPlayerEntity player) {
        boolean wearing = hasFullSet(player, RentapoltItems.FLIGHT_SET);
        if (wearing) {
            player.getAbilities().allowFlying = true;
            if (!player.getAbilities().flying) {
                player.getAbilities().allowFlying = true;
            }
            player.sendAbilitiesUpdate();
        } else if (!player.isCreative() && !player.isSpectator()) {
            if (player.getAbilities().allowFlying) {
                player.getAbilities().allowFlying = false;
                player.getAbilities().flying = false;
                player.sendAbilitiesUpdate();
            }
        }
    }

    private static void handleInvisibility(ServerPlayerEntity player) {
        if (hasFullSet(player, RentapoltItems.INVISIBILITY_SET)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 220, 0, true, true));
        }
    }

    private static void handleHeavy(ServerPlayerEntity player) {
        if (hasFullSet(player, RentapoltItems.HEAVY_SET)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 220, 1, true, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 220, 0, true, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 220, 0, true, true));
        }
    }

    private static boolean hasFullSet(ServerPlayerEntity player, ArmorSet set) {
        return isWearing(player, set.helmet(), EquipmentSlot.HEAD)
                && isWearing(player, set.chestplate(), EquipmentSlot.CHEST)
                && isWearing(player, set.leggings(), EquipmentSlot.LEGS)
                && isWearing(player, set.boots(), EquipmentSlot.FEET);
    }

    private static boolean isWearing(ServerPlayerEntity player, Item expected, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && stack.isOf(expected);
    }
}
