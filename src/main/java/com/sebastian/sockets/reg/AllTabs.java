package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.misc.ItemUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Sockets.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .icon(() -> AllBlocks.SOCKET.get().asItem().getDefaultInstance())
            .title(Component.translatable("itemGroup.sockets.main"))
            .displayItems((parameters, output) -> {
                output.accept(AllBlocks.SOCKET.get().asItem().getDefaultInstance());
                output.accept(AllBlocks.TOASTER.get().asItem().getDefaultInstance());
                output.accept(AllBlocks.MICROWAVE.get().asItem().getDefaultInstance());
                output.accept(AllItems.TOASTED_BREAD.get().getDefaultInstance());
                output.accept(AllItems.WIRE_AND_PLUG.get().getDefaultInstance());
                output.accept(AllItems.SMART_PHONE.get().getDefaultInstance());
                output.accept(ItemUtils.TabEnchantments.OVERHEATING);
                output.accept(ItemUtils.TabEnchantments.MOLTEN_FORTUNE_1);
                output.accept(ItemUtils.TabEnchantments.MOLTEN_FORTUNE_2);
                output.accept(ItemUtils.TabEnchantments.MOLTEN_FORTUNE_3);
                output.accept(ItemUtils.TabEnchantments.MOLTEN_FORTUNE_4);
                output.accept(ItemUtils.TabEnchantments.MOLTEN_FORTUNE_5);
            }).build());

}
