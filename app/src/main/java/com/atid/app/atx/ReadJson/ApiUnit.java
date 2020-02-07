package com.atid.app.atx.ReadJson;

public class ApiUnit {
    private ApiUnit() {}


    public static final String BASE_URL = "http://rfid.somee.com/";
   // public static final String BASE_URL = "http://66.66.66.52:3804/";
    public static final String BASE_Token = "https://id.kiotviet.vn";
    public static final String BASE_Product = "https://public.kiotapi.com";


    public static WebApi getNVService() {

        return RetrofitClient.getClient(BASE_URL).create(WebApi.class);
    }
    public static WebApi getToketService() {

        return RetrofitClient.getClient(BASE_Token).create(WebApi.class);
    }
    public static WebApi getServicesKiot() {

        return RetrofitClient.getClient(BASE_Product).create(WebApi.class);
    }

}
