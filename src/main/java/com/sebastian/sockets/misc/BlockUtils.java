package com.sebastian.sockets.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
    public static ItemStack[] getBlockDrops(final ServerLevel w, final BlockPos pos )
    {
        List<ItemStack> out = new ArrayList<>();
        final BlockState state = w.getBlockState(pos);
        if( state != null )
        {
            out = state.getBlock().getDrops( state, w, pos, null );
        }
        if( out == null )
        {
            return new ItemStack[0];
        }
        return out.toArray( new ItemStack[out.size()] );
    }
}
