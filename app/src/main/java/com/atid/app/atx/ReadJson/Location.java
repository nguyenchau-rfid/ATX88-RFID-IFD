package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("TrackingGPS_Longitude")
    @Expose
    private float TrackingGPS_Longitude;

    @SerializedName("TrackingGPS_Latitude")
    @Expose
    private float TrackingGPS_Latitude;

    @SerializedName("TrackingGPS_Time")
    @Expose
    private String TrackingGPS_Time;

    @SerializedName("TrackingGPS_Location")
    @Expose
    private String TrackingGPS_Location;
}
