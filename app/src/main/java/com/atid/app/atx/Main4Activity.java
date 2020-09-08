package com.atid.app.atx;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atid.app.atx.ReadJson.ApiUnit;
import com.atid.app.atx.ReadJson.ChiNhanhChiTiet;
import com.atid.app.atx.ReadJson.ListChiNhanh;
import com.atid.app.atx.ReadJson.LocationAdapter;
import com.atid.app.atx.ReadJson.LoginAdapter;
import com.atid.app.atx.ReadJson.PostTag;
import com.atid.app.atx.ReadJson.TokenManager;
import com.atid.app.atx.ReadJson.WebApi;
import com.atid.app.atx.dialog.MessageBox;
import com.atid.app.atx.dialog.WaitDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

    ArrayList<PostTag> lisRFID= new ArrayList<PostTag>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btnPost = (Button) findViewById(R.id.btnPost);
        tvXem = findViewById(R.id.tvDiachi);

        loginTask.setmaUserID(16.0);
        Arraylisttest();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ListView listView = (ListView) findViewById(R.id.lstViewTest);
//                PosttagAdapter adapter = new PosttagAdapter(getApplicationContext(), lisRFID);
//                listView.setAdapter(adapter);
//                for(int i=0;i<lisRFID.size();i++)
//                {
//                    PostReadTag(lisRFID.get(i));
//                }
//                for(int i=0;i<lisRFID.size();i++)
//                {
//                   // PostReadTag(lisRFID.get(i));
//                    System.out.println(lisRFID.get(i).getEpc() +"--"+lisRFID.get(i).getRfidTaskID());
//                }
               // MessageBox.show( Main4Activity.this,"Xin chao");
                WaitDialog.show(Main4Activity.this,2,"Please wait","Loading..",null );


            }
        });

    }

    public void Arraylisttest()
    {
        lisRFID= new ArrayList<PostTag>();
        PostTag post = new PostTag();
        PostTag post1 = new PostTag();
        PostTag post2 = new PostTag();
        PostTag post3 = new PostTag();
        PostTag post4 = new PostTag();
        PostTag post5 = new PostTag();
        PostTag post6 = new PostTag();
        PostTag post7 = new PostTag();
        PostTag post8 = new PostTag();
        PostTag post9 = new PostTag();

        post.setRfidTaskID(loginTask.getmaUserID());
        post.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post.setEpc("4966640000000000000000A9");
        lisRFID.add(post);
        post1.setRfidTaskID(loginTask.getmaUserID());
        post1.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post1.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post1.setEpc("4966640000000000000000A7");
        lisRFID.add(post1);
        post2.setRfidTaskID(loginTask.getmaUserID());
        post2.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post2.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post2.setEpc("4966640000000000000000A8");
        lisRFID.add(post2);
        post3.setRfidTaskID(loginTask.getmaUserID());
        post3.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post3.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post3.setEpc("4966640000000000000000AF");
        lisRFID.add(post3);
        post4.setRfidTaskID(loginTask.getmaUserID());
        post4.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post4.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post4.setEpc("4966640000000000000000AA");
        lisRFID.add(post4);
        post5.setRfidTaskID(loginTask.getmaUserID());
        post5.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post5.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post5.setEpc("4966640000000000000000AB");
        lisRFID.add(post5);
        post6.setRfidTaskID(loginTask.getmaUserID());
        post6.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post6.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post6.setEpc("4966640000000000000000AC");
        lisRFID.add(post6);
        post7.setRfidTaskID(loginTask.getmaUserID());
        post7.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post7.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post7.setEpc("4966640000000000000000AD");
        lisRFID.add(post7);
        post8.setRfidTaskID(loginTask.getmaUserID());
        post8.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post8.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post8.setEpc("4966640000000000000000AE");
        lisRFID.add(post7);
        post9.setRfidTaskID(loginTask.getmaUserID());
        post9.setTrackingGPS_Longitude(localManager.getTrackingGPS_Longitude());
        post9.setTrackingGPS_Latitude(localManager.getTrackingGPS_Latitude());
        post9.setEpc("4966640000000000000000B0");
        lisRFID.add(post9);

    }
    public void PostReadTag(PostTag posttag) {
        mAPIService_PostReadTag = ApiUnit.getServiceIFD();
        mAPIService_PostReadTag.PostReadTagTest(posttag, ngayToken.Laytoken(getApplicationContext(), keyToken_IFD)).enqueue(new retrofit2.Callback<PostTag>() {
            @Override
            public void onResponse(Call<PostTag> call, Response<PostTag> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),response.body().getDidError()+"--"+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
