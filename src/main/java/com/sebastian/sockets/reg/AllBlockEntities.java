package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.blockentities.SocketBlockEntity;
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

}
