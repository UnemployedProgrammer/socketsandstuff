package com.sebastian.sockets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Sockets.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder BUILDER_INT = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue IS_DEBUG = BUILDER
            .comment("If you set this to true in production, your game will get corrupted or will crash! Therefore, keep your hands off!")
            .define("isDeveloperMode", false);

    private static final ForgeConfigSpec.IntValue BOOM_ITEMS_RANGE = BUILDER
            .comment("When you put for example iron in a toaster, for example, it explodes. This is the strength. Set to 0 if disabled.")
            .defineInRange("dangerousExplodeItemsRange", 5, 1, 1000);

    private static final ForgeConfigSpec.BooleanValue BOOM_ITEMS_FIRE = BUILDER
            .comment("When you put for example iron in a toaster, for example, it explodes. This is enabled, when the explosion should spawn fire.")
            .define("dangerousExplodeItemsFire", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean devMode;
    public static int rangeBoomItems;
    public static boolean fireBoomItems;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        devMode = IS_DEBUG.get();
        fireBoomItems = BOOM_ITEMS_FIRE.get();
        rangeBoomItems = BOOM_ITEMS_RANGE.get();
    }
}
