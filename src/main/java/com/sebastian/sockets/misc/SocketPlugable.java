package com.sebastian.sockets.misc;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface SocketPlugable {
    public Vec3 getConnectorPos(BlockState state);
    public int maxTransferCapacity();
}
