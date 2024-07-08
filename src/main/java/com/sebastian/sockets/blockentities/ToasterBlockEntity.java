package com.sebastian.sockets.blockentities;

import com.sebastian.sockets.Config;
import com.sebastian.sockets.math.RandomMath;
import com.sebastian.sockets.misc.SimpleRawRecipe;
import com.sebastian.sockets.recipe.ToasterRecipe;
import com.sebastian.sockets.reg.AllBlockEntities;
import com.sebastian.sockets.reg.AllItems;
import com.sebastian.sockets.reg.AllRecipes;
import com.sebastian.sockets.reg.AllSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ToasterBlockEntity extends SocketPluggableEntity {

    int countup;
    boolean recipe;
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ToasterBlockEntity.this.setChanged();
        }
    };
    public final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    public ToasterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AllBlockEntities.TOASTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("countup", countup);
        pTag.putBoolean("recipe", recipe);
        pTag.put("inventory", inventory.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if(nbt.contains("countup")) {
            countup = nbt.getInt("countup");
        }
        if(nbt.contains("recipe")) {
            recipe = nbt.getBoolean("recipe");
        }
        if(nbt.contains("inventory")) {
            inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {

        if (cap == ForgeCapabilities.ENERGY) {
            return this.getEnergyOptional().cast();
        } else if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.optional.cast();
        } else {
            return super.getCapability(cap);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optional.invalidate();
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getItem() {
        return this.inventory.getStackInSlot(0);
    }
    public void setItem(ItemStack itemStack) {
        this.inventory.setStackInSlot(0, itemStack);
    }

    public void useToasterTick() {
        if(getEnergy().getEnergyStored() <= 0) return;
        getEnergy().removeEnergy(1);
        countup++;
        if(countup == 200) useToasterDone();
    }

    public static List<SimpleRawRecipe> getRecipesList(List<SimpleRawRecipe> starter_or_empty_list_of_recipes, Level lvl) {
        //for (Map.Entry<ItemStack, ItemStack> itemStackItemStackEntry : RecipeFileStructureBase.getType(RecipeTypes.TOASTER_RECIPE.id()).getRecipes().entrySet()) {
        //            starter_or_empty_list_of_recipes.add(new ToasterRawRecipe(itemStackItemStackEntry.getKey().getItem(), itemStackItemStackEntry.getValue().getItem()));
        //        }

        //List<ToasterRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ToasterRecipe.Type.INSTANCE);
        //        for (ToasterRecipe recipe : recipes) {
        //            starter_or_empty_list_of_recipes.add(new SimpleRawRecipe(recipe.getInputItem().getItems()[0].getItem(), recipe.getOutput().getItem()));
        //        }

        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe(Items.BREAD, AllItems.TOASTED_BREAD.get()));

        return starter_or_empty_list_of_recipes;
    }

    public static List<Item> boomItems() {
        List<Item> list = new ArrayList<>();
        list.add(Items.IRON_PICKAXE);
        list.add(Items.IRON_INGOT);
        return list;
    }

    public static Item getOutputItemFromRecipeInput(Item input, Level lvl) {
        for (SimpleRawRecipe simpleRawRecipe : getRecipesList(new ArrayList<>(), lvl)) {
           if(simpleRawRecipe.in() == input) return simpleRawRecipe.out();
        }
        return null;
    }

    public static boolean getIsRecipeFromRecipeInput(Item input, Level lvl) {
        for (SimpleRawRecipe simpleRawRecipe : getRecipesList(new ArrayList<>(), lvl)) {
            if(simpleRawRecipe.in() == input) return true;
        }
        return false;
    }


    public void useToasterDone() {
        recipe = false;
        countup = 0;
        if(level != null) {
            Item out = getOutputItemFromRecipeInput(getItem().getItem(), level);
            if(out == null) return;
            ItemStack itemstackout = new ItemStack(out);
            if(itemstackout != null) {
                ItemEntity itementity = new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), itemstackout);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
                setItem(ItemStack.EMPTY);
                sound(false);
            }
        }
    }

    /* Starts Processing // Returns if Item is Recipe / Start worked
     -- Toaster Start Function -- */
    public boolean useToasterBegin(Item item, Entity entity) {
        if(recipe) return false;
        boolean out = false;
        if(getIsRecipeFromRecipeInput(item, level)) {
            setItem(new ItemStack(item));
            recipe = true;
            out = true;
            sound(true);
        } else if (boomItems().contains(item)) {
            if(Config.rangeBoomItems != 0) {
                Explosion explode = level.explode(entity, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), Config.rangeBoomItems, Config.fireBoomItems, Level.ExplosionInteraction.BLOCK);
                explode.finalizeExplosion(true);
                explode.explode();
            }
        }
        return out;
    }

    public void useToasterKill() {
        if(!recipe || getItem().isEmpty()) return;
        ItemEntity itementity = new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), getItem());
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
        setItem(ItemStack.EMPTY);
        recipe = false;
        countup = 0;
        sound(false);
    }

    public void sound(boolean in) {
        if(level == null) return;
        float randomValue = RandomMath.getRandomFloat(0.9f, 1.1f);
        level.playSound(null, worldPosition, in ? AllSounds.TOASTER_POP.get() : AllSounds.TOASTER_IN.get(), SoundSource.BLOCKS, 0.8f, randomValue);
    }

    @Override
    public void tick() {
        if(recipe) {
            useToasterTick();
        }
        super.tick();
    }

    @Override
    public Vec3 getConnectorPos(BlockState state) {
        return new Vec3(0.3,0.1,0.3);
    }

    //TRANSFER SPEED IS GOOD
}
