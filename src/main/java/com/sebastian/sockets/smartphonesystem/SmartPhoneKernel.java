package com.sebastian.sockets.smartphonesystem;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartPhoneKernel {
    public int BATTERY_PERCENTAGE = 0;
    public SmartPhoneApp currentApp = new SmartPhoneApp(this);
    public boolean screenon = false;
    public HashMap<String,String> storage = new HashMap<String, String>();
    public SmartPhoneScreen phoneScreen;

    public SmartPhoneKernel() {

    }

    public void launchApplication(SmartPhoneApp app) {
        currentApp = app;
    }

    public int getBatteryPercentage() {
        return BATTERY_PERCENTAGE;
    }

    public void onTurnOn() {
        screenon = true;
    }

    public void removeWidgets() {
        phoneScreen.removeWidgets(collectWidgets(new ArrayList<>()));
    }

    public void onTurnOff() {
        screenon = false;
    }

    public void tick() {
        currentApp.tick();
    }

    public void keyPress(int pKeyCode, int pScanCode, int pModifiers) {
        currentApp.onKeyboard(pKeyCode, pScanCode, pModifiers);
    }

    public void mouseWheelMove(double delta) {
        currentApp.onMouseScroll(delta);
    }

    public static String getGameTime(Level lvl) {
        long dayTicks = lvl.getDayTime();
        // Each Minecraft day is 24000 ticks
        long ticksPerDay = 24000L;

        // Make sure dayTicks is within the range of a single day
        long currentDayTicks = dayTicks % ticksPerDay;

        // Calculate the hours and minutes
        long hours = (currentDayTicks / 1000L);
        long minutes = (currentDayTicks % 1000L) * 60 / 1000L;

        // Format hours and minutes to a 24-hour format string
        return String.format("%02d:%02d", hours, minutes);
    }

    public List<AbstractWidget> collectWidgets(List<AbstractWidget> list) {
        if(getState() == SmartPhoneState.RUNNING) {
            return currentApp.init(list);
        }
        return list;
    }

    public SmartPhoneState getState() {
        if(screenon) {
            return SmartPhoneState.RUNNING;
        } else {
            return SmartPhoneState.OFF;
        }
    }

    public void draw(UIGraphics gg, SmartPhoneState state) {
        switch (state) {
            case RUNNING -> {
                gg.drawRectangle(getMinXApp(),getMinYApp(), getMaxXApp(), 10, 0x303030);
                currentApp.render(gg);
            }
            default -> {
                System.out.println("Draw Nothing");
                gg.drawRectangle(getMinXApp(), getMinYApp(), getMaxXApp(), getMaxYApp(), 0x000000);
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