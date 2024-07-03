package com.sebastian.sockets.smartphonesystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class UIGraphics {
    private GuiGraphics gg;

    public void drawRectangle(int x, int y, int width, int height, int Color) {
        gg.fill(cutnumber(x, 1), cutnumber(y, 2), cutnumber(x + width, 1), cutnumber(y + height, 2), Color);
    }

    public void text(Component Text, int x, int y, int Color) {
        gg.drawCenteredString(Minecraft.getInstance().font, Text, cutnumber(x, 1), cutnumber(y, 2), Color);
    }
    public void textLeftAlign(Component Text, int x, int y, int Color) {
        gg.drawString(Minecraft.getInstance().font, Text, cutnumber(x, 1), cutnumber(y, 2), Color);
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

}
