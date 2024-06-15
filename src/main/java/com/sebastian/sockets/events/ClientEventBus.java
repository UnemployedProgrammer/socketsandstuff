package com.sebastian.sockets.events;

import com.sebastian.sockets.customrecipe.RecipeFileStructureBase;
import com.sebastian.sockets.render.screen.RecipeLoadPanicScreen;
import com.sebastian.sockets.render.screen.UpdateAvailablePopupScreen;
import com.sebastian.sockets.updatesystem.CheckForVersion;
import com.sebastian.sockets.updatesystem.UpdateFileDownloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventBus {
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init event) {
        Screen gui = event.getScreen();

        if(gui instanceof TitleScreen) {
            CheckForVersion.checkGoogleAppsScrServersForModVersion();
            CheckForVersion.CHECKED = true;
            if(RecipeFileStructureBase.PANICED && !RecipeFileStructureBase.PANICED_OK) {
                Minecraft.getInstance().setScreen(new RecipeLoadPanicScreen());
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(UpdateFileDownloader.downloads) {
            System.out.println("Download");
            System.out.println("Update");
            System.out.println("Download: " + UpdateFileDownloader.downloadPercentage + "%");
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.literal("Download: " + UpdateFileDownloader.downloadPercentage + "%"), (Component)null);
        }
    }
}
