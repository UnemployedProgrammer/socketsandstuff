package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.recipe.ToasterRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Sockets.MODID);

    public static final RegistryObject<RecipeSerializer<ToasterRecipe>> TOASTING_SERIALIZER =
            SERIALIZERS.register("toast", () -> ToasterRecipe.Serializer.INSTANCE);
}
