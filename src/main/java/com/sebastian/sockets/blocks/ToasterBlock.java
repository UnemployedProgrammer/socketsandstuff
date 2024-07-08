package com.sebastian.sockets.blocks;

import com.sebastian.sockets.blockentities.TickableBlockEntity;
import com.sebastian.sockets.blockentities.ToasterBlockEntity;
import com.sebastian.sockets.math.VoxelUtils;
import com.sebastian.sockets.misc.SocketPlugable;
import com.sebastian.sockets.reg.AllBlockEntities;
import com.sebastian.sockets.reg.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToasterBlock extends Block implements EntityBlock {
    public ToasterBlock(Properties pProperties) {
        super(pProperties);
    }

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    private static final VoxelShape NORTH = Block.box(2, 0, 4, 14, 8, 11);
    private static final VoxelShape EAST = VoxelUtils.rotateShape(Direction.NORTH, Direction.SOUTH, NORTH);
    private static final VoxelShape SOUTH = Block.box(2, 0, 5, 14, 8, 12);
    private static final VoxelShape WEST = VoxelUtils.rotateShape(Direction.SOUTH, Direction.EAST, SOUTH);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch (pState.getValue(FACING)) {
            case NORTH -> {
                return NORTH;
            }
            case EAST -> {
                return EAST;
            }
            case SOUTH -> {
                return SOUTH;
            }
            case WEST -> {
                return WEST;
            }
            default -> {
                return NORTH;
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide() || pHand == InteractionHand.OFF_HAND) return InteractionResult.CONSUME;
        if(pPlayer.getItemInHand(pHand).is(AllItems.WIRE_AND_PLUG.get())) return InteractionResult.PASS;
        if(pLevel.getBlockEntity(pPos) instanceof ToasterBlockEntity bE) {
            if(pPlayer.isShiftKeyDown()) {
                bE.useToasterKill();
            } else {
                if(bE.useToasterBegin(pPlayer.getItemInHand(pHand).getItem(), pPlayer)) {
                    pPlayer.getItemInHand(pHand).shrink(1);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return TickableBlockEntity.getTickerHelper(level);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction dir = pContext.getHorizontalDirection();
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
        return AllBlockEntities.TOASTER_BLOCK_ENTITY.get().create(blockPos, blockState);
    }
}