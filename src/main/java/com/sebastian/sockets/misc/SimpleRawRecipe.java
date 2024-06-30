package com.sebastian.sockets.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record SimpleRawRecipe(Item in, Item out) {

    public static record ItemStackBased(ItemStack in, ItemStack out) {

    }

}