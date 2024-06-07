package com.sebastian.sockets.customrecipe;

import com.google.gson.JsonObject;
import com.sebastian.sockets.Sockets;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RecipeFileStructureBase {
    public static File CONFIGDIR = FMLPaths.CONFIGDIR.get().toFile();
    public static List<RecipeType> recipeTypes = new ArrayList<>();
    public static Boolean PANICED = false;
    public static Boolean PANICED_OK = false;
    public static String PANIC_MSG = "{RECIPE/REG:SOCKETS/PANIC}~Unknown";

    public static void panic(String str) {
        Sockets.LOGGER.error("{RECIPE/REG:SOCKETS/PANIC}~"+str);
        PANICED = true;
        PANIC_MSG = "{RECIPE/REG:SOCKETS/PANIC}~"+str;
    }

    /////////////////////////////////////////////////

    public static RecipeType getType(String id) {
        RecipeType crnt = null;
        for (RecipeType recipeType : recipeTypes) {
            if(recipeType.id() == id) crnt = recipeType;
        }
        return crnt;
    }

    public static void addRecipeType(RecipeType type) {
        recipeTypes.add(type);
    }

    ////////////////////////////////////////////////////////

    public static void checkOrGenerateFolders() {
        if(CONFIGDIR.exists()) {
            try {
                File RECIPEFOLDER = new File(CONFIGDIR, "sockets_recipes");
                if(RECIPEFOLDER.mkdir()) {
                    Sockets.LOGGER.debug("Sockets Recipe Dir Created!");
                } else {
                    Sockets.LOGGER.debug("Sockets Recipe Dir Found!");
                }
                for (RecipeType recipeType : recipeTypes) {
                    File type = new File(RECIPEFOLDER, recipeType.id()+".json");
                    if(type.createNewFile()) {
                        FileWriter myWriter = new FileWriter(type);
                        myWriter.write(jsonEncode(recipeType));
                        myWriter.close();
                    } else {
                        try {
                            File myObj = new File(RECIPEFOLDER, recipeType.id()+".json");
                            Scanner myReader = new Scanner(myObj);
                            String data = "";
                            while (myReader.hasNextLine()) {
                                data = data+myReader.nextLine();
                                System.out.println(data);
                            }
                            getType(recipeType.id()).setRecipes(jsonDecode(data, recipeType).getRecipes());
                            myReader.close();
                        } catch (FileNotFoundException e) {
                            panic(e.getLocalizedMessage());
                        }
                    }
                    for (Map.Entry<ItemStack, ItemStack> itemStackItemStackEntry : RecipeFileStructureBase.getType(recipeType.id()).getRecipes().entrySet()) {
                        Sockets.LOGGER.debug("Found " + recipeType.id() + " recipe: in- " + ForgeRegistries.ITEMS.getKey(itemStackItemStackEntry.getKey().getItem()).toString() + " , out-  " + ForgeRegistries.ITEMS.getKey(itemStackItemStackEntry.getValue().getItem()).toString());
                    }
                }
            } catch (IOException e) {
                panic(e.getMessage());
            }
        } else {
            panic("No FMLPaths.CONFIGDIR found existing. Try Restarting!");
        }
    }

    public static String jsonEncode(RecipeType type) {
        if(type.id() == "toaster_recipe") {
            String out =  RecipeType.generateJsonForRecipes(type.getRecipes());
            Sockets.LOGGER.debug("Encoded Toaster Recipe: " + out);
            return out;
        }
        return "{}";
    }

    public static RecipeType jsonDecode(String json, RecipeType type) {
        if(type.id() == "toaster_recipe") {
            type.setRecipes(RecipeType.parseJsonToRecipes(json));
        }
        return type;
    }

    public static String generateJsonForRecipe(ItemStack in, ItemStack out) {
        JsonObject recipeJson = new JsonObject();
        recipeJson.add("input", Sockets.RECIPEGSON.toJsonTree(in));
        recipeJson.add("output", Sockets.RECIPEGSON.toJsonTree(out));
        return Sockets.RECIPEGSON.toJson(recipeJson);
    }
}
