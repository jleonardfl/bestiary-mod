package com.jc.bestiary.client.renderer.geo.entity;

import com.jc.bestiary.Bestiary;
import com.jc.bestiary.entity.BrownBearEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrownBearRenderer extends GeoEntityRenderer<BrownBearEntity> {
    public BrownBearRenderer(EntityRendererProvider.Context context) {
        super(context, new DefaultedEntityGeoModel<>(ResourceLocation.fromNamespaceAndPath(Bestiary.MODID, "brown_bear")));
    }
}
