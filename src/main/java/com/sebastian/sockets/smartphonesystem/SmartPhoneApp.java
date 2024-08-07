package com.sebastian.sockets.smartphonesystem;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.TntBlock;

import java.util.List;

public class SmartPhoneApp {

    SmartPhoneKernel kernel;

    public SmartPhoneApp(SmartPhoneKernel kernel) {
        this.kernel = kernel;
    }

    public List<AbstractWidget> init(List<AbstractWidget> widgets) {
        return widgets;
    }

    public void tick() {

    }

    public void onKeyboard(int pKeyCode, int pScanCode, int pModifiers) {

    }

    public void saveToStorage(String key, String value) {
        if(hasStorageKey(key)) {
            kernel.storage.replace(key, value);
        } else {
            kernel.storage.put(key, value);
        }
    }

    public String getFromStorage(String key) {
        return kernel.storage.get(key);
    }

    public void deleteFromStorage(String key) {
        kernel.storage.remove(key);
    }

    public boolean hasStorageKey(String key) {
        return kernel.storage.containsKey(key);
    }

    public void onMouseScroll(double delta) {

    }

    public void render(UIGraphics guiGraphics) {

    }
}
