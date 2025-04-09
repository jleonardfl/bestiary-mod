package com.jc.bestiary.registry;

import com.jc.bestiary.Bestiary;
import com.jc.bestiary.entity.BrownBearEntity;
import com.jc.bestiary.entity.MothManEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BestiaryEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Bestiary.MODID);

    public static final Supplier<EntityType<MothManEntity>> MOTHMAN = ENTITIES.register("mothman",
            () -> EntityType.Builder.of(MothManEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 2.0f)
                    .clientTrackingRange(8)
                    .build(Bestiary.MODID + ":mothman"));
    public static final Supplier<EntityType<BrownBearEntity>> BROWN_BEAR = ENTITIES.register("brown_bear",
            () -> EntityType.Builder.of(BrownBearEntity::new, MobCategory.CREATURE)
                    .immuneTo(Blocks.SWEET_BERRY_BUSH)
                    .clientTrackingRange(8)
                    .build(Bestiary.MODID + ":brown_bear"));

}
