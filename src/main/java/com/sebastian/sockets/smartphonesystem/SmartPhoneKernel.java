package com.sebastian.sockets.smartphonesystem;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.server.commands.TimeCommand;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;

public class SmartPhoneKernel {
    public int BATTERY_PERCENTAGE = 0;
    public SmartPhoneApp currentApp;
    public boolean screenon = false;
    public HashMap<String,String> storage = new HashMap<String, String>();

    public void launchApplication(SmartPhoneApp app) {
        currentApp = app;
    }

    public int getBatteryPercentage() {
        return BATTERY_PERCENTAGE;
    }

    public void onTurnOn() {
        screenon = true;
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

    public String getGameTime(Level lvl) {
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
        List<AbstractWidget> listAfterApp = currentApp.init(list);
        return listAfterApp;
    }

    public SmartPhoneState getState() {
       if(screenon) return SmartPhoneState.OFF; else return SmartPhoneState.RUNNING;
    }

    public void draw(UIGraphics gg, SmartPhoneState state) {
        switch (state) {
            case RUNNING -> {
                gg.drawRectangle(getMinXApp(),getMinYApp(), getMaxXApp(), 10, 0x303030);
                currentApp.render(gg);
            }
            default -> {
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