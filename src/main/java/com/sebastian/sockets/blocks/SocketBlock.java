package com.sebastian.sockets.blocks;

import com.sebastian.sockets.math.VoxelUtils;
import com.sebastian.sockets.reg.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SocketBlock extends Block implements EntityBlock {

    public SocketBlock(Properties properties) {
        super(properties);
    }

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    private static final VoxelShape NORTH = Block.box(4,3,0, 12,13,2);
    private static final VoxelShape EAST = VoxelUtils.rotateShape(Direction.NORTH, Direction.EAST, NORTH);
    private static final VoxelShape SOUTH = VoxelUtils.rotateShape(Direction.NORTH, Direction.SOUTH, NORTH);
    private static final VoxelShape WEST = VoxelUtils.rotateShape(Direction.NORTH, Direction.WEST, NORTH);
    private static final VoxelShape UP = Block.box(4,14,3, 12, 16, 13);
    private static final VoxelShape DOWN = Block.box(4,0,3,12,2,13);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch (pState.getValue(FACING)) {
            case NORTH -> {
                return NORTH;
            }
            case EAST -> {
                return SOUTH;
            }
            case SOUTH -> {
                return WEST;
            }
            case WEST -> {
                return EAST;
            }
            case UP -> {
                return UP;
            }
            case DOWN -> {
                return DOWN;
            }
            default -> {
                return NORTH;
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction dir = pContext.getNearestLookingDirection();
        return this.defaultBlockState().setValue(FACING, dir);
    }
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return AllBlockEntities.SOCKET_BLOCK_ENTITY.get().create(blockPos, blockState);
    }
}
