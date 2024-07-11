package com.sebastian.sockets.modmarket.loader;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.resource.ResourcePackLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddonResourceLoader {
    private void addResourcePacks() {
        Minecraft minecraft = Minecraft.getInstance();
        File addonResourcesDir = new File(minecraft.gameDirectory, "addonresources");

        if (addonResourcesDir.exists() && addonResourcesDir.isDirectory()) {
            File[] resourcePacks = addonResourcesDir.listFiles((dir, name) -> name.endsWith(".zip"));
            if (resourcePacks != null) {
                for (File resourcePack : resourcePacks) {
                    try {
                        addResourcePack(minecraft, resourcePack);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void addResourcePack(Minecraft minecraft, File resourcePackFile) {

    }
}
