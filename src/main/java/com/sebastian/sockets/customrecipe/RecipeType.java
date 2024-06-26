package com.sebastian.sockets.customrecipe;

import com.google.gson.*;
import com.sebastian.sockets.Sockets;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public record RecipeType(String id) {
    private static Map<ItemStack, ItemStack> inOut_Items = new HashMap<>();

    @Deprecated
    public void addRecipe(ItemStack in, ItemStack out) {
        inOut_Items.put(in, out);
    }

    @Deprecated
    public ItemStack getOutput(ItemStack in) {
        return inOut_Items.get(in);
    }

    @Deprecated
    public Map<ItemStack, ItemStack> getRecipes() {
        return inOut_Items;
    }

    @Deprecated
    public void setRecipes(Map<ItemStack, ItemStack> inOut_Items) {
        this.inOut_Items = inOut_Items;
    }

    @Deprecated
    public static String generateJsonForRecipes(Map<ItemStack, ItemStack> recipes) {
        JsonArray recipesArray = new JsonArray();

        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            JsonObject recipeJson = new JsonObject();
            recipeJson.add("in", Sockets.RECIPEGSON.toJsonTree(entry.getKey()));
            recipeJson.add("out", Sockets.RECIPEGSON.toJsonTree(entry.getValue()));
            recipesArray.add(recipeJson);
        }

        return Sockets.RECIPEGSON.toJson(recipesArray);
    }

    @Deprecated
    public static Map<ItemStack, ItemStack> parseJsonToRecipes(String json) {
        Map<ItemStack, ItemStack> recipes = new HashMap<>();
        try {
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject recipeJson = element.getAsJsonObject();
                ItemStack in = Sockets.RECIPEGSON.fromJson(recipeJson.get("in"), ItemStack.class);
                ItemStack out = Sockets.RECIPEGSON.fromJson(recipeJson.get("out"), ItemStack.class);
                recipes.put(in, out);
            }
        } catch (JsonSyntaxException e) {
            RecipeFileStructureBase.panic("Recipe File Corrupted. File:UNKNOWN");
        }

        return recipes;
    }
}
