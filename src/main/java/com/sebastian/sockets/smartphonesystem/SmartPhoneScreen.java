package com.sebastian.sockets.smartphonesystem;

import com.sebastian.sockets.Sockets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SmartPhoneScreen extends Screen {

    SmartPhoneKernel smartPhoneKernel;

    public SmartPhoneScreen(SmartPhoneKernel kernel) {
        super(Component.literal("Phone"));
        this.smartPhoneKernel = kernel;
    }

    @Override
    protected void init() {
        smartPhoneKernel.phoneScreen = this;
        for (AbstractWidget collectWidget : smartPhoneKernel.collectWidgets(new ArrayList<>())) {
            addWidget(collectWidget);
        }
        super.init();
    }

    @Override
    public void tick() {
        super.tick();
        smartPhoneKernel.tick();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        smartPhoneKernel.draw(new UIGraphics(pGuiGraphics, pMouseX, pMouseY), smartPhoneKernel.getState());
        pGuiGraphics.blit(new ResourceLocation(Sockets.MODID, "textures/gui/smartphone_background.png"), width / 2 - 40, height / 2 - 64, 0,0,80,128, 80,128);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        smartPhoneKernel.keyPress(pKeyCode, pScanCode, pModifiers);
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        smartPhoneKernel.mouseWheelMove(pDelta);
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    public void removeWidgets(List<AbstractWidget> widgets) {
        for (AbstractWidget widget : widgets) {
            this.removeWidget(widget);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
