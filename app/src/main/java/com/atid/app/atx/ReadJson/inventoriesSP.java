package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class inventoriesSP {

    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productCode")
    @Expose
    private String productCode;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("branchId")
    @Expose
    private Integer branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("cost")
    @Expose
    private float cost;
    @SerializedName("onHand")
    @Expose
    private Integer onHand=0;
    @SerializedName("reserved")
    @Expose
    private Integer reserved;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Integer getDemsoluong() {
        return demsoluong;
    }

    public void setDemsoluong(Integer demsoluong) {
        this.demsoluong = demsoluong;
    }

    public Integer demsoluong=0;
}