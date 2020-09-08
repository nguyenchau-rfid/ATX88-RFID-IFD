package com.atid.app.atx.ReadJson;

import java.util.List;

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface WebApi {
    //lấy token IFD
    @POST("/connect/token")
    @FormUrlEncoded
    Call<TokenAccess> TokenIFD(@Field("client_id") String clientId,
                               @Field("client_secret") String clientSecret,
                               @Field("grant_type") String grant_type,
                               @Field("scopes") String scopes
    );

    //lấy token KiotViet
    @POST("/connect/token")
    @FormUrlEncoded
    Call<TokenAccess> TokenKiotViet(@Field("client_id") String clientId,
                                    @Field("client_secret") String clientSecret,
                                    @Field("grant_type") String grant_type,
                                    @Field("scopes") String scopes
    );

    @GET("/branches?pageSize=50")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<ListChiNhanh> getAllChiNhanh(@Header("Content-Type") String token,
                                      @Header("Retailer") String Retailer,
                                      @Header("Authorization") String Authorization);

    //Dang nhap user
    @GET("SysUsers")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<User> getAuser(@Query("loginName") String username,
                        @Query("password") String pass,
                        @Header("Authorization") String token);

    //Lay RFIDTasks
    @GET("RFIDTasks")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<RFIDTasksAdapter> getRFIDTasks(@Query("responsibleUserID") double UserID,
                                        @Header("Authorization") String token);

    //POST The ve server
    @FormUrlEncoded
    @POST("RFIDTasks/ReadTag")
    Call<ReadTagAdapter> PostReadTag(
            @Field("rfidTaskID") double Longitude,
            @Field("trackingGPS_Longitude") double Latitude,
            @Field("trackingGPS_Latitude") double TimeL,
            @Field("trackingGPS_Location") String Location,
            @Field("epc") String epc,
            @Header("Authorization") String token

    );

    //POST The ve server
    @Headers({
            "Content-type: application/json"
    })
    @POST("RFIDTasks/ReadTag")
    Call<PostTag> PostReadTagTest(
            @Body PostTag posttag,
            @Header("Authorization") String token

    );

    //thêm nhân viên
    @POST("/api/users/Posttbluser/")
    @FormUrlEncoded
    Call<User> ThemNV(@Field("Usernam") String userName,
                      @Field("Name") String TenNV,
                      @Field("pass") String pass

    );



    // chuyen doi tu RFID sang productcode

    @GET("/api/RFIDTasks/RFIDTagReference/epc/{epc}")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<RFIDTagReference> getRFtoProduct(@Path("epc") String RFIDTag,
    @Header("Authorization") String token);


    // Tìm kiếm sản phẩm từ Productcode
    @GET("/api/RFIDReferences/GettblProductKiot/")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<RfidToProductCode> getFindProduct(@Query("kiot") int productCode);

    //lay ra thong tin chi tiet san pham
    @GET("/products/{productId}")
    Call<ChiTietSP> getChitietSP(@Header("Content-Type") String token,
                                 @Header("Retailer") String Retailer,
                                 @Header("Authorization") String Authorization,
                                 @Path("productId") Double productid
    );

    //thêm thông tin địa điểm
    @POST("/api/InventoryCheckDetails/PosttblInventoryCheckDetail/")
    @FormUrlEncoded
    Call<Location> ThemLocation(@Field("TrackingGPS_Longitude") float Longitude,
                                @Field("TrackingGPS_Latitude") float Latitude,
                                @Field("TrackingGPS_Time") String TimeL,
                                @Field("TrackingGPS_Location") String Location

    );

   /* @GET("/api/Employees/GettblEmployees/")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<List<NhanVien>> getAllNhanVien();*/

    // lay ra 1 nhan vien
   /* @GET("/api/Employees/GettblEmployee/")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<NhanVien> getANhanVien(@Query("EmployeeCode") String emcode);*/



   /* @GET("/api/users/Gettblusers/")
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    Call<List<User>> getAllUser();*/


    // lấy ra 1 nhân viên
  /*  @GET("/customers")
    //@Headers({"Content-Type : application/x-www-form-urlencoded",
    // "Retailer : ifd",
    //   "Authorization : Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE1NDQzMjA2NjIsImV4cCI6MTU0NDQwNzA2MiwiaXNzIjoiaHR0cDovL2lkLmtpb3R2aWV0LnZuIiwiYXVkIjpbImh0dHA6Ly9pZC5raW90dmlldC52bi9yZXNvdXJjZXMiLCJLaW90VmlldC5BcGkuUHVibGljIl0sImNsaWVudF9pZCI6IjNiOTU2NjVhLThjMjgtNDkyZi04NWY1LWQxYTA5NWU1NDFhOSIsImNsaWVudF9SZXRhaWxlckNvZGUiOiJpZmQiLCJjbGllbnRfUmV0YWlsZXJJZCI6IjUxMDczIiwiY2xpZW50X1VzZXJJZCI6IjIxMjk1MiIsInNjb3BlIjpbIlB1YmxpY0FwaS5BY2Nlc3MiXX0.c3I44g4JuCmUImA1_piasi7QDsD3k8gEY_rXiphckQWb5a8hJsmvXtmlDWXfvzuaqncFbzKOoeZzo58DMI0XIWA4n1Nyb0CqFMKKK2xXgsod2aU2giDbJsOythnwMr4ZV0GgkX8uWi7X219TCYh-VrXQ4VgMMHrzmzIU3bmgTHlB5xETIBX6Cq_lEdJ4Uy8EarlfJg_-aoEd3UHoXe4ByzOneF8Wrv6uHhS3uigINGSTRCDZuqAflPT-a-o9oIK0YiE-jTDMNg2AvUiUn2YPGzV1S-JZLcz5sdONkFF-yFUvOUhYOigEftV0kK9wMQ0Za3dhGDuIa0uRMcYOvJ7E3Q"})
    Call<List<NhanVien>> customers(@Header("Content-Type") String token,
                                   @Header("Retailer") String Retailer,
                                   @Header("Authorization") String Authorization
    );*/

    /* @GET("/products?includeInventory=true")
         //@Headers({"Content-Type : application/x-www-form-urlencoded",
         // "Retailer : ifd",
         //   "Authorization : Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE1NDQzMjA2NjIsImV4cCI6MTU0NDQwNzA2MiwiaXNzIjoiaHR0cDovL2lkLmtpb3R2aWV0LnZuIiwiYXVkIjpbImh0dHA6Ly9pZC5raW90dmlldC52bi9yZXNvdXJjZXMiLCJLaW90VmlldC5BcGkuUHVibGljIl0sImNsaWVudF9pZCI6IjNiOTU2NjVhLThjMjgtNDkyZi04NWY1LWQxYTA5NWU1NDFhOSIsImNsaWVudF9SZXRhaWxlckNvZGUiOiJpZmQiLCJjbGllbnRfUmV0YWlsZXJJZCI6IjUxMDczIiwiY2xpZW50X1VzZXJJZCI6IjIxMjk1MiIsInNjb3BlIjpbIlB1YmxpY0FwaS5BY2Nlc3MiXX0.c3I44g4JuCmUImA1_piasi7QDsD3k8gEY_rXiphckQWb5a8hJsmvXtmlDWXfvzuaqncFbzKOoeZzo58DMI0XIWA4n1Nyb0CqFMKKK2xXgsod2aU2giDbJsOythnwMr4ZV0GgkX8uWi7X219TCYh-VrXQ4VgMMHrzmzIU3bmgTHlB5xETIBX6Cq_lEdJ4Uy8EarlfJg_-aoEd3UHoXe4ByzOneF8Wrv6uHhS3uigINGSTRCDZuqAflPT-a-o9oIK0YiE-jTDMNg2AvUiUn2YPGzV1S-JZLcz5sdONkFF-yFUvOUhYOigEftV0kK9wMQ0Za3dhGDuIa0uRMcYOvJ7E3Q"})
     Call<SanPham> listProduct(@Header("Content-Type") String token,
                               @Header("Retailer") String Retailer,
                               @Header("Authorization") String Authorization
     );*/


    //thêm nhân viên
    /*@POST("wws/api/nv/")
    @FormUrlEncoded
    Call<NhanVien> ThemNV(@Field("MaNV") String manv,
                          @Field("TenNV") String TenNV,
                          @Field("DiaChi") String DiaChi,
                          @Field("DienThoai") String DienThoai,
                          @Field("CMND") String CMND
    );*/
}