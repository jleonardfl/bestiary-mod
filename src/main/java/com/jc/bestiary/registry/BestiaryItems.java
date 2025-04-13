package com.jc.bestiary.registry;

import com.jc.bestiary.Bestiary;
import com.klikli_dev.modonomicon.item.ModonomiconItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BestiaryItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Bestiary.MODID);

    // SPAWN EGGS
    public static final DeferredItem<Item> MOTHMAN_SPAWN_EGG = ITEMS.register(
            "mothman_spwan_egg", () -> new DeferredSpawnEggItem(
                    BestiaryEntities.MOTHMAN,
                    0x4a4639,
                    0xf7e82f,
                    new Item.Properties()));

    public static final DeferredItem<Item> BROWN_BEAR_SPAWN_EGG = ITEMS.register(
            "brown_bear_spwan_egg", () -> new DeferredSpawnEggItem(
                    BestiaryEntities.BROWN_BEAR,
                    0x4a4639,
                    0xf7e82f,
                    new Item.Properties()));

    public static final DeferredItem<Item> BESTIARY_BOOK = ITEMS.register(
            "demo", () -> new ModonomiconItem(new Item.Properties()));


}
