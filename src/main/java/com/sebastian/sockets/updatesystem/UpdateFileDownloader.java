package com.sebastian.sockets.updatesystem;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateFileDownloader {
    // Class-level variable to hold the download percentage
    public static int downloadPercentage = 0;
    public static boolean downloads;

    public static CompletableFuture<Void> downloadFileAsync(String fileURL, String destinationPath, int chunkSize, int delayMillis) {
        return CompletableFuture.runAsync(() -> {
            HttpURLConnection httpConn = null;
            try {
                URL url = new URL(fileURL);
                httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");

                    if (disposition != null) {
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10, disposition.length() - 1);
                        }
                    } else {
                        fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
                    }

                    // Get the size of the file
                    int contentLength = httpConn.getContentLength();

                    // Convert content length to Megabits
                    double fileSizeMb = contentLength * 8.0 / (1024 * 1024);

                    InputStream inputStream = httpConn.getInputStream();
                    String saveFilePath = destinationPath + "/" + fileName;

                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                    byte[] buffer = new byte[chunkSize];
                    int bytesRead = -1;
                    int totalBytesRead = 0;
                    long startTime = System.currentTimeMillis();

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        // Calculate download percentage and update the class-level variable
                        downloadPercentage = (int) ((totalBytesRead * 100L) / contentLength);
                        downloads = true;

                        Thread.sleep(delayMillis); // Add delay to slow down the download
                    }

                    long endTime = System.currentTimeMillis();
                    double downloadTimeSeconds = (endTime - startTime) / 1000.0;

                    // Calculate estimated download time at 50 Mbps
                    double downloadSpeedMbps = 50.0; // Mbps
                    double estimatedDownloadTime = fileSizeMb / downloadSpeedMbps;

                    outputStream.close();
                    inputStream.close();
                } else {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            }
        });
    }

    public static void downloadFinally(String fileURL, String destinationPath) {
        int chunkSize = 1024; // Size of each chunk to read
        int delayMillis = 1; // Delay in milliseconds between each chunk read

        CompletableFuture<Void> downloadFuture = downloadFileAsync(fileURL, destinationPath, chunkSize, delayMillis);

        // You can continue doing other tasks while download is in progress
        downloadFuture.thenRun(() -> {
            // After download completes, you can access the downloadPercentage variable
            System.out.println("Final download percentage: " + downloadPercentage + "%");
            downloads = false;
        });
    }
}
