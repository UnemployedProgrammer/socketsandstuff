package com.sebastian.sockets.reg;

import com.sebastian.sockets.blocks.MicrowaveBlock;
import com.sebastian.sockets.blocks.SocketBlock;
import com.sebastian.sockets.blocks.ToasterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.sebastian.sockets.Sockets.MODID;

public class AllBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static RegistryObject<Block> SOCKET = registerBlock("socket", () -> new SocketBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion()));
    public static RegistryObject<Block> TOASTER = registerBlock("toaster", () -> new ToasterBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion()));
    public static RegistryObject<Block> MICROWAVE = registerBlock("microwave", () -> new MicrowaveBlock(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return AllItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
