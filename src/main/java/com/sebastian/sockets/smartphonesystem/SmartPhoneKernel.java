package com.sebastian.sockets.smartphonesystem;

import net.minecraft.client.gui.GuiGraphics;

public class SmartPhoneKernel {
    public static int BATTERY_PERCENTAGE = 0;
    public static SmartPhoneApp currentApp;
    public static boolean screenon = false;

    public static void launchApplication(SmartPhoneApp app) {
        currentApp = app;
    }

    public static int getBatteryPercentage() {
        return BATTERY_PERCENTAGE;
    }

    public void onTurnOn() {

    }

    public void onTurnOff() {

    }

    public void getGameTime(boolean hour24clock) {

    }

    public void draw(UIGraphics gg, SmartPhoneState state) {
        switch (state) {
            case OFF -> {

            }
        }
    }

    public static int getMinXApp() {
        return 10;
    }
    public static int getMaxXApp() {
        return 100;
    }
    public static int getMinYApp() {
        return 10;
    }
    public static int getMaxYApp() {
        return 100;
    }
}