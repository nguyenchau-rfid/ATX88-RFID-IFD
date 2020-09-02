package com.atid.app.atx.ReadJson;

public class ApiUnit {
    private ApiUnit() {}


    public static final String BASE_URL_IFD = "https://api.ifd.vn/api/";
   // public static final String BASE_URL = "http://66.66.66.52:3804/";
    public static final String BASE_Token_Ifd = "https://api.ifd.vn/";
    public static final String BASE_Token_KiotViet = "https://id.kiotviet.vn/";
    public static final String BASE_URL_KIOT = "https://public.kiotapi.com";


    public static WebApi getServiceIFD() {

        return RetrofitClient.getClient(BASE_URL_IFD).create(WebApi.class);
    }
    public static WebApi getTokenIfd() {

        return RetrofitClient.getClient(BASE_Token_Ifd).create(WebApi.class);
    }
    public static WebApi getTokenKiot() {

        return RetrofitClient.getClient(BASE_Token_KiotViet).create(WebApi.class);
    }
    public static WebApi getServicesKiot() {

        return RetrofitClient.getClient(BASE_URL_KIOT).create(WebApi.class);
    }

}
