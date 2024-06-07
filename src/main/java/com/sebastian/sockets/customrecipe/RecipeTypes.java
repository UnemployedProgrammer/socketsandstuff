package com.sebastian.sockets.customrecipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RecipeTypes {
    public static RecipeType TOASTER_RECIPE = new RecipeType("toaster_recipe");

    public static void initDefault() {
        TOASTER_RECIPE.addRecipe(new ItemStack(Items.ACACIA_FENCE, 2), new ItemStack(Items.ACACIA_FENCE_GATE, 1));
    }

}
