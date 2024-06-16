package com.sebastian.sockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.sebastian.sockets.customrecipe.ItemStackJsonConverter;
import com.sebastian.sockets.customrecipe.RecipeFileStructureBase;
import com.sebastian.sockets.customrecipe.RecipeTypes;
import com.sebastian.sockets.events.ServerEvents;
import com.sebastian.sockets.reg.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Sockets.MODID)
public class Sockets
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "sockets";
    public static final String GAMEVER = "1.20.1";
    public static final String MODVERS = "0.1.2";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Gson RECIPEGSON = null;

    public Sockets()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        AllItems.ITEMS.register(modEventBus);
        AllBlocks.BLOCKS.register(modEventBus);
        AllTabs.CREATIVE_MODE_TABS.register(modEventBus);
        AllBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        AllSounds.SOUND_EVENTS.register(modEventBus);
        AllParticles.PARTICLE_TYPES.register(modEventBus);
        AllEnchantments.ENCHANTMENTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //Setup GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackJsonConverter());
        RECIPEGSON = gsonBuilder.create();


        //Setup Recipes
        RecipeTypes.initDefault();
        RecipeFileStructureBase.addRecipeType(RecipeTypes.TOASTER_RECIPE);
        RecipeFileStructureBase.checkOrGenerateFolders();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
