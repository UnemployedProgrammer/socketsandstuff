package com.sebastian.sockets.events;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.math.RandomMath;
import com.sebastian.sockets.misc.BlockUtils;
import com.sebastian.sockets.misc.ItemUtils;
import com.sebastian.sockets.reg.AllEnchantments;
import com.sebastian.sockets.reg.AllParticles;
import com.sebastian.sockets.reg.AllSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
                boolean modified = false;
                for (ItemStack drop : drops) {
                    System.out.println("First Drop: " + drop.getDescriptionId());
                    event.setCanceled(true);
                    sLvl.destroyBlock(event.getPos(), false);
                    ItemStack rawOut = ItemUtils.getSmeltedItemForStack(sLvl, drop);
                    rawOut.setCount(drop.getCount());
                    System.out.println("got out smelt");

                    //Check for Fortune
                    System.out.println(drop.getCount());
                    ItemStack tempStack = new ItemStack(rawOut.getItem());
                    if(enchantments.get(AllEnchantments.MOLTEN_FORTUNE.get()) != null && !modified) {
                        int enchLvl = enchantments.get(AllEnchantments.MOLTEN_FORTUNE.get());
                        float modifier = ItemUtils.getMultiplierForMoltenFortune(enchLvl);
                        float endFloat = modifier * rawOut.getCount();
                        int endCount = Math.round(endFloat);
                        System.out.println("End Count: " + endCount + " | With Modifier: " + modifier + " | So, End Float: " + endFloat + " | Beginner Count: " + rawOut.getCount());
                        tempStack.setCount(endCount);
                    }

                    ItemStack tempStack2 = new ItemStack(tempStack.getItem());
                    tempStack2.setCount(rawOut.getItem() == drop.getItem() ? drop.getCount() : tempStack.getCount());

                    ItemEntity out = new ItemEntity(sLvl, event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, tempStack2);
                    if(!event.getPlayer().isCreative()) {
                        sLvl.addFreshEntity(out);
                        for(int i = 0; i < 360; i++) {
                            if(i % 20 == 0) {
                                sLvl.addParticle(AllParticles.ENERGY_SPARK.get(),
                                        event.getPos().getX() + 0.5d, event.getPos().getY() + 1, event.getPos().getZ() + 0.5d,
                                        Math.cos(i) * 0.15d, 0.15d, Math.sin(i) * 0.15d);
                            }
                        }
                        float randomValue = RandomMath.getRandomFloat(0.8f, 1.5f);
                        sLvl.playSound(null, event.getPos(), AllSounds.ENERGY_ZAP.get(), SoundSource.BLOCKS, 0.1f, randomValue);
                    }
                    rawOut.setCount(1);
                }
            }
        }
    }
}
