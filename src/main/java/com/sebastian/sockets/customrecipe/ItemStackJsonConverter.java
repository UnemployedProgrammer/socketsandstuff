package com.sebastian.sockets.customrecipe;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

@Deprecated
public class ItemStackJsonConverter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public JsonElement serialize(ItemStack stack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        jsonObject.addProperty("count", stack.getCount());
        jsonObject.addProperty("nbt", stack.getOrCreateTag().toString());
        return jsonObject;
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String itemName = jsonObject.get("item").getAsString();
        String NBT = jsonObject.get("nbt").getAsString();
        int count = jsonObject.get("count").getAsInt();

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

        if (!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName))) {
            RecipeFileStructureBase.panic("No such item while reading in (caught).");
            item = Items.BEDROCK;
        }

         // Get the item from the registry
        if (item == null) {
            RecipeFileStructureBase.panic("No such item while reading in.");
        }
        ItemStack stack = new ItemStack(item, count);
        try {
            stack.setTag(TagParser.parseTag(NBT));
        } catch (CommandSyntaxException e) {
            RecipeFileStructureBase.panic("Nbt is Corrupt or Failed to load. ItemRecipe(as Tip): " + itemName);
        }
        return new ItemStack(item, count);
    }
}
