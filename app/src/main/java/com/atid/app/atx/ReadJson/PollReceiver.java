package com.atid.app.atx.ReadJson;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class PollReceiver extends BroadcastReceiver {
    private final static String TAG = "WifiService";
    private static final int PERIOD=1000*60; // 1 minutes
    // 60 * 60 * 1000
    private  static final int INITIAL_DELAY=5000; // 5 seconds

    static AlarmManager mgr;
    static Intent i;
    static PendingIntent pi;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected()) {
            // Your code here

        }


//        if (intent.getAction() == null) {
//            MyService2.enqueueWork(context);
//        }
//        else {
//            scheduleAlarms(context);
//        }
    }
   public static void scheduleAlarms(Context ctxt) {
//        AlarmManager mgr=
//                (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
//        Intent i=new Intent(ctxt, PollReceiver.class);
//        PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);
//        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + INITIAL_DELAY,
//                PERIOD, pi);


    }
    public static void scheduleAlarmsStop() {
      //  pi.cancel();
       // mgr.cancel(pi);
   //    Log.i(TAG, "Alarm cancelled"+ new Date().toString());

    }

}
