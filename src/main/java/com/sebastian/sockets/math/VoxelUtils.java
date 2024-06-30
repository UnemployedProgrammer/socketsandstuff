package com.sebastian.sockets.math;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class VoxelUtils {
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.ordinal() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static class MicrowaveShapes {
        public static VoxelShape open = Shapes.or(
                Block.box(0, 0, 2, 16, 1, 14),
                Block.box(0, 1, 13, 16, 9, 14),
                Block.box(1, 8, 2, 15, 9, 13),
                Block.box(15, 1, 2, 16, 9, 13),
                Block.box(0, 1, 2, 1, 9, 13)
        );
        public static VoxelShape closed = Shapes.or(
                Block.box(0, 0, 2, 16, 1, 14),
                Block.box(0, 1, 13, 16, 9, 14),
                Block.box(1, 8, 2, 15, 9, 13),
                Block.box(15, 1, 2, 16, 9, 13),
                Block.box(0, 1, 2, 1, 9, 13),
                Block.box(1, 1, 2, 15, 8, 3)
        );

        public static VoxelShape OPEN_NORTH = open;
        public static VoxelShape OPEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, open);
        public static VoxelShape OPEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, open);
        public static VoxelShape OPEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, open);

        public static VoxelShape CLOSED_NORTH = closed;
        public static VoxelShape CLOSED_EAST = rotateShape(Direction.NORTH, Direction.EAST, closed);
        public static VoxelShape CLOSED_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, closed);
        public static VoxelShape CLOSED_WEST = rotateShape(Direction.NORTH, Direction.WEST, closed);
    }
}
