package com.sebastian.sockets.blocks;

import com.sebastian.sockets.blockentities.SocketBlockEntity;
import com.sebastian.sockets.blockentities.TickableBlockEntity;
import com.sebastian.sockets.math.VoxelUtils;
import com.sebastian.sockets.misc.ConnectionState;
import com.sebastian.sockets.reg.AllBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SocketBlock extends Block implements EntityBlock {

    public SocketBlock(Properties properties) {
        super(properties);
    }

    public static final String CONNECTION_STATE_PREFIX = "error.sockets.socket.";
    public static ChatFormatting getColorForState(ConnectionState state) {
        return state.getError() ? ChatFormatting.RED : ChatFormatting.GREEN;
    }
    public static SoundEvent getSoundForState(ConnectionState state) {
        return state.getError() ? SoundEvents.NOTE_BLOCK_DIDGERIDOO.get() : SoundEvents.NOTE_BLOCK_PLING.get();
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
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pHand == InteractionHand.OFF_HAND || pLevel.isClientSide()) return InteractionResult.FAIL;
        if(pPlayer.isShiftKeyDown()) {
            if(pLevel.getBlockEntity(pPos) instanceof SocketBlockEntity socketBE) {
                ConnectionState breakState = socketBE.breakConnection();
                Component msg = Component.translatable(CONNECTION_STATE_PREFIX + breakState.getName()).withStyle(getColorForState(breakState));
                pPlayer.sendSystemMessage(msg);
                pLevel.playSound(null, pPos, getSoundForState(breakState), SoundSource.BLOCKS);
            }
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.sendSystemMessage(Component.translatable(CONNECTION_STATE_PREFIX + ConnectionState.SHIFT_TO_BREAK.getName()).withStyle(ChatFormatting.RED));
            pLevel.playSound(null, pPos, getSoundForState(ConnectionState.SHIFT_TO_BREAK), SoundSource.BLOCKS);
            return InteractionResult.FAIL;
        }

        //return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return TickableBlockEntity.getTickerHelper(level);
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
