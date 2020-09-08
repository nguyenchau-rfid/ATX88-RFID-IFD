package com.atid.app.atx;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.RfidToProductCode;
import com.atid.app.atx.ReadJson.WebApi;
import com.atid.app.atx.ReadJson.WifiReceiver;
import com.atid.app.atx.activity.view.BaseView;
import com.atid.app.atx.adapter.DataListAdapter;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.lib.module.barcode.types.BarcodeType;
import com.atid.lib.module.rfid.uhf.params.TagExtParam;
import com.atid.lib.reader.ATEAReader;
import com.atid.lib.reader.types.KeyState;
import com.atid.lib.reader.types.KeyType;
import com.atid.lib.reader.types.OperationMode;
import com.atid.lib.types.ActionState;
import com.atid.lib.types.ResultCode;
import com.atid.lib.util.diagnotics.ATLog;

import java.text.BreakIterator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class Main3Activity  extends Activity   {
    Button btnTurnOnWIfi,btnWifiOk;
    WifiManager wifiManager;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    private BroadcastReceiver mNetworkReceiver;
    static TextView tvStatuswifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btnTurnOnWIfi=(Button) findViewById(R.id.btnwifiTurnOn);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        tvStatuswifi=(TextView)findViewById(R.id.tvStatusWifi);
        mNetworkReceiver = new WifiReceiver();
        registerNetworkBroadcastForNougat();
        if(isOnline())
        {
            finish();
        }
        btnTurnOnWIfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(true);

                    Handler handler = new Handler();
                    Runnable delayrunnable = new Runnable() {
                        @Override
                        public void run() {
                            Intent activity = new Intent(getApplicationContext(), Main2Activity.class);
                            startActivity(activity);
                        }
                    };
                    handler.postDelayed(delayrunnable, 5000);
                }
        });

    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
    public static void dialog(boolean value) {
        if (value) {
            tvStatuswifi.setText("We are back !!!");
            tvStatuswifi.setBackgroundColor(Color.GREEN);
            tvStatuswifi.setTextColor(Color.WHITE);
        } else {
            tvStatuswifi.setVisibility(View.VISIBLE);
            tvStatuswifi.setText("Could not Connect to internet !!!");
            tvStatuswifi.setBackgroundColor(Color.RED);
            tvStatuswifi.setTextColor(Color.WHITE);
        }
    }
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        finish();
        super.onStop();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }



}
