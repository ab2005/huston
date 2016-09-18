package com.example.ab.huston;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App extends Application {
    public static final String PORT = "8080";

    private static final String TAG = App.class.getName();
    private static final String COMMAND_FORMAT = "";

    private String cgiFolder = "/system/sbin";
    private String webFolder = "/sdcard/web";
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        Resources res = getResources();
        res.openRawResource(R.raw.websocketd);

        AssetManager am = getAssets();
        AssetFileDescriptor afd = null;
        try {
            afd = am.openNonAssetFd("foo");
            // TODO:
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            public void run() {
                runDaemon();
            }
        }).start();


    }

    private void runDaemon() {
        final String tarCommand = String.format(COMMAND_FORMAT, PORT, cgiFolder, webFolder);

        Process server = null;
        BufferedReader out = null, err = null;
        try {
            server = Runtime.getRuntime().exec(tarCommand);
            err = new BufferedReader(new InputStreamReader(server.getErrorStream()));
            out = new BufferedReader(new InputStreamReader(server.getInputStream()));
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = err.readLine()) != null) {
                errors.append(line + "\n");
            }
            while ((line = out.readLine()) != null) {
                errors.append(line + "\n");
            }
            int res = server.waitFor();
            if (res != 0) {
                throw new IOException(errors.toString());
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        } finally {
            if (server != null) {
                server.destroy();
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

