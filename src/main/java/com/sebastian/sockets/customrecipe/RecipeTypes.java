package com.sebastian.sockets.customrecipe;

import com.sebastian.sockets.reg.AllItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RecipeTypes {
    public static RecipeType TOASTER_RECIPE = new RecipeType("toaster_recipe");

    public static void initDefault() {
        TOASTER_RECIPE.addRecipe(new ItemStack(Items.BREAD, 1), new ItemStack(AllItems.TOASTED_BREAD.get(), 1));
    }

}
