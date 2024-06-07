package com.sebastian.sockets.events;

import com.sebastian.sockets.customrecipe.RecipeFileStructureBase;
import com.sebastian.sockets.render.screen.RecipeLoadPanicScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventBus {
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init event) {
        Screen gui = event.getScreen();

        if(gui instanceof TitleScreen) {
            if(RecipeFileStructureBase.PANICED && !RecipeFileStructureBase.PANICED_OK) {
                Minecraft.getInstance().setScreen(new RecipeLoadPanicScreen());
            }
        }
    }
}
