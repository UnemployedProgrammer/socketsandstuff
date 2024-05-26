package com.sebastian.sockets.items;

import com.sebastian.sockets.blockentities.SocketBlockEntity;
import com.sebastian.sockets.blockentities.SocketPluggableEntity;
import com.sebastian.sockets.blocks.SocketBlock;
import com.sebastian.sockets.misc.ConnectionState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class WireAndPlug extends Item {
    public WireAndPlug(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getHand() == InteractionHand.OFF_HAND || pContext.getLevel().isClientSide()) return InteractionResult.FAIL;
        if(hasFirstPos(pContext)) {
            connect(pContext);
        } else {
            saveFirstPos(pContext);
        }
        return InteractionResult.SUCCESS;
    }

    public void saveFirstPos(UseOnContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        ctx.getItemInHand().getOrCreateTag().putInt("first_x", pos.getX());
        ctx.getItemInHand().getOrCreateTag().putInt("first_y", pos.getY());
        ctx.getItemInHand().getOrCreateTag().putInt("first_z", pos.getZ());
        ctx.getItemInHand().getOrCreateTag().putBoolean("first", true);
        Component msg = Component.translatable(SocketBlock.CONNECTION_STATE_PREFIX + ConnectionState.FIRST_POS_SET.getName()).withStyle(SocketBlock.getColorForState(ConnectionState.FIRST_POS_SET));
        ctx.getPlayer().sendSystemMessage(msg);
        ctx.getLevel().playSound(null, ctx.getClickedPos(), SocketBlock.getSoundForState(ConnectionState.FIRST_POS_SET), SoundSource.BLOCKS);
    }

    public BlockPos getFirstPos(UseOnContext ctx) {
        CompoundTag tag = ctx.getItemInHand().getOrCreateTag();
        int x = tag.contains("first_x") ? tag.getInt("first_x") : 0;
        int y = tag.contains("first_y") ? tag.getInt("first_y") : 0;
        int z = tag.contains("first_z") ? tag.getInt("first_z") : 0;
        return new BlockPos(x, y, z);
    }

    public boolean hasFirstPos(UseOnContext ctx) {
        if (ctx.getItemInHand().getOrCreateTag().contains("first"))
            return ctx.getItemInHand().getOrCreateTag().getBoolean("first");
        return false;
    }

    public void connect(UseOnContext ctx) {
        if(!hasFirstPos(ctx)) return;
        BlockPos firstPos = getFirstPos(ctx);
        if(ctx.getLevel().getBlockEntity(firstPos) instanceof SocketBlockEntity socketBlockEntity) {
            ConnectionState connectionState = socketBlockEntity.setConnected(ctx.getClickedPos().getX(), ctx.getClickedPos().getY(), ctx.getClickedPos().getZ());
            Component msg = Component.translatable(SocketBlock.CONNECTION_STATE_PREFIX + connectionState.getName()).withStyle(SocketBlock.getColorForState(connectionState));
            ctx.getPlayer().sendSystemMessage(msg);
            ctx.getLevel().playSound(null, ctx.getClickedPos(), SocketBlock.getSoundForState(connectionState), SoundSource.BLOCKS);
            ctx.getItemInHand().getOrCreateTag().putBoolean("first", false);
        } else {
            ConnectionState connectionState = ConnectionState.BE_NOT_FOUND;
            Component msg = Component.translatable(SocketBlock.CONNECTION_STATE_PREFIX + connectionState.getName()).withStyle(SocketBlock.getColorForState(connectionState));
            ctx.getPlayer().sendSystemMessage(msg);
            ctx.getLevel().playSound(null, ctx.getClickedPos(), SocketBlock.getSoundForState(connectionState), SoundSource.BLOCKS);
            ctx.getItemInHand().getOrCreateTag().putBoolean("first", false);
        }
    }
}
