package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostTag {


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

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    private double rfidTaskID=0;
        private double trackingGPS_Longitude=0;
        private double trackingGPS_Latitude=0;
        private String epc="";


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

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("didError")
    @Expose
    private Boolean didError;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public ModelReadTagAdapter getModelReadTag() {
        return modelReadTag;
    }

    public void setModelReadTag(ModelReadTagAdapter modelReadTag) {
        this.modelReadTag = modelReadTag;
    }

    @SerializedName("model")
    @Expose

    private ModelReadTagAdapter modelReadTag;

        @Override
        public String toString() {
            return "Posts{" +
                    "userId=" + rfidTaskID +
                    ", id=" + trackingGPS_Longitude +
                    ", title='" + trackingGPS_Latitude + '\'' +
                    ", body='" + epc + '\'' +
                    '}';
        }

}
