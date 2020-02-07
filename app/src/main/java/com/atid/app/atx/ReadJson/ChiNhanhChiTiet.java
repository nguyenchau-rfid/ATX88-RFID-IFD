package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChiNhanhChiTiet {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("wardName")
    @Expose
    private String wardName;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("retailerId")
    @Expose
    private Integer retailerId;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    private Object  maChinhanh;
    public Object  getMaChinhanh() {
        return maChinhanh;
    }
    @Override
    public String toString() {
        return branchName ;
    }
    public void setMaChinhanh(String maChinhanh) {
        this.maChinhanh = maChinhanh;
    }
    public ChiNhanhChiTiet(String brandname,Object macn )
    {
        this.branchName=brandname;
        this.maChinhanh=macn;

    }
}
