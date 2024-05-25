package com.sebastian.sockets.reg;

import com.sebastian.sockets.Sockets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Sockets.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> AllBlocks.SOCKET.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                //output.accept(item);
                output.accept(AllBlocks.SOCKET.get().asItem().getDefaultInstance());
            }).build());

}
