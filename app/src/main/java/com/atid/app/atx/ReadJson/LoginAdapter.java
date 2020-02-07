package com.atid.app.atx.ReadJson;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginAdapter implements Parcelable {
    static String Username;
    static String Password;
    static String maCodeSP;
    static String brandname="";
    static int branhid;

    public LoginAdapter() {

    }
    public LoginAdapter(String username, String password, int brandid,String brandname) {
        this.Username = username;
        this.Password = password;
        this.branhid = brandid;
        this.brandname=brandname;

    }
    public LoginAdapter(String maCodeSP) {
        this.maCodeSP = maCodeSP;
    }

    protected LoginAdapter(Parcel in) {
        Username = in.readString();
        Password = in.readString();
        maCodeSP = in.readString();
        branhid = in.readInt();
        brandname=in.readString();
    }

    public static final Creator<LoginAdapter> CREATOR = new Creator<LoginAdapter>() {
        @Override
        public LoginAdapter createFromParcel(Parcel in) {
            return new LoginAdapter(in);
        }

        @Override
        public LoginAdapter[] newArray(int size) {
            return new LoginAdapter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Username);
        dest.writeString(Password);
        dest.writeString(maCodeSP);
        dest.writeString(brandname);
        dest.writeInt(branhid);
    }
    public int getBrandid() {
        return this.branhid;
    }
    public String getBrandname(){
        return this.brandname;
    }
    public String getmaCodeSP(){
        return this.maCodeSP;
    }
}
