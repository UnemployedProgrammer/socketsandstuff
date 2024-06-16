package com.sebastian.sockets.updatesystem;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import com.sebastian.sockets.Sockets;
import com.sebastian.sockets.render.screen.UpdateAvailablePopupScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class CheckForVersion {
    public static record VersionCheckerCollection(boolean isUpToDate, String newestVersionFileUrl) {}

    public static Boolean CHECKED = false;

    public static class VersionCheck {
        public String modversion;
        public String mrhversion;
    }

    public static void checkGoogleAppsScrServersForModVersion() {
        if(CHECKED) return;
        CHECKED = true;
        if(UpdateAvailablePopupScreen.isOpen) return;
        try {
            CompletableFuture<VersionCheck> future = getVersionAsync();
            future.thenAccept(version -> {
                try {
                    if (version == null) return;
                    System.out.println(version.modversion);
                    if(!version.modversion.equals(Sockets.MODVERS)) {
                        Minecraft.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                UpdateAvailablePopupScreen.isOpen = true;
                                CHECKED = true;
                                Minecraft.getInstance().pushGuiLayer(new UpdateAvailablePopupScreen(version.modversion, version.mrhversion));
                                Sockets.LOGGER.debug("Mod isn't UpToDate!");
                            }
                        });
                    } else {
                        Minecraft.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                CHECKED = true;
                                Sockets.LOGGER.debug("Mod is UpToDate!");
                            }
                        });
                    }
                } catch (Exception e) {
                    LogUtils.getLogger().warn(e.getMessage());
                }
            });
        } catch (Exception e) {
            LogUtils.getLogger().warn(e.getMessage());
        }
    }

    public static VersionCheck getVersion() {
        VersionCheck json = null;
        try {
            URL url = new URL(UrlConstants.getApiRequestUrlForVersion());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String res = response.toString();
                Sockets.LOGGER.debug("Got UPDATE Response: " + res);

                json = new Gson().fromJson(res, VersionCheck.class);
            } else {
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static CompletableFuture<VersionCheck> getVersionAsync() {
        return CompletableFuture.supplyAsync(() -> {
            VersionCheck json = null;
            try {
                json = getVersion();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        });
    }
}
