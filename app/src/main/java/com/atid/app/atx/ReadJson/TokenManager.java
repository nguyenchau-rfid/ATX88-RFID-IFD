package com.atid.app.atx.ReadJson;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TokenManager {
    private String Content = "application/json";
    private String Retailer = "ifd";
    private String client_id = "3b95665a-8c28-492f-85f5-d1a095e541a9";
    private String client_secret = "5D593843B1E1AF345598ADD93B484171C058A367";
    private String grant_type = "client_credentials";
    private String scopes = "PublicApi.Access";
    private String Authorization = "Bearer ";
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat simpleday = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Date dateSave = null;
    private Date dateToday = null;
    private Calendar calNow = Calendar.getInstance();
    private String dateT = df.format(calNow.getTime());
    private String dateDefault = "";
    private String dateFormat="";

    public String getDateFormat() {
        return dateFormat;
    }

    public String setDateFormat(Long dateConvert) {
         return dateFormat =df.format(dateConvert);
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


    public String getDateT() {
        return dateT;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getGio() {
        return simpleday.format(calNow.getTime());
    }

    public boolean checkNgay(String dateS) {
        try {
            df.setLenient(false);
            dateSave = df.parse(dateS);
            dateToday = df.parse(dateT);
            if (dateSave.before(dateToday))
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
        return dateDefault = df.format(calNow.getTimeInMillis() - (60 * 60 * 24 * 1000)) + "";  //trừ 1 ngày;
    }

    public void setDateDefault() {

        this.dateDefault = calNow.getTimeInMillis() - (60 * 60 * 24 * 1000) + "";  //trừ 1 ngày;
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
