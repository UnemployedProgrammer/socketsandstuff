package com.sebastian.sockets.updatesystem;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Updater {
    public static void InstallAndDownloadUpdate(String url) {
        String downloadUrl = getDownloadFileURL(url);
        UpdateFileDownloader.downloadFinally(downloadUrl, UpdateFileDownloader.makeReadyToDownload().getPath());
    }

    private static String makeHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("HTTP GET request failed: " + responseCode);
            return null;
        }
    }

    private static String handleApiResponse(String response) {
        String url = "";
        if (response != null) {
            JsonObject jsonObject = parseJson(response);
            System.out.println(jsonObject.toString());
            if (jsonObject != null) {
                JsonArray files = jsonObject.getAsJsonArray("files");
                if (files != null && files.size() > 0) {
                    JsonObject file = files.get(0).getAsJsonObject();
                    url = file.get("url").getAsString();
                    System.out.println("URL of the first file: " + url);
                } else {
                    System.out.println("No files found in the response");
                }
            } else {
                System.out.println("Failed to parse JSON");
            }
        } else {
            System.out.println("Failed to fetch data from API");
        }
        return url;
    }

    private static JsonObject parseJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JsonObject.class);
    }

    private static String getDownloadFileURL(String url) {
        AtomicReference<String> returnv = new AtomicReference<>("");

        CompletableFuture.supplyAsync(() -> {
            try {
                return makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).thenAcceptAsync(response -> {
            returnv.set(handleApiResponse(response));
        }).join(); // Ensure main thread waits for CompletableFuture to complete

        return returnv.get();
    }
}
