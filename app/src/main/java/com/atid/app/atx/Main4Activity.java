package com.atid.app.atx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.ChiNhanhChiTiet;
import com.atid.app.atx.ReadJson.ListChiNhanh;
import com.atid.app.atx.ReadJson.Location;
import com.atid.app.atx.ReadJson.LocationAdapter;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.MyService;
import com.atid.app.atx.ReadJson.PostTag;
import com.atid.app.atx.ReadJson.ReadTagAdapter;
import com.atid.app.atx.ReadJson.TokenAccess;
import com.atid.app.atx.ReadJson.TokenManager;
import com.atid.app.atx.ReadJson.User;
import com.atid.app.atx.ReadJson.WebApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class Main4Activity extends Activity {
    LocationAdapter localManager = new LocationAdapter();
    LoginAdapter loginTask = new LoginAdapter();
    TokenManager ngayToken = new TokenManager();
    private WebApi mAPIService_PostReadTag,mAPIService_LayChiNhanh;
    Button btnPost;
    TextView tvXem;
    String keyDay_IFD = "Ngay_IFD";
    String keyDay_Kiot = "Ngay_Kiot";
    String keyToken_IFD = "keytoken_IFD";
    String keyToken_Kiot = "keytoken_Kiot";

    PostTag post = new PostTag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btnPost = (Button) findViewById(R.id.btnPost);
        tvXem = findViewById(R.id.tvDiachi);


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
                View.inflate(getApplicationContext(), R.layout.view_inventory, rootLayout);

            }
        });

    }
        public void LayCNKiotViet() {
        mAPIService_LayChiNhanh = ApiUnit.getServicesKiot();
        Call<ListChiNhanh> call = mAPIService_LayChiNhanh.getAllChiNhanh(ngayToken.getContent(), ngayToken.getRetailer(), ngayToken.Laytoken(getApplicationContext(), keyToken_Kiot));
        call.enqueue(new retrofit2.Callback<ListChiNhanh>() {
            @Override
            public void onResponse(Call<ListChiNhanh> call, Response<ListChiNhanh> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<ChiNhanhChiTiet> tUser = response.body().getChiNhanhChiTiet();
                        for (int i = 0; i < tUser.size(); i++) {
                            String brandName = tUser.get(i).getBranchName();
                            int branchId = tUser.get(i).getId();
                           System.out.println(branchId +" -- "+ brandName);
                        }
                        call.cancel();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ListChiNhanh> call, Throwable t) {
                Log.e("TAG", "LayCNKiotViet" + t.toString());
            }
        });
    }
    public void PostReadTag(String epc) {
        PostTag post = new PostTag();
        post.setRfidTaskID(loginTask.getmaUserID());
        post.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post.setEpc("4966640000000000000000A9");
        System.out.println(post.toString());
        mAPIService_PostReadTag = ApiUnit.getServiceIFD();
        mAPIService_PostReadTag.PostReadTagTest(post, ngayToken.Laytoken(getApplicationContext(), keyToken_IFD)).enqueue(new retrofit2.Callback<PostTag>() {
            @Override
            public void onResponse(Call<PostTag> call, Response<PostTag> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
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
            public void onFailure(Call<PostTag> call, Throwable t) {
                Log.e("postLocation", "Dangnhap " + t.toString());
                call.cancel();
            }
        });
    }

    //    mAPIService_PostReadTag = ApiUnit.getIFDService();
//        mAPIService_PostReadTag.PostReadTag(loginTask.getmaUserID(),
//                localManager.getTrackingGPS_Longitude(),
//                localManager.getTrackingGPS_Latitude(),
//                localManager.getTrackingGPS_Location(),
//    epc,ngayToken.Laytoken(getApplicationContext(),keyToken)).enqueue(new retrofit2.Callback<ReadTagAdapter>() {
//        @Override
//        public void onResponse(Call<ReadTagAdapter> call, Response<ReadTagAdapter> response) {
//            if (response.isSuccessful()) {
//                System.out.println("Post ReadTag thanh cong "+response.body().getMessage());
//            } else {
//                //response.errorBody().string()
//                // error case
//                switch (response.code()) {
//                    case 401:
//                        Toast.makeText(getApplicationContext(), "invalid_token", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 404:
//                        Toast.makeText(getApplicationContext(), "user name or password is wrong", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 500:
//                        Toast.makeText(getApplicationContext(), "server broken", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        System.out.println( "unknown error "+response.toString()
//                        );
//                        break;
//                }
//            }
//
//            call.cancel();
//        }
//        @Override
//        public void onFailure(Call<ReadTagAdapter> call, Throwable t) {
//            Log.e("postLocation", "Dangnhap " + t.toString());
//            call.cancel();
//        }
//    });
//}
    public void checkTimeToken() {

        String ngaytemp = ngayToken.Laytoken(getApplicationContext(), keyDay_IFD);

        if (ngayToken.checkNgay(ngaytemp)) {
            Log.e("Gettoken", ngayToken.checkNgay(ngaytemp) + "--" + "--");
            Toast.makeText(getApplicationContext(), " het han.....", Toast.LENGTH_SHORT).show();
            //sendToken();
            // this.recreate();
        } else {
            Toast.makeText(getApplicationContext(), "khong het han.....", Toast.LENGTH_SHORT).show();
            Log.e("Gettoken", ngayToken.checkNgay(ngaytemp) + "--" + "khong het han" + ngaytemp);

        }

    }
//    public void Postuser() {
//        mAPIService_DangNhap = ApiUnit.getNVService();
//        mAPIService_DangNhap.ThemLocation(localManager.getTrackingGPS_Longitude(),
//                localManager.getTrackingGPS_Latitude(),
//                localManager.getTrackingGPS_Time(),
//                localManager.getTrackingGPS_Location()).enqueue(new retrofit2.Callback<Location>() {
//            @Override
//            public void onResponse(Call<Location> call, Response<Location> response) {
//                if (response.isSuccessful()) {
//                    Log.e("postLocation", "Dangnhap " +localManager.getTrackingGPS_Latitude() + "-"+ localManager.getTrackingGPS_Longitude());
//                } else
//                    Toast.makeText(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu không đúng.....", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<Location> call, Throwable t) {
//                Log.e("postLocation", "Dangnhap " + t.toString());
//                Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại kết nối Internet.....", Toast.LENGTH_SHORT).show();
//                call.cancel();
//            }
//        });
//    }

}
