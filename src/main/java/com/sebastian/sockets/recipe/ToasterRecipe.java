package com.sebastian.sockets.recipe;

import com.google.gson.JsonObject;
import com.sebastian.sockets.Sockets;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ToasterRecipe implements Recipe<SimpleContainer> {

    private final Ingredient inputItem;
    private final ItemStack output;
    private final ResourceLocation id;

    public ToasterRecipe(Ingredient inputItem, ItemStack output, ResourceLocation id) {
        this.inputItem = inputItem;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> in = NonNullList.create();
        in.add(inputItem);
        return in;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static class Type implements RecipeType<ToasterRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "toast";
    }

    public static class Serializer implements RecipeSerializer<ToasterRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Sockets.MODID, "toast");

        @Override
        public ToasterRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            JsonObject ingredients = GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient");
            Ingredient inputItem = Ingredient.fromJson(ingredients);
            System.out.println("Parsed " + pRecipeId.toString());
            return new ToasterRecipe(inputItem, output, pRecipeId);
        }

        @Override
        public @Nullable ToasterRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient in = Ingredient.fromNetwork(pBuffer);
            ItemStack output = pBuffer.readItem();
            System.out.println("Net");
            return new ToasterRecipe(in, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ToasterRecipe pRecipe) {
            pRecipe.getInputItem().toNetwork(pBuffer);
            System.out.println("To Net");
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
