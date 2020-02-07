package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChitietSP_Inventories {

    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("branchId")
    @Expose
    private Integer branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("cost")
    @Expose
    private Float cost;
    @SerializedName("onHand")
    @Expose
    private Integer onHand;
    @SerializedName("reserved")
    @Expose
    private Integer reserved;
    @SerializedName("minQuantity")
    @Expose
    private Integer minQuantity;
    @SerializedName("maxQuantity")
    @Expose
    private Integer maxQuantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
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

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}
