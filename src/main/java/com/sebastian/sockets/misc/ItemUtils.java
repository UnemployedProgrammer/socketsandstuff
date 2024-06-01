package com.sebastian.sockets.misc;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.Random;

public class ItemUtils {
    public static ItemStack getSmeltedItemForStack(Level lvl, ItemStack stack) {
        Optional<SmeltingRecipe> recipeFor = lvl.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), lvl);
        if(!recipeFor.isPresent()) return stack;
        ItemStack out = recipeFor.get().getResultItem(RegistryAccess.EMPTY);
        return out; //output of furnace recipe;
    }

    public static float getMultiplierForMoltenFortune(int lvl) {
        Random random = new Random();
        float min = lvl * 0.7f;
        float max = lvl * 1.3333f;
        float randVal = min + random.nextFloat() * (max - min);
        float outgoing = Math.max(min, Math.min(randVal, max));
        System.out.println("Outgoing:" + outgoing);
        return outgoing;
    }
}
