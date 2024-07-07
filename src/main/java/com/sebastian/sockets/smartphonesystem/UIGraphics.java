package com.sebastian.sockets.smartphonesystem;

import jdk.incubator.vector.VectorOperators;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class UIGraphics {
    private GuiGraphics gg;
    private int mX;
    private int mY;

    public UIGraphics(GuiGraphics gg, int mouseX, int mouseY) {
        this.gg = gg;
        this.mX = mouseX;
        this.mY = mouseY;
    }

    public void drawRectangle(int x, int y, int width, int height, int Color) {
        gg.fill(cutnumber(x, 1), cutnumber(y, 2), cutnumber(x + width, 1), cutnumber(y + height, 2), Color);
    }

    public void text(Component Text, int x, int y, int Color) {
        gg.drawCenteredString(Minecraft.getInstance().font, Text, cutnumber(x, 1), cutnumber(y, 2), Color);
    }
    public void textLeftAlign(Component Text, int x, int y, int Color) {
        gg.drawString(Minecraft.getInstance().font, Text, cutnumber(x, 1), cutnumber(y, 2), Color);
    }

    public void blit(ResourceLocation pAtlasLocation, int pX, int pY, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        gg.blit(pAtlasLocation, cutnumber(pX, 1), cutnumber(pY, 2), pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }

    //STATE:
    //1: X COORD
    //2: Y COORD
    public int cutnumber(int num, int state) {
        if(state == 1) {
            return Math.max(SmartPhoneKernel.getMinXApp(), Math.min(SmartPhoneKernel.getMaxXApp(), num));
        }
        if(state == 2) {
            return Math.max(SmartPhoneKernel.getMinYApp(), Math.min(SmartPhoneKernel.getMaxYApp(), num));
        }
        return 1000000000;
    }

    public int getMouseX() {
        return mX;
    }

    public int getMouseY() {
        return mY;
    }
}
