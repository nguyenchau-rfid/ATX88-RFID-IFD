package com.atid.app.atx;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.Location;
import com.atid.app.atx.ReadJson.LocationAdapter;
import com.atid.app.atx.ReadJson.User;
import com.atid.app.atx.ReadJson.WebApi;

import retrofit2.Call;
import retrofit2.Response;

public class Main4Activity extends Activity {
    LocationAdapter localManager=new LocationAdapter();
    private WebApi mAPIService_DangNhap;
    Button btnPost;
    TextView tvXem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btnPost=(Button)findViewById(R.id.btnPost);
        tvXem=findViewById(R.id.tvDiachi);
       // localManager.setTrackingGPS_Time();
        tvXem.setText(localManager.getTrackingGPS_Latitude()+"\n"+localManager.getTrackingGPS_Longitude()+"\n"+
                localManager.getTrackingGPS_Location()+"\n"+localManager.getTrackingGPS_Time()+"");
        Toast.makeText(getApplicationContext(),
                localManager.getTrackingGPS_Latitude()+localManager.getTrackingGPS_Longitude()+
                localManager.getTrackingGPS_Location()+localManager.getTrackingGPS_Time()+"", Toast.LENGTH_SHORT).show();
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Postuser();
            }
        });

    }

    public void Postuser() {
        mAPIService_DangNhap = ApiUnit.getNVService();
        mAPIService_DangNhap.ThemLocation(localManager.getTrackingGPS_Longitude(),
                localManager.getTrackingGPS_Latitude(),
                localManager.getTrackingGPS_Time(),
                localManager.getTrackingGPS_Location()).enqueue(new retrofit2.Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                    Log.e("postLocation", "Dangnhap " +localManager.getTrackingGPS_Latitude() + "-"+ localManager.getTrackingGPS_Longitude());
                } else
                    Toast.makeText(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu không đúng.....", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Log.e("postLocation", "Dangnhap " + t.toString());
                Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại kết nối Internet.....", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

}
