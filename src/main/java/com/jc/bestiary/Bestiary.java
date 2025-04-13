package com.jc.bestiary;

import com.jc.bestiary.client.renderer.geo.entity.BrownBearRenderer;
import com.jc.bestiary.client.renderer.geo.entity.MothManRenderer;
import com.jc.bestiary.datagen.DataGenerators;
import com.jc.bestiary.entity.BrownBearEntity;
import com.jc.bestiary.entity.MothManEntity;

import com.jc.bestiary.registry.BestiaryCreative;
import com.jc.bestiary.registry.BestiaryEntities;
import com.jc.bestiary.registry.BestiaryItems;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Bestiary.MODID)
public class Bestiary
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bestiary";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Bestiary(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onAttributeCreation);

        // Register my book generator class to datagen modonomicon
        modEventBus.addListener(DataGenerators::gatherData);

        BestiaryEntities.ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        BestiaryItems.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        BestiaryCreative.CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Bestiary) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(BestiaryItems.MOTHMAN_SPAWN_EGG);
            event.accept(BestiaryItems.BROWN_BEAR_SPAWN_EGG);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code

        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BestiaryEntities.MOTHMAN.get(), MothManRenderer::new);
            event.registerEntityRenderer(BestiaryEntities.BROWN_BEAR.get(), BrownBearRenderer::new);
        }
    }

    private void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(BestiaryEntities.MOTHMAN.get(), MothManEntity.createAttributes().build());
        event.put(BestiaryEntities.BROWN_BEAR.get(), BrownBearEntity.createAttributes().build());
    }
}
