package com.sebastian.sockets.events;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.misc.BlockUtils;
import com.sebastian.sockets.misc.ItemUtils;
import com.sebastian.sockets.reg.AllEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Sockets.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        System.out.println("Block Broken!");
        if(event.getPlayer() == null) return;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND));
        if(enchantments == null) return;
        if(enchantments.get(AllEnchantments.OVERHEATING_ENCHANTMENT.get()) != null) {
            System.out.println("Has Enchantment!");
            if(event.getPlayer().level() instanceof ServerLevel sLvl) {
                ItemStack[] drops = BlockUtils.getBlockDrops(sLvl, event.getPos());
                List<ItemStack> outStack = new ArrayList<>();
                for (ItemStack drop : drops) {
                    System.out.println("First Drop: " + drop.getDescriptionId());
                    event.setCanceled(true);
                    sLvl.destroyBlock(event.getPos(), false);
                    ItemStack rawOut = ItemUtils.getSmeltedItemForStack(sLvl, drop);
                    ItemEntity out = new ItemEntity(sLvl, event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, rawOut);
                    sLvl.addFreshEntity(out);
                }
            }
        }
    }
}
