package com.sebastian.sockets.render.screen;

import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.updatesystem.UpdateFileDownloader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLPaths;

public class UpdateAvailablePopupScreen extends Screen {

    public static boolean isOpen = false;

    public ImageWidget background;
    public Button Ignore;
    public Button Download;
    public Button ViewChangelog;
    public String newVer;

    public UpdateAvailablePopupScreen(String updateVer) {
        super(Component.translatable("screen.sockets.update_available"));
        this.newVer = updateVer;
    }

    @Override
    public void init() {
        if(minecraft == null) return;

        background = addRenderableOnly(new ImageWidget(width / 2 - 66, height / 2 - 80, 132, 160, new ResourceLocation(Sockets.MODID, "textures/gui/bc_upd.png")));

        Download = addRenderableWidget(Button.builder(Component.translatable("screen_button.sockets.download_update_available"), (btn) -> {
            Sockets.LOGGER.debug("Starting Download...");
            ToastComponent toastcomponent = Minecraft.getInstance().getToasts();
            SystemToast.addOrUpdate(toastcomponent, SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable("update.sockets.start"), (Component)null);
            UpdateFileDownloader.downloadFinally("https://cdn.modrinth.com/data/LNytGWDc/versions/HNYrbfZZ/create-1.20.1-0.5.1.f.jar", FMLPaths.GAMEDIR.get().toAbsolutePath().toString());
            minecraft.popGuiLayer();
        }).bounds(width / 2 - 60, height / 2 + 10 , 120, 20).build());

        ViewChangelog = addRenderableWidget(Button.builder(Component.translatable("screen_button.sockets.changelog_update_available"), (btn) -> {
            Util.getPlatform().openUri("https://modrinth.com/mod/sockets-devices/changelog");
        }).bounds(width / 2 - 60, height / 2 + 32 , 120, 20).build());

        Ignore = addRenderableWidget(Button.builder(Component.translatable("screen_button.sockets.ignore_update_available"), (btn) -> {
            minecraft.popGuiLayer();
        }).bounds(width / 2 - 60, height / 2 + 54, 120, 20).build());

        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, pGuiGraphics));
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        drawCenteredStringWithoutDropShadow(pGuiGraphics, minecraft.font, Component.translatable("screen.sockets.update_available"), width / 2, height / 2 - 75, 0x3C3C3C);
        drawCenteredStringWithoutDropShadow(pGuiGraphics, minecraft.font, Component.translatable("itemGroup.sockets.main"), width / 2, height / 2 - 60, 0x3C3C3C);
        drawCenteredStringWithoutDropShadow(pGuiGraphics, minecraft.font, Component.literal("v"+newVer), width / 2, height / 2 - 50, 0x3C3C3C);
    }

    public static void drawCenteredStringWithoutDropShadow(GuiGraphics gg, Font pFont, Component pText, int pX, int pY, int pColor) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        gg.drawString(pFont, formattedcharsequence, pX - pFont.width(formattedcharsequence) / 2, pY, pColor, false);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
