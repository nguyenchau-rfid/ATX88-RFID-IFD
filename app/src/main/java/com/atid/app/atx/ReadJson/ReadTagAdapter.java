package com.atid.app.atx.ReadJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReadTagAdapter {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("didError")
    @Expose
    private Boolean didError;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    @SerializedName("model")
    @Expose

    double rfidTaskID;
    double        trackingGPS_Longitude;

    public double getRfidTaskID() {
        return rfidTaskID;
    }

    public void setRfidTaskID(double rfidTaskID) {
        this.rfidTaskID = rfidTaskID;
    }

    public double getTrackingGPS_Longitude() {
        return trackingGPS_Longitude;
    }

    public void setTrackingGPS_Longitude(double trackingGPS_Longitude) {
        this.trackingGPS_Longitude = trackingGPS_Longitude;
    }

    public double getTrackingGPS_Latitude() {
        return trackingGPS_Latitude;
    }

    public void setTrackingGPS_Latitude(double trackingGPS_Latitude) {
        this.trackingGPS_Latitude = trackingGPS_Latitude;
    }

    public double getTrackingGPS_Location() {
        return trackingGPS_Location;
    }

    public void setTrackingGPS_Location(double trackingGPS_Location) {
        this.trackingGPS_Location = trackingGPS_Location;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    double trackingGPS_Latitude;
    double     trackingGPS_Location;
    String epc;

    private ArrayList<ModelReadTagAdapter> model=null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDidError() {
        return didError;
    }

    public void setDidError(Boolean didError) {
        this.didError = didError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<ModelReadTagAdapter> getModel() {
        return model;
    }

    public void setModel(ArrayList<ModelReadTagAdapter> model) {
        this.model = model;
    }

}
