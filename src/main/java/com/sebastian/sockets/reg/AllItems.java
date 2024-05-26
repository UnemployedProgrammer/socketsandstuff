package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.items.WireAndPlug;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Sockets.MODID);

    public static final RegistryObject<Item> TOASTED_BREAD = ITEMS.register("toasted_bread",
            () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.8F).build())));
    public static final RegistryObject<WireAndPlug> WIRE_AND_PLUG = ITEMS.register("wire_and_plug",
            () -> new WireAndPlug(new Item.Properties()));
}
