package com.atid.app.atx;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.LocationAdapter;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.ModelRFIDTasksAdapter;
import com.atid.app.atx.ReadJson.MyTask;
import com.atid.app.atx.ReadJson.RFIDTasksAdapter;
import com.atid.app.atx.ReadJson.TokenManager;
import com.atid.app.atx.ReadJson.User;
import com.atid.app.atx.ReadJson.ViewTaskAdapter;
import com.atid.app.atx.ReadJson.WebApi;
import com.atid.app.atx.ReadJson.WifiReceiver;
import com.atid.app.atx.activity.DeviceManageActivity;
import com.atid.app.atx.dialog.CustomProgressDialogue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class Main2Activity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = Main2Activity.class.getSimpleName();
    EditText tvusername;
    EditText tvpass,tvshowpass;
    TextView txtUser;
    Button btnLogin, btnThoat;
    TextView tvXem, tvChinhanh;

    String keyDay_IFD = "Ngay_IFD";
    String keyDay_Kiot = "Ngay_Kiot";
    String keyToken_IFD = "keytoken_IFD";
    String keyToken_Kiot = "keytoken_Kiot";

    AlertDialog alertGPS;
    Dialog alertWifi;
    Dialog myDialog;
    boolean isGpsOn = false;
    boolean isWifiOn = false;
    private WebApi mAPIService_DangNhap_IFD;
    private static Long time3day = (TimeUnit.DAYS.toMillis(1)) * 3;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    protected LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private LocationAdapter locationAdapter = new LocationAdapter();
    TokenManager ngayToken = new TokenManager();
    Context _mContext;
    BroadcastReceiver br = null;
    IntentFilter filter;
    CustomProgressDialogue object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main2);
        requestLocationPermissions();
        verifyStoragePermissions(this);
        myDialog = new Dialog(this);
        InsDisplay();
        BroadcastReceiver br = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        _mContext = getApplicationContext();
        _mContext.registerReceiver(br, filter);
        object = new CustomProgressDialogue(this);
        mAPIService_DangNhap_IFD = ApiUnit.getServiceIFD();
        if (isGpsOn()) {
            Log.i("MyCurrent", "Bat GPS");
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if (isPlayServicesAvailable()) {
                setUpLocationClientIfNeeded();
                buildLocationRequest();

            } else {
                tvXem.setText("Device does not support Google Play services");
            }
            isGpsOn = true;
        } else {
            showGPSDisabledAlertToUser();
            Log.i("MyCurrent", "khong bat GPS");
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  String user = tvusername.getText().toString().trim();
                // String pass = tvpass.getText().toString().trim();
                String user = "test1";
                String pass = "Chau#@2020";
                object.show();
                Dangnhap(user, pass);

            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Step 1: Defing object of HashMap Class
                HashMap<Integer, String> HashMap = new HashMap<Integer, String>();
                //Step 2: Adding Key Value pair
                HashMap.put(1001, "India");
                HashMap.put(1002, "Canada");
                HashMap.put(1003, "Australia");
                //Step 3: Displaying key value pairs using for loop
                for (Map.Entry map : HashMap.entrySet()) {
                    //Step 4: Using getKey and getValue methods to retrieve key and corresponding value
                    if(map.getValue().equals("Australia"))
                        System.out.println(map.getKey() + " " + map.getValue());
                }
                 Intent intent = new Intent(getApplicationContext(), Main4Activity.class);
                  startActivity(intent);
            }
        });
    }


    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void TaoMoiToken() {
        MyTask myTask = new MyTask();
        Timer timer = new Timer();
        timer.schedule(myTask, 0, 1000 * 60 * 60 * 8);

    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertGPS != null) {
            alertGPS.dismiss();
        }
        if (alertWifi != null) {
            alertWifi.dismiss();
        }

    }

    public void InsDisplay() {
        tvusername = findViewById(R.id.edtUserName);
        tvpass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvXem = findViewById(R.id.tvTest);
        btnThoat = findViewById(R.id.btnThoat);
        tvChinhanh = findViewById(R.id.tvChiNhanh);
        txtUser = findViewById(R.id.textView3);
        TextInputLayout textFirstname = (TextInputLayout) findViewById(R.id.txInput);
      //  EditText firstname = (EditText) findViewById(R.id.tvInput);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        if (isNetworkAvailable(getApplicationContext())) {
              TaoMoiToken();
        }

    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        //    stopService(new Intent(getApplicationContext(), MyService.class));
        finish();
        super.onStop();
    }

    @Override
    public void onResume() {
        if (br != null) {
            Log.d("Receiver", "Can't register receiver which already has been registered");
        } else {
            try {
                br = new WifiReceiver();
                filter = new IntentFilter();
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(CONNECTIVITY_SERVICE);
                _mContext = getApplicationContext();
                _mContext.registerReceiver(br, filter);
            } catch (Exception err) {
                Log.e(err.getClass().getName(), err.getMessage(), err);
            }
        }
        this.onCreate(null);
        super.onResume();
    }

    @Override
    public void onPause() {
        try {
            if (br == null) {
                Log.d("Receiver", "Can't unregister a receiver which was never registered");
            } else {
                getApplicationContext().unregisterReceiver(br);
                br = null;
            }
        } catch (Exception err) {
            Log.e(err.getClass().getName(), err.getMessage(), err);
            Log.e("Receiver not registered", "Couldn't get context");
        }
        super.onPause();
    }

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void Dangnhap(final String username, final String Pass) {
        Call<User> call = mAPIService_DangNhap_IFD.getAuser(username, Pass, ngayToken.Laytoken(getApplicationContext(), keyToken_IFD));
        call.enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    object.dismiss();
                    Toast.makeText(getApplicationContext(), "Chào Mừng " +
                            " Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                    //LoginAdapter nvparacel = new LoginAdapter(username, Pass, maUserID);
                    // Intent intent = new Intent(getApplicationContext(), DeviceManageActivity.class);
                    //  Intent intent = new Intent(getApplicationContext(), Main4Activity.class);
                    //   intent.putExtra("parcelable", nvparacel);
                    //    startActivity(intent);
                    double tokendangnhap = response.body().getModelUser().getUserID();
                    //Log.e(TAG, "Dangnhap " + tokendangnhap);
                    LayTaskUser(tokendangnhap);
                } else {
                    //response.errorBody().string()
                    // error case
                    JSONObject object = null;
                    String messageString = "";
                    try {
                        object = new JSONObject(response.errorBody().string());
                        System.out.println(object.getString("errorMessage"));
                        messageString = object.getString("errorMessage");

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(getApplicationContext(), "Loi 401" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(getApplicationContext(), "Loi 404" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getApplicationContext(), "Loi 500" + messageString, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "unknown error " + messageString, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Dangnhap " + t.toString());
                Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại kết nối Internet.....", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void LayTaskUser(double maUserID) {
        //  mAPIService_DangNhap_IFD = ApiUnit.getServiceIFD();
        Call<RFIDTasksAdapter> call = mAPIService_DangNhap_IFD.getRFIDTasks(maUserID, ngayToken.Laytoken(getApplicationContext(), keyToken_IFD));
        call.enqueue(new retrofit2.Callback<RFIDTasksAdapter>() {
            @Override
            public void onResponse(Call<RFIDTasksAdapter> call, Response<RFIDTasksAdapter> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<ModelRFIDTasksAdapter> ListTask = response.body().getModelRFIDTasksAdapter();

//                        for (int i = 0; i < ListTask.size(); i++) {
//                            System.out.println("Danh sach Task " + ListTask.get(i).getRfidTaskID() + " : " + ListTask.get(i).getDescription());
//                        }

                        ShowPopup(ListTask);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(retrofit2.Call<RFIDTasksAdapter> call, Throwable t) {
                Log.e(TAG, "LayTaskUser" + t.toString());
            }
        });
    }

    public void ShowPopup(ArrayList<ModelRFIDTasksAdapter> allUsersList) {
        try {
            TextView txtclose;
            myDialog.setContentView(R.layout.custompopup);
            ListView listView = myDialog.findViewById(R.id.lvTask);
            txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
            txtclose.setText("X");
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            final ViewTaskAdapter arrayAdapter = new ViewTaskAdapter(getApplicationContext(), R.layout.customshow, allUsersList);
            listView.setAdapter(arrayAdapter);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LoginAdapter post = new LoginAdapter();
                    post.setVoucherType(arrayAdapter.getItem(position).getVoucherType());
                    post.setmaUserID(arrayAdapter.getItem(position).getRfidTaskID());
                    if (mLastLocation != null) {
                        post.setLongitude(locationAdapter.getTrackingGPS_Latitude());
                        post.setLatitude(locationAdapter.getTrackingGPS_Longitude());
                        post.setLocalAddress(locationAdapter.getTrackingGPS_Location());
                    } else {
                        post.setLongitude(0);
                        post.setLatitude(0);
                    }
                    System.out.println(post.getmaUserID() + "\n"
                            + post.getLongitude() + "\n"
                            + post.getLongitude()
                    );
                    // Intent intent = new Intent(getApplicationContext(), DeviceManageActivity.class);
                    Intent intent = new Intent(getApplicationContext(), DeviceManageActivity.class);
                    // intent.putExtra("parcelable", nvparacel);
                    myDialog.cancel();
                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUi() {
        if (mLastLocation != null) {
            String diachi = "";
            diachi = getCompleteAddressString(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            locationAdapter.setTrackingGPS_Latitude(mLastLocation.getLatitude());
            locationAdapter.setTrackingGPS_Longitude(mLastLocation.getLongitude());
            locationAdapter.setTrackingGPS_Location("" + diachi);
            locationAdapter.setTrackingGPS_Time();
            tvXem.setText(diachi);
        }
    }

    // doi dia chi location sang dia chi vat ly
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            strAdd = address;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyCurrent", "Canont get Address!");
        }
        return strAdd;
    }

    private void buildLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

    }

    private boolean isPlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                == ConnectionResult.SUCCESS;
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void deleteFileKiemKho() {

        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Temp_ATX";
        File file = new File(sdCard);
        Log.e(TAG, file + "--" + file.listFiles());
        if (!file.isDirectory()) {
            file.mkdirs();
        } else {
            for (File c : file.listFiles()) {
                Date lastModDate = new Date(c.lastModified());
                String dateSave = ngayToken.setDateFormat(lastModDate.getTime() + time3day);
                //lastModDate.getTime().;
                Log.e(TAG, dateSave + "*-*" + "-----" + ngayToken.checkNgay(dateSave));
                if (c.getName().contains("Kiểm Kho ") || ngayToken.checkNgay(dateSave) == true) {
                    c.delete();
                }
            }

        }
    }

    public void Postuser(final String username, final String Pass) {
        mAPIService_DangNhap_IFD = ApiUnit.getServiceIFD();
        mAPIService_DangNhap_IFD.ThemNV(username, "tennv", Pass).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "Dangnhap " + response.body().toString());
                } else
                    Toast.makeText(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu không đúng.....", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Dangnhap " + t.toString());
                Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại kết nối Internet.....", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    public void CheckexistFileToken() {
        String filePath = getApplicationContext().getFilesDir().getParent() + "/shared_prefs";
        String tenfile = "saveToken.xml";
        File file = new File(filePath, tenfile);
        //check file if not exist
        if (file.exists()) {
            //return;
            Log.e("aa", "da ton tai");
            // checkTimeToken();
        } else {
            ngayToken.LuuToken(getApplicationContext(), keyDay_IFD, ngayToken.getDateT());
            //  checkTimeToken();
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnectedInternet = activeNetwork != null && activeNetwork.isConnected();
        if (isConnectedInternet) {
            isWifiOn = true;
            return true;
        } else {
            // showWifiDisabledAlertToUser();
            //Log.d("Network", "Not Connected");
            return false;
        }
    }

    private void showGPSDisabledAlertToUser() {
        if (alertGPS == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Kiểm Tra Kết Nối GPS Cho Thiết Bị");
            alertDialogBuilder.setMessage("Vui lòng bật kết nối GPS để tiếp tục sử dụng chương trình")
                    .setCancelable(false)
                    .setPositiveButton("Bật Kết Nối GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    callGPSSettingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(callGPSSettingIntent);

                                }
                            });
            alertGPS = alertDialogBuilder.create();
            alertGPS.show();
        } else
            return;


    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.i("aaa", String.format(Locale.getDefault(), "onLocationChanged : %f, %f",
//                location.getLatitude(), location.getLongitude()));
        mLastLocation = location;
        updateUi();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLastLocation = lastLocation;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
//    @Override
//    protected void onResume() {
//        //  finish();
//        // startActivity(getIntent());
////        registerReceiver(br, new IntentFilter(MyService.COUNTDOWN_BR));
//        super.onResume();
//        if (isGpsOn == true && alertGPS != null) {
//            alertGPS.dismiss();
//        }
//        if (isWifiOn == true && alertWifi != null) {
//            alertWifi.dismiss();
//        }
//        this.onCreate(null);
//
//    }
//        if (isNetworkAvailable(getApplicationContext())) {
//            alertWifi.dismiss();
//            TaoMoiToken();
//            //mAPIService_Token = ApiUnit.getToketService();
//            //  startService(new Intent(getApplicationContext(), MyService.class));
//            //ngayToken.sendToken();
//            //  CheckexistFileToken();
//            //   deleteFileKiemKho();
//            tvusername.setText("");
//            tvpass.setText("");
//            tvusername.requestFocus();
////            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                @Override
////                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                    ChiNhanhChiTiet swt = (ChiNhanhChiTiet) parent.getItemAtPosition(position);
////                    Integer key = (Integer) swt.getMaChinhanh();
////                    maUserID = key;
////                    tenchinhanh = swt.getBranchName();
////                    tvChinhanh.setText(swt.getBranchName());
////                    //  account = new NhanVienAdapter("1", "2", machinhanh,tenchinhanh);
////                    // Toast.makeText(getApplicationContext(), key + "----" + swt.getBranchName(), Toast.LENGTH_SHORT).show();
////                }
////                @Override
////                public void onNothingSelected(AdapterView<?> parent) {
////
////                }
////            });
//    public void showWifiDisabledAlertToUser() {
//        if (alertWifi == null) {
//            AlertDialog.Builder alertWifiBuilder = new AlertDialog.Builder(this);
//            alertWifiBuilder.setTitle("Kiểm Tra Kết Nối Internet Cho Thiết Bị");
//            alertWifiBuilder.setMessage("Vui lòng bật kết nối Wifi hoặc 3G/4G để tiếp tục sử dụng chương trình")
//                    .setCancelable(false)
//                    .setPositiveButton("Bật Kết Nối Mạng",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    Intent callWifiSettingIntent = new Intent(
//                                            android.provider.Settings.ACTION_WIFI_SETTINGS);
//                                    callWifiSettingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(callWifiSettingIntent);
//                                }
//                            });
//            alertWifi = alertWifiBuilder.create();
//            alertWifi.show();
//        } else return;
//    }
//    private void loadSpinnerData() {
//
//        List<ChiNhanhChiTiet> itemList = new ArrayList<>();
//        for (Map.Entry<Integer, String> entry : listChiNhanh.entrySet()) {
//            Integer key = entry.getKey();
//            String value = entry.getValue();
//            // Log.d("list", value + "--*");
//            itemList.add(new ChiNhanhChiTiet(value, key));
//        }
//        // Log.d("list", itemList.size() + "-*");
//        ArrayAdapter<ChiNhanhChiTiet> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, itemList);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerAdapter);
//
//    }

//    public void LayCNKiotViet() {
//        mAPIService_LayChiNhanh = ApiUnit.getServicesKiot();
//        Call<ListChiNhanh> call = mAPIService_LayChiNhanh.getAllChiNhanh(ngayToken.getContent(), ngayToken.getRetailer(), ngayToken.Laytoken(getApplicationContext(), keyToken));
//        call.enqueue(new retrofit2.Callback<ListChiNhanh>() {
//            @Override
//            public void onResponse(Call<ListChiNhanh> call, Response<ListChiNhanh> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        ArrayList<ChiNhanhChiTiet> tUser = response.body().getChiNhanhChiTiet();
//                        for (int i = 0; i < tUser.size(); i++) {
//                            String brandName = tUser.get(i).getBranchName();
//                            int branchId = tUser.get(i).getId();
//                            if ((listChiNhanh.get(branchId)) == null) {
//                                listChiNhanh.put(branchId, brandName);
//                            }
//                        }
//                        call.cancel();
//                        loadSpinnerData();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<ListChiNhanh> call, Throwable t) {
//                Log.e(TAG, "LayCNKiotViet" + t.toString());
//            }
//        });
//    }

//    public void checkTimeToken() {
//
//        String ngaytemp = ngayToken.Laytoken(getApplicationContext(), keyDay);
//        if (ngayToken.checkNgay(ngaytemp)) {
//            Log.e("Gettoken", ngayToken.checkNgay(ngaytemp) + "--" + "--");
//
//            sendToken();
//            this.recreate();
//        } else {
//            Log.e("Gettoken", ngayToken.checkNgay(ngaytemp) + "--" + "khong het han" + ngaytemp);
//
//        }
//
//    }

// String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Temp_ATX";
//   File file = new File(sdCard);
//    Log.e(TAG,file + "--"+file.listFiles());
//      if (!file.isDirectory()) {
//     file.mkdirs();

