package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.enchantment.MoltenFortuneEnchantment;
import com.sebastian.sockets.enchantment.OverheatingEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Sockets.MODID);
    public static final RegistryObject<Enchantment> OVERHEATING_ENCHANTMENT = ENCHANTMENTS.register("overheating", () -> new OverheatingEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> MOLTEN_FORTUNE = ENCHANTMENTS.register("molten_fortune", () -> new MoltenFortuneEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));

}
