package com.jc.bestiary.client.model.geo.entity;

import com.jc.bestiary.Bestiary;
import com.jc.bestiary.entity.MothManEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MothManModel extends DefaultedEntityGeoModel<MothManEntity> {
    public MothManModel() {
        super(ResourceLocation.fromNamespaceAndPath(Bestiary.MODID, "mothman"));
    }
}
