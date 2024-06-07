package com.sebastian.sockets.blockentities;

import com.sebastian.sockets.customrecipe.RecipeTypes;
import com.sebastian.sockets.math.RandomMath;
import com.sebastian.sockets.misc.ToasterRawRecipe;
import com.sebastian.sockets.reg.AllBlockEntities;
import com.sebastian.sockets.reg.AllItems;
import com.sebastian.sockets.reg.AllSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static List<ToasterRawRecipe> getRecipesList(List<ToasterRawRecipe> starter_or_empty_list_of_recipes) {
        return starter_or_empty_list_of_recipes;
    }

    public static Item getOutputItemFromRecipeInput(Item input) {
        for (ToasterRawRecipe toasterRawRecipe : getRecipesList(new ArrayList<>())) {
           if(toasterRawRecipe.in() == input) return toasterRawRecipe.out();
        }
        return null;
    }

    public static boolean getIsRecipeFromRecipeInput(Item input) {
        for (ToasterRawRecipe toasterRawRecipe : getRecipesList(new ArrayList<>())) {
            if(toasterRawRecipe.in() == input) return true;
        }
        return false;
    }


    public void useToasterDone() {
        recipe = false;
        countup = 0;
        if(level != null) {
            Item out = getOutputItemFromRecipeInput(getItem().getItem());
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
    public boolean useToasterBegin(Item item) {
        if(recipe) return false;
        boolean out = false;
        if(getIsRecipeFromRecipeInput(item)) {
            setItem(new ItemStack(item));
            recipe = true;
            out = true;
            sound(true);
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
