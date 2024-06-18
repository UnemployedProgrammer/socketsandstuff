package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.blockentities.MicrowaveBlockEntity;
import com.sebastian.sockets.blockentities.SocketBlockEntity;
import com.sebastian.sockets.blockentities.ToasterBlockEntity;
import com.sebastian.sockets.blocks.MicrowaveBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Sockets.MODID);

    public static final RegistryObject<BlockEntityType<SocketBlockEntity>> SOCKET_BLOCK_ENTITY =
                BLOCK_ENTITIES.register("socket_entity",
                        () -> BlockEntityType.Builder.of(SocketBlockEntity::new, AllBlocks.SOCKET.get())
                                .build(null));

    public static final RegistryObject<BlockEntityType<ToasterBlockEntity>> TOASTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("toaster_entity",
                    () -> BlockEntityType.Builder.of(ToasterBlockEntity::new, AllBlocks.TOASTER.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<MicrowaveBlockEntity>> MICROWAVE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("microwave_entity",
                    () -> BlockEntityType.Builder.of(MicrowaveBlockEntity::new, AllBlocks.MICROWAVE.get())
                            .build(null));

}
