package com.sebastian.sockets.blockentities;

import com.sebastian.sockets.Config;
import com.sebastian.sockets.blocks.MicrowaveBlock;
import com.sebastian.sockets.customrecipe.RecipeFileStructureBase;
import com.sebastian.sockets.customrecipe.RecipeTypes;
import com.sebastian.sockets.math.RandomMath;
import com.sebastian.sockets.misc.SimpleRawRecipe;
import com.sebastian.sockets.reg.AllBlockEntities;
import com.sebastian.sockets.reg.AllEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MicrowaveBlockEntity extends SocketPluggableEntity {

    int countup;
    boolean recipe;
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            MicrowaveBlockEntity.this.setChanged();
        }
    };
    public final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    public MicrowaveBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AllBlockEntities.MICROWAVE_BLOCK_ENTITY.get(), pPos, pBlockState);
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

    public void useMicrowaveTick() {
        if(getEnergy().getEnergyStored() <= 0) return;
        getEnergy().removeEnergy(1);
        countup++;
        if(countup == 200) useMicrowaveDone();
    }

    public static List<SimpleRawRecipe.ItemStackBased> getRecipesList(List<SimpleRawRecipe.ItemStackBased> starter_or_empty_list_of_recipes) {

        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.BLOCK_FORTUNE, 1)), EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 1))));
        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.BLOCK_FORTUNE, 2)), EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 2))));
        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.BLOCK_FORTUNE, 3)), EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 3))));
        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 3)), EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 4))));
        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 4)), EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 5))));
        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.MOLTEN_FORTUNE.get(), 5)), EnchantedBookItem.createForEnchantment(new EnchantmentInstance(AllEnchantments.OVERHEATING_ENCHANTMENT.get(), 5))));
        starter_or_empty_list_of_recipes.add(new SimpleRawRecipe.ItemStackBased(new ItemStack(Items.POTATO), new ItemStack(Items.BAKED_POTATO)));

        return starter_or_empty_list_of_recipes;
    }

    public static List<Item> boomItems() {
        List<Item> list = new ArrayList<>();
        list.add(Items.IRON_SWORD);
        list.add(Items.IRON_PICKAXE);
        list.add(Items.IRON_AXE);
        list.add(Items.IRON_SHOVEL);
        list.add(Items.IRON_HOE);
        list.add(Items.IRON_HELMET);
        list.add(Items.IRON_CHESTPLATE);
        list.add(Items.IRON_LEGGINGS);
        list.add(Items.IRON_BOOTS);
        list.add(Items.IRON_INGOT);
        list.add(Items.IRON_BLOCK);
        list.add(Items.RAW_IRON);
        list.add(Items.RAW_IRON_BLOCK);
        return list;
    }

    public static ItemStack getOutputItemFromRecipeInput(ItemStack input) {
        for (SimpleRawRecipe.ItemStackBased microwaveRawRecipe : getRecipesList(new ArrayList<>())) {
           if(microwaveRawRecipe.in().copyWithCount(1).equals(input.copyWithCount(1),true)) return microwaveRawRecipe.out().copyWithCount(1);
        }
        return null;
    }

    public static boolean getIsRecipeFromRecipeInput(ItemStack input) {
        for (SimpleRawRecipe.ItemStackBased microwaveRawRecipe : getRecipesList(new ArrayList<>())) {
            if(input.copyWithCount(1).equals(microwaveRawRecipe.in().copyWithCount(1),true)) return true;

            System.out.println(input.copyWithCount(1).equals(microwaveRawRecipe.in().copyWithCount(1),true));
        }
        return false;
    }


    public void useMicrowaveDone() {
        recipe = false;
        countup = 0;
        if(level != null) {
            ItemStack out = getOutputItemFromRecipeInput(getItem().copyWithCount(1));
            if(out == null) return;
            ItemStack itemstackout = out; //Easy for Copying
            if(itemstackout != null) {
                ItemEntity itementity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.1, worldPosition.getZ() + 0.5, itemstackout);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
                setItem(ItemStack.EMPTY);
                sound(false);
                level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos())
                        .setValue(MicrowaveBlock.OPENED, true));
            }
        }
    }

    /* Starts Processing // Returns if Item is Recipe / Start worked
     -- Toaster Start Function -- */
    public boolean useMicrowaveBegin(Item item) {
        if(recipe) return false;
        if(!level.getBlockState(getBlockPos()).getValue(MicrowaveBlock.OPENED)) return false;
        boolean out = false;
        if(getIsRecipeFromRecipeInput(new ItemStack(item))) {
            setItem(new ItemStack(item));
            recipe = true;
            out = true;
            sound(true);
            level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos())
                    .setValue(MicrowaveBlock.OPENED, false));
        } else if (boomItems().contains(item)) {
            if(Config.rangeBoomItems != 0) {
                Explosion explode = level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), Config.rangeBoomItems, Config.fireBoomItems, Level.ExplosionInteraction.TNT);
                explode.finalizeExplosion(true);
                explode.explode();
            }
        }
        return out;
    }

    public void useMicrowaveKill() {
        if(!recipe || getItem().isEmpty()) return;
        ItemEntity itementity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.1, worldPosition.getZ() + 0.5, getItem());
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
        setItem(ItemStack.EMPTY);
        recipe = false;
        countup = 0;
        sound(false);
        level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos())
        .setValue(MicrowaveBlock.OPENED, true));
    }

    public void toggleDoor() {
        if(recipe) return;
        if(level.getBlockState(getBlockPos()).getValue(MicrowaveBlock.OPENED)) {
            level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos())
                    .setValue(MicrowaveBlock.OPENED, false));
            sound(false);
        } else {
            level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos())
                    .setValue(MicrowaveBlock.OPENED, true));
            sound(true);
        }
    }

    public void sound(boolean in) {
        if(level == null) return;
        float randomValue = RandomMath.getRandomFloat(0.9f, 1.1f);
        level.playSound(null, worldPosition, in ? SoundEvents.BAMBOO_WOOD_DOOR_OPEN : SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundSource.BLOCKS, 0.8f, randomValue);
    }

    @Override
    public void tick() {
        if(recipe) {
            useMicrowaveTick();
        }
        super.tick();
    }

    @Override
    public Vec3 getConnectorPos(BlockState state) {
        return new Vec3(0.3,0.1,0.3);
    }

    //TRANSFER SPEED IS GOOD
}
