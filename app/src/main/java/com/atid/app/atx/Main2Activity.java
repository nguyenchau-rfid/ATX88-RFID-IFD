package com.atid.app.atx;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.ChiNhanhChiTiet;
import com.atid.app.atx.ReadJson.ListChiNhanh;
import com.atid.app.atx.ReadJson.LocationAdapter;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.TokenAccess;
import com.atid.app.atx.ReadJson.TokenManager;
import com.atid.app.atx.ReadJson.User;
import com.atid.app.atx.ReadJson.WebApi;
import com.atid.app.atx.activity.DeviceManageActivity;
import com.atid.app.atx.dialog.MessageBox;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jxl.write.DateTime;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

public class Main2Activity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener   {
    private static final String TAG = Main2Activity.class.getSimpleName();
    static int machinhanh;
    static String tenchinhanh = "";
    static HashMap<Integer, String> listChiNhanh = new HashMap<>();
    EditText tvusername;
    EditText tvpass;
    Spinner spinner;
    TextView txtUser;
    Button btnLogin, btnThoat;
    TextView tvXem, tvChinhanh;
    TokenManager ngayToken = new TokenManager();
    String keyDay = "Ngay";
    String keyToken = "keytoken";
    String token = "";
    AlertDialog alert;
    boolean checkWifi = false;
    private WebApi mAPIService_DangNhap;
    private WebApi mAPIService_Token;
    private WebApi mAPIService_LayChiNhanh;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private static Long time3day= (TimeUnit.DAYS.toMillis(1))*3;
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;

    protected LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        verifyStoragePermissions(this);
        InsDisplay();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String user = tvusername.getText().toString().trim();
                 String pass = tvpass.getText().toString().trim();
               // String user = "njoe";
              //  String pass = "123";
                Dangnhap(user, pass);
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvXem.setText(mLastLocation.getLatitude()+mLastLocation.getLongitude()+"" );
                //Postuser(tvusername.getText().toString(),tvpass.getText().toString());
                if(Double.toString(mLastLocation.getLongitude())==null)
                {
                    Toast.makeText(getApplicationContext(),"Lay thong tin GPS....",Toast.LENGTH_SHORT).show();
                }
                    else
                        {Intent callGPSSettingIntent = new Intent(Main2Activity.this,Main4Activity.class);
                startActivity(callGPSSettingIntent);}


            }
        });
        if(isGpsOn())
        {
            Log.i("MyCurrent", "Bat GPS");

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER , 0, 0, this);

            if (isPlayServicesAvailable()) {
                setUpLocationClientIfNeeded();
                buildLocationRequest();

            } else {
                tvXem.setText("Device does not support Google Play services");
            }
        }
        else {
            showGPSDisabledAlertToUser();
            Log.i("MyCurrent", "khong bat GPS");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alert != null)
        { alert.dismiss(); }
    }

    public void InsDisplay() {
        tvusername = findViewById(R.id.edtUserName);
        tvpass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvXem = findViewById(R.id.tvTest);
        btnThoat = findViewById(R.id.btnThoat);
        tvChinhanh = findViewById(R.id.tvChiNhanh);
        spinner = findViewById(R.id.spinner2);
        txtUser = findViewById(R.id.textView3);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkAvailable(getApplicationContext())) {
            mAPIService_Token = ApiUnit.getToketService();
            CheckexistFileToken();
            deleteFileKiemKho();
            tvusername.setText("");
            tvpass.setText("");
            tvusername.requestFocus();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ChiNhanhChiTiet swt = (ChiNhanhChiTiet) parent.getItemAtPosition(position);
                    Integer key = (Integer) swt.getMaChinhanh();
                    machinhanh = key;
                    tenchinhanh = swt.getBranchName();
                    tvChinhanh.setText(swt.getBranchName());
                    //  account = new NhanVienAdapter("1", "2", machinhanh,tenchinhanh);
                    // Toast.makeText(getApplicationContext(), key + "----" + swt.getBranchName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
      //  finish();
       // startActivity(getIntent());
        super.onResume();
        if(alert != null)
        { alert.dismiss(); }
       this.onCreate(null);

    }

    private void updateUi() {
        if (mLastLocation != null) {
            String diachi="";
            diachi=getCompleteAddressString(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            LocationAdapter locationAdapter = new LocationAdapter();
           locationAdapter.setTrackingGPS_Latitude((float) mLastLocation.getLatitude());
            locationAdapter.setTrackingGPS_Longitude((float) mLastLocation.getLongitude());
            locationAdapter.setTrackingGPS_Location(""+diachi);
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
            strAdd =address;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MyCurrent", "Canont get Address!");
        }
        return strAdd;
    }
    private void showGPSDisabledAlertToUser(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Kiểm Tra Kết Nối GPS Cho Thiết Bị");
        alertDialogBuilder.setMessage("Vui lòng bật kết nối GPS để tiếp tục sử dụng chương trình")
                .setCancelable(false)
                .setPositiveButton("Bật Kết Nối GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        alert = alertDialogBuilder.create();
        alert.show();
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
        Log.e(TAG,file + "--"+file.listFiles());
        if (!file.isDirectory()) {
            file.mkdirs();
        } else {
            for (File c : file.listFiles()) {
                Date lastModDate = new Date(c.lastModified());
                String dateSave = ngayToken.setDateFormat(lastModDate.getTime() + time3day);
                //lastModDate.getTime().;
                Log.e(TAG, dateSave +"*-*"+ "-----"+ ngayToken.checkNgay(dateSave));
                if (c.getName().contains("Kiểm Kho ")||ngayToken.checkNgay(dateSave)==true) {
                    c.delete();
                }
            }

        }
    }

    public void Dangnhap(final String username, final String Pass) {
        mAPIService_DangNhap = ApiUnit.getNVService();
        mAPIService_DangNhap.getAuser(username, Pass).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Chào Mừng " + response.body().getName() +
                            " Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                    LoginAdapter nvparacel = new LoginAdapter(username, Pass, machinhanh, tenchinhanh);
                    Intent intent = new Intent(getApplicationContext(), DeviceManageActivity.class);
                    intent.putExtra("parcelable", nvparacel);
                    startActivity(intent);
                    call.cancel();
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
    public void Postuser(final String username, final String Pass) {
        mAPIService_DangNhap = ApiUnit.getNVService();
        mAPIService_DangNhap.ThemNV(username,"tennv", Pass).enqueue(new retrofit2.Callback<User>() {
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

    public void sendToken() {
        mAPIService_Token = ApiUnit.getToketService();
        mAPIService_Token.oathToken(ngayToken.getClient_id(), ngayToken.getClient_secret(), ngayToken.getGrant_type(), ngayToken.getScopes()).enqueue(new retrofit2.Callback<TokenAccess>() {
            public void onResponse(retrofit2.Call<TokenAccess> call, retrofit2.Response<TokenAccess> response) {
                token = ngayToken.getAuthorization() + response.body().getAccessToken();
                String valueDay = ngayToken.getDateT();
                String keyToken = "keytoken";
                ngayToken.LuuToken(getApplicationContext(), keyDay, valueDay, keyToken, token);
                //Log.e(TAG, keyDay + "--" + valueDay + "--" + token);
                call.cancel();
            }

            @Override
            public void onFailure(retrofit2.Call<TokenAccess> call, Throwable t) {
                Log.e(TAG, "sendToken " + t.toString());
                //  Toast.makeText(, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại kết nối internet để lấy token", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadSpinnerData() {

        List<ChiNhanhChiTiet> itemList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : listChiNhanh.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            // Log.d("list", value + "--*");
            itemList.add(new ChiNhanhChiTiet(value, key));
        }
        // Log.d("list", itemList.size() + "-*");
        ArrayAdapter<ChiNhanhChiTiet> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

    }

    public void LayCNKiotViet() {
        mAPIService_LayChiNhanh = ApiUnit.getServicesKiot();
        Call<ListChiNhanh> call = mAPIService_LayChiNhanh.getAllChiNhanh(ngayToken.getContent(), ngayToken.getRetailer(), ngayToken.Laytoken(getApplicationContext(), keyToken));
        call.enqueue(new retrofit2.Callback<ListChiNhanh>() {
            @Override
            public void onResponse(Call<ListChiNhanh> call, Response<ListChiNhanh> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<ChiNhanhChiTiet> tUser = response.body().getChiNhanhChiTiet();
                        for (int i = 0; i < tUser.size(); i++) {
                            String brandName = tUser.get(i).getBranchName();
                            int branchId = tUser.get(i).getId();
                            if ((listChiNhanh.get(branchId)) == null) {
                                listChiNhanh.put(branchId, brandName);
                            }
                        }
                        call.cancel();
                        loadSpinnerData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<ListChiNhanh> call, Throwable t) {
                Log.e(TAG, "LayCNKiotViet" + t.toString());
            }
        });
    }

    public void checkTimeToken() {

        String ngaytemp = ngayToken.Laytoken(getApplicationContext(), keyDay);
        LayCNKiotViet();
        if (ngayToken.checkNgay(ngaytemp)) {
            tvXem.append("\n" + "Token hết hạn");
            sendToken();
            LayCNKiotViet();
            // loadSpinnerData();
            this.recreate();
        } else {
            //tvXem.append("\n" + "ok");
            LayCNKiotViet();
        }

    }

    public void CheckexistFileToken() {
        String filePath = getApplicationContext().getFilesDir().getParent() + "/shared_prefs";
        String tenfile = "saveToken.xml";
        File file = new File(filePath, tenfile);
        //check file if not exist
        if (file.exists()) {
            //return;
            // Log.d("aa", "da ton tai");
            checkTimeToken();
        } else {
            ngayToken.LuuToken(getApplicationContext(), keyDay, ngayToken.getDateDefault());
            checkTimeToken();
        }
    }
    private boolean isGpsOn() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSon =  manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSon)
        {

            return true;
        }
        else
        {

            return false;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnectedInternet = activeNetwork != null && activeNetwork.isConnected();
        if (isConnectedInternet) {
            checkWifi = true;

            return true;
        } else {
            checkNetworkConnection();
            //Log.d("Network", "Not Connected");
            return false;
        }
    }

    public void checkNetworkConnection() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kiểm Tra Kết Nối Internet Cho Thiết Bị");
        builder.setMessage("Vui lòng bật kết nối Wifi hoặc 3G/4G để tiếp tục sử dụng chương trình");
        builder.setCancelable(checkWifi);
        builder.setNegativeButton("Bật Kết Nối Mạng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(dialogIntent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("aaa", String.format(Locale.getDefault(), "onLocationChanged : %f, %f",
                location.getLatitude(), location.getLongitude()));
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
