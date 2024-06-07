package com.sebastian.sockets.misc;

import com.sebastian.sockets.reg.AllEnchantments;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
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
    public static class TabEnchantments {
        public static ItemStack OVERHEATING = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.OVERHEATING_ENCHANTMENT.get(), 1));
        public static ItemStack MOLTEN_FORTUNE_1 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 1));
        public static ItemStack MOLTEN_FORTUNE_2 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 2));
        public static ItemStack MOLTEN_FORTUNE_3 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 3));
        public static ItemStack MOLTEN_FORTUNE_4 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 4));
        public static ItemStack MOLTEN_FORTUNE_5 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 5));
    }
}
