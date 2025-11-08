package com.rentapolt.client.render;

import com.rentapolt.RentapoltMod;
import com.rentapolt.entity.RentapoltHostileEntity;
import com.rentapolt.entity.RentapoltPassiveEntity;
import com.rentapolt.registry.RentapoltEntities;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public final class RentapoltEntityRenderers {
    private RentapoltEntityRenderers() {}

    public static void register() {
        registerHostile(RentapoltEntities.MUTANT_CREEPER, "mutant_creeper");
        registerHostile(RentapoltEntities.MUTANT_ZOMBIE, "mutant_zombie");
        registerHostile(RentapoltEntities.FIRE_GOLEM, "fire_golem");
        registerHostile(RentapoltEntities.PLASMA_BEAST, "plasma_beast");
        registerHostile(RentapoltEntities.SHADOW_SERPENT, "shadow_serpent");

        registerPassive(RentapoltEntities.LION, "lion");
        registerPassive(RentapoltEntities.ELEPHANT, "elephant");
        registerPassive(RentapoltEntities.PHOENIX, "phoenix");
        registerPassive(RentapoltEntities.GRIFFIN, "griffin");
    }

    private static void registerHostile(net.minecraft.entity.EntityType<RentapoltHostileEntity> type, String texture) {
        Identifier id = RentapoltMod.id("textures/entity/" + texture + ".png");
        EntityRendererRegistry.register(type, context -> new MobEntityRenderer<RentapoltHostileEntity, BipedEntityModel<RentapoltHostileEntity>>(context,
                new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER)), 0.6F) {
            @Override
            public Identifier getTexture(RentapoltHostileEntity entity) {
                return id;
            }
        });
    }

    private static void registerPassive(net.minecraft.entity.EntityType<RentapoltPassiveEntity> type, String texture) {
        Identifier id = RentapoltMod.id("textures/entity/" + texture + ".png");
        EntityRendererRegistry.register(type, context -> new MobEntityRenderer<RentapoltPassiveEntity, BipedEntityModel<RentapoltPassiveEntity>>(context,
                new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER)), 0.7F) {
            @Override
            public Identifier getTexture(RentapoltPassiveEntity entity) {
                return id;
            }
        });
    }
}
