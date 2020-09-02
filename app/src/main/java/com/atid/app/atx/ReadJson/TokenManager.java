package com.atid.app.atx.ReadJson;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.atid.app.atx.Main2Activity;
import com.atid.app.atx.R;
import com.atid.app.atx.activity.view.BaseView;
import com.atid.app.atx.dialog.MessageBox;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TokenManager extends Application {
    private static Application instance;

    public TokenManager() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private String scopes_kiot = "PublicApi.Access";
    private String Authorization = "Bearer ";
    String token = "";
    String keyDay_IFD = "Ngay_IFD";
    String keyDay_Kiot = "Ngay_Kiot";

    private String Content = "application/json";
    private String Retailer = "ifd";
    private String client_id = "s5Q#5mLCPuvJGkgs";
    private String client_id_kiot = "3b95665a-8c28-492f-85f5-d1a095e541a9";
    private String client_secret = "H2pm2aRUdZ8lTQHK6zcwiWMs++7Tm9hEPoss1+Uk9C8=";
    private String client_secret_kiot = "5D593843B1E1AF345598ADD93B484171C058A367";
    private String grant_type = "client_credentials";
    private String scopes = "api1";
    private WebApi mAPIService_Token_IFD, mAPIService_Token_KiotViet;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat simpleday = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private SimpleDateFormat simpleHour = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Date dateSave = null;
    private Date dateToday = null;
    private Calendar calNow = Calendar.getInstance();
    private String dateT = simpleday.format(calNow.getTime());
    private String dateDefault = "";
    private String dateFormat = "";
    private String GioFormat = "";

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public String getClient_id_kiot() {
        return client_id_kiot;
    }

    public String getClient_secret_kiot() {
        return client_secret_kiot;
    }

    public String getScopes_kiot() {
        return scopes_kiot;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String setDateFormat(Long dateConvert) {
        return dateFormat = df.format(dateConvert);
    }

    public String setGio(Long GioConvert) {
        return GioFormat = simpleday.format(GioConvert);
    }

    public Date getGio() {
        return calNow.getTime();
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getScopes() {
        return scopes;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void sendTokenifd() {
        mAPIService_Token_IFD = ApiUnit.getTokenIfd();
        mAPIService_Token_IFD.TokenIFD(getClient_id(), getClient_secret(), getGrant_type(), getScopes()).enqueue(new retrofit2.Callback<TokenAccess>() {
            public void onResponse(retrofit2.Call<TokenAccess> call, retrofit2.Response<TokenAccess> response) {
                if (response.isSuccessful()) {
                    token = getAuthorization() + response.body().getAccessToken();
                    String valueDay = getDateT();
                    String keyToken = "keytoken_IFD";
                    LuuToken(getContext(), keyDay_IFD, valueDay, keyToken, token);
                    Log.e("Gettoken", keyDay_IFD + "--" + valueDay + "--" + token);
                }
                call.cancel();
            }

            @Override
            public void onFailure(retrofit2.Call<TokenAccess> call, Throwable t) {
                Log.e("TAG", "sendToken-- " + t.toString());
                Toast.makeText(getContext(), "Vui lòng kiểm tra lại kết nối internet để lấy token", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendTokenKiotViet() {
        mAPIService_Token_KiotViet = ApiUnit.getTokenKiot();
        mAPIService_Token_KiotViet.TokenKiotViet(getClient_id_kiot(), getClient_secret_kiot(), getGrant_type(), getScopes_kiot()).enqueue(new retrofit2.Callback<TokenAccess>() {
            public void onResponse(retrofit2.Call<TokenAccess> call, retrofit2.Response<TokenAccess> response) {
                if (response.isSuccessful()) {
                    token = getAuthorization() + response.body().getAccessToken();
                    String valueDay = getDateT();
                    String keyToken = "keytoken_Kiot";
                    LuuToken(getContext(), keyDay_Kiot, valueDay, keyToken, token);
                    Log.e("Gettoken", keyDay_Kiot + "--" + valueDay + "--" + token);
                }
                call.cancel();
            }

            @Override
            public void onFailure(retrofit2.Call<TokenAccess> call, Throwable t) {
                Log.e("TAG", "sendToken-- " + t.toString());
                //  Toast.makeText(, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Vui lòng kiểm tra lại kết nối internet để lấy token", Toast.LENGTH_LONG).show();


            }
        });
    }

    public String getDateT() {
        Calendar calNow = Calendar.getInstance();
        String dateT = simpleHour.format(calNow.getTime());
        System.out.println("Lay gio hien tai token" + dateT);
        return dateT;
    }

    public String setDateHour(String ngaygio) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(ngaygio);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateT = simpleHour.format(date);

        return dateT;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }


    public boolean checkNgay(String dateS) {
        try {
            //   long diff =GioToken.getGio().getTime()- ngayToken.getGio().getTime() ;
            //  long diffMinutes = diff / (60 * 1000);
            simpleday.setLenient(false);
            Calendar calNow = Calendar.getInstance();
            String dateNow = simpleday.format(calNow.getTime());
            dateSave = simpleday.parse(dateS);
            dateToday = simpleday.parse(dateNow);
            long diff = dateToday.getTime() - dateSave.getTime();
            long diffMinutes = diff / (60 * 1000);
            Log.e("Gettoken", simpleday.format(dateToday.getTime()) + "--" + simpleday.format(dateSave.getTime()) + "===" + diffMinutes);
            // if (dateSave.before(dateToday))
            //    return true;
            if (diffMinutes > 58)
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void LuuToken(Context c, String keyDate, String valuesDate, String keyToken, String valuesToken) {

        SharedPreferences sharedPreferences = c.getSharedPreferences("saveToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyDate, valuesDate);
        editor.putString(keyToken, valuesToken);

        editor.apply();

    }

    public void LuuToken(Context c, String keyDate, String valuesDate) {

        SharedPreferences sharedPreferences = c.getSharedPreferences("saveToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyDate, valuesDate);

        editor.apply();

    }

    public String Laytoken(Context c, String key) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("saveToken", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(key, "");


        return name;
    }

    public void XoaToken(Context c) {
        SharedPreferences settings = c.getSharedPreferences("saveToken", Context.MODE_PRIVATE);
        //settings.getAll()=null;
    }

    public String getDateDefault() {
        return dateDefault = df.format(calNow.getTimeInMillis() - (60 * 60 * 1000)) + "";  //trừ 1 Gio;
    }

    public void setDateDefault() {

        this.dateDefault = calNow.getTimeInMillis() - (60 * 60 * 1000) + "";  //trừ 1 Gio;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getRetailer() {
        return Retailer;
    }

    public void setRetailer(String retailer) {
        Retailer = retailer;
    }

}
