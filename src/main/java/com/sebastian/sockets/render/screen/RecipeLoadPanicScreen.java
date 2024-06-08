package com.sebastian.sockets.render.screen;

import com.sebastian.sockets.customrecipe.RecipeFileStructureBase;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RecipeLoadPanicScreen extends Screen {

    public Button OK;
    public Button QUIT;
    public Button QUIT_AND_REPORT;

    public RecipeLoadPanicScreen() {
        super(Component.translatable("screen.sockets.error_recipe"));
    }

    @Override
    protected void init() {
        if(minecraft == null) return;

        QUIT_AND_REPORT = addRenderableWidget(Button.builder(Component.translatable("screen_button.sockets.quitreport_error_recipe"), (click) -> {
            Util.getPlatform().openUri("https://github.com/UnemployedProgrammer/socketsandstuff/issues");
            this.minecraft.stop();
        }).bounds(width / 2 - 100, height / 2 + 35, 200, 20).build());

        QUIT = addRenderableWidget(Button.builder(Component.translatable("screen_button.sockets.quit_error_recipe"), (click) -> {
            this.minecraft.stop();
        }).bounds(width / 2 - 100, height / 2 + 60, 200, 20).build());

        OK = addRenderableWidget(Button.builder(Component.translatable("screen_button.sockets.continue_error_recipe"), (click) -> {
            RecipeFileStructureBase.PANICED_OK = true;
            minecraft.popGuiLayer();
        }).bounds(width / 2 - 100, height / 2 + 85, 200, 20).tooltip(Tooltip.create(Component.translatable("screen.sockets.continue_error_recipe"))).build());
        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.translatable("screen.sockets.error_recipe"), width / 2, height / 2 - 20, -1);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.literal(RecipeFileStructureBase.PANIC_MSG), width / 2, height / 2, 0xFF4242);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.translatable("screen.sockets.continue_error_recipe"), width / 2, height / 2 + 20, 0xFF4242);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if(OK != null) OK.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if(QUIT != null) QUIT.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if(QUIT_AND_REPORT != null) QUIT_AND_REPORT.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
