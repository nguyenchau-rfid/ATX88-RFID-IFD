package com.atid.app.atx.ReadJson;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class MyService extends Service {
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.atid.app.atx.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);
    CountDownTimer cdt = null;

 //   private Handler handler;
 //   private Runnable myRunnable;
    public MyService() {}
    @Override
    public void onCreate() {
        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(60000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown 5seconds remaining-services: " + millisUntilFinished / 5000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
            }
        };

        cdt.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
//        handler =  new Handler();
//        Runnable myRunnable = new Runnable() {
//            public void run() {
//                //Do something after 15Min
//                Log.e("CountexpiredToken",  "--" + "--"+new Date().toString());
//                handler.postDelayed(this, 1000*60);
//                Log.e("CountexpiredToken",  "Dem duoc 15p" + "--"+new Date().toString());
//            }
//        };
//        handler.postDelayed(myRunnable, 1000*60);
//
//        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
      //  handler.removeMessages(0);
       // handler.removeCallbacks(myRunnable);
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
