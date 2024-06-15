package com.sebastian.sockets.render.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AfterUpdateRestartGameScreen extends Screen {

    Button btn;

    public AfterUpdateRestartGameScreen() {
        super(Component.translatable("update.sockets.restart"));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderDirtBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.drawCenteredString(minecraft.font, Component.translatable("update.sockets.restart"), width / 2, 10, 0xFFFFFF);
    }

    @Override
    protected void init() {
        super.init();
        btn = addRenderableWidget(Button.builder(Component.translatable("update.sockets.restart_close").append(Component.translatable("update.sockets.restart_close_manually").withStyle(ChatFormatting.UNDERLINE)), (click) -> {
            Minecraft.getInstance().stop();
        }).bounds(width / 2 - 100, height / 2, 200, 20).build());
    }
}
