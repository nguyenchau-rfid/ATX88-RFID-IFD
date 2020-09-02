package com.atid.app.atx.ReadJson;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginAdapter implements Parcelable {
    static String Username="";
    static String Password="";
    static double maUserID;
    static double Longitude;
    static double Latitude;

    public static double getLongitude() {
        return Longitude;
    }

    public static void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public static double getLatitude() {
        return Latitude;
    }

    public static void setLatitude(double latitude) {
        Latitude = latitude;
    }



    public LoginAdapter() {

    }
    public LoginAdapter(String username, String password, double maUserID,double maLongitude,double maLatitude) {
        this.Username = username;
        this.Password = password;
        this.maUserID = maUserID;
        this.Longitude=maLongitude;
        this.Latitude=maLatitude;


    }

    public void setmaUserID(double maUserID) {
        this.maUserID = maUserID;
    }

    protected LoginAdapter(Parcel in) {
        Username = in.readString();
        Password = in.readString();
        maUserID = in.readDouble();
        Longitude= in.readDouble();
        Latitude= in.readDouble();
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
        dest.writeDouble(maUserID);
        dest.writeDouble(Longitude);
        dest.writeDouble(Latitude);
    }
    public double getmaUserID() {
        return this.maUserID;
    }


}
