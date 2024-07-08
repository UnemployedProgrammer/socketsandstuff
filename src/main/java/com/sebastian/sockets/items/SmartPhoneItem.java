package com.sebastian.sockets.items;

import com.sebastian.sockets.misc.ClientHooks;
import com.sebastian.sockets.smartphonesystem.SmartPhoneKernel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SmartPhoneItem extends Item {
    public SmartPhoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSmartPhoneScreen(new SmartPhoneKernel()));
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pLevel != null) {
            pTooltipComponents.add(Component.literal("Me: Nothing here yet. More will come soon! :)"));
            pTooltipComponents.add(Component.literal("Phone: Bro, give me any functionality at least for now bro! :("));
            pTooltipComponents.add(Component.literal("Me: Yeah, ok. I'll give you day time."));
            pTooltipComponents.add(Component.literal("Phone: Thanks Bro!"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal(SmartPhoneKernel.getGameTime(pLevel)));
        } else {
            pTooltipComponents.add(Component.literal("Nothing here yet. More will come soon! :)"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
