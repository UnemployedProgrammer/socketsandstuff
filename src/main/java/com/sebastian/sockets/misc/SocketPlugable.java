package com.sebastian.sockets.misc;

import net.minecraft.world.phys.Vec3;

public interface SocketPlugable {
    public Vec3 getConnectorPos();
    public int maxTransferCapacity();
}
