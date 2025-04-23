package com.example.pbjclicker;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class PassiveIncomeService extends Service {
    private Handler handler;
    private int pbjPerSec;
    int brianUp, stickyFingersUp = 0;
    boolean music = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            synchronized (this) {
                brianUp = intent.getIntExtra("brianUp", 0);
                stickyFingersUp = intent.getIntExtra("stickyFingersUp", 0);
                music = intent.getBooleanExtra("music", false);
                pbjPerSec = calcPassiveIncome(brianUp, stickyFingersUp, music);
                Log.d("PassiveIncomeService", "Received values: brianUp=" + brianUp + ", stickyFingersUp=" + stickyFingersUp + ", music=" + music);
            }
        } else {
            Log.d("PassiveIncomeService", "Intent is null");
        }
        startPassiveIncome();
        return START_STICKY;
    }

    private synchronized void startPassiveIncome() {
        if (handler != null) // Check if handler already active
            handler.removeCallbacksAndMessages(null);
        else
            handler = new Handler();
        Runnable passiveIncrease = new Runnable() {
            @Override
            public void run() {
                pbjPerSec = calcPassiveIncome(brianUp, stickyFingersUp, music);
                // Send passive income update to all components
                Intent updateIntent = new Intent("passive_income_update");
                updateIntent.putExtra("pbjPerSec", pbjPerSec);
                sendBroadcast(updateIntent);
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(passiveIncrease, 1000);
    }

    private int calcPassiveIncome(int brianUp, int stickyFingersUp, boolean music) {
        int pbjPerSec = 0;
        if (brianUp == 1)
            pbjPerSec += 10;
        if (brianUp == 2)
            pbjPerSec += 20;
        if (brianUp == 3)
            pbjPerSec += 30;
        if (stickyFingersUp > 0)
            pbjPerSec += (brianUp * 5 * stickyFingersUp);
        if (music)
            pbjPerSec *= 2;
        return pbjPerSec;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
