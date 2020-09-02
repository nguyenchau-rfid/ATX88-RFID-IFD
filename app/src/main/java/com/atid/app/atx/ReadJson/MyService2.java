package com.atid.app.atx.ReadJson;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.atid.app.atx.Main4Activity;

import java.util.Date;

public class MyService2 extends JobIntentService {
    TokenManager ngayToken = new TokenManager();
    private static final int UNIQUE_JOB_ID=1338;

    static void enqueueWork(Context ctxt) {

        enqueueWork(ctxt,MyService2.class,1,new Intent(ctxt, MyService2.class));

    }

    @Override
    public void onHandleWork(@NonNull Intent intent) {

        Log.i(getClass().getSimpleName(), "Alarm run "+ new Date().toString()+ngayToken.Laytoken(getApplicationContext(),"Ngay"));

    }
}
