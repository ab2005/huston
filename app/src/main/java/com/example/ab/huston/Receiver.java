package com.example.ab.huston;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * A Receiver that can kill process.
 */
public class Receiver extends BroadcastReceiver {
    private static final String TAG = Receiver.class.getSimpleName();
    private boolean sStarted;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "start":
                if (!sStarted) {
                    sStarted = true;
                    Log.d(TAG, "Starting process");
                } else {
                    Log.d(TAG, "Process already started");
                }
                break;
            case "stop":
                Log.d(TAG, "Terminating process");
                System.exit(0);
        }
    }
}
