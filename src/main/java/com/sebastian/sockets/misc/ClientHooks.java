package com.sebastian.sockets.misc;

import com.sebastian.sockets.smartphonesystem.SmartPhoneKernel;
import com.sebastian.sockets.smartphonesystem.SmartPhoneScreen;
import net.minecraft.client.Minecraft;

public class ClientHooks {
    public static void openSmartPhoneScreen(SmartPhoneKernel kernel) {
        Minecraft.getInstance().setScreen(new SmartPhoneScreen(kernel));
    }
}
