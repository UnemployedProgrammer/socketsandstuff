package com.sebastian.sockets.customrecipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sebastian.sockets.Sockets;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RecipeType(String id) {
    private static Map<ItemStack, ItemStack> inOut_Items = new HashMap<>();

    public void addRecipe(ItemStack in, ItemStack out) {
        inOut_Items.put(in, out);
    }

    public ItemStack getOutput(ItemStack in) {
        return inOut_Items.get(in);
    }

    public Map<ItemStack, ItemStack> getRecipes() {
        return inOut_Items;
    }

    public void setRecipes(Map<ItemStack, ItemStack> inOut_Items) {
        this.inOut_Items = inOut_Items;
    }

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

    public static Map<ItemStack, ItemStack> parseJsonToRecipes(String json) {
        Map<ItemStack, ItemStack> recipes = new HashMap<>();
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            JsonObject recipeJson = element.getAsJsonObject();
            ItemStack in = Sockets.RECIPEGSON.fromJson(recipeJson.get("in"), ItemStack.class);
            ItemStack out = Sockets.RECIPEGSON.fromJson(recipeJson.get("out"), ItemStack.class);
            recipes.put(in, out);
        }

        return recipes;
    }
}
