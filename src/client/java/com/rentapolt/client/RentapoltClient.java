package com.rentapolt.client;

import com.rentapolt.client.render.RentapoltEntityRenderers;
import com.rentapolt.registry.RentapoltBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class RentapoltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RentapoltEntityRenderers.register();
        BlockRenderLayerMap.INSTANCE.putBlock(RentapoltBlocks.CITY_GLOW_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(RentapoltBlocks.ENERGY_BLOCK, RenderLayer.getTranslucent());
    }
}
