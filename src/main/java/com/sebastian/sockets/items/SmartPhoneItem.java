package com.sebastian.sockets.items;

import com.sebastian.sockets.misc.ClientHooks;
import com.sebastian.sockets.smartphonesystem.SmartPhoneKernel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class SmartPhoneItem extends Item {
    public SmartPhoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSmartPhoneScreen(new SmartPhoneKernel()));
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
