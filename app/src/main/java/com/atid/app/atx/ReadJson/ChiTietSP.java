package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChiTietSP {

    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("retailerId")
    @Expose
    private Integer retailerId;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("name")
    @Expose
    private String name;

    public String getCodeRFID() {
        return codeRFID;
    }

    public void setCodeRFID(String codeRFID) {
        this.codeRFID = codeRFID;
    }

    @SerializedName("codeRFID")
    @Expose
    private String codeRFID;

    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("allowsSale")
    @Expose
    private Boolean allowsSale;
    @SerializedName("hasVariants")
    @Expose
    private Boolean hasVariants;
    @SerializedName("basePrice")
    @Expose
    private Integer basePrice;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("conversionValue")
    @Expose
    private Integer conversionValue;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isRewardPoint")
    @Expose
    private Boolean isRewardPoint;

    @SerializedName("inventories")
    @Expose
    public ArrayList<ChitietSP_Inventories> inventories=null;
    public ArrayList<ChitietSP_Inventories> getInv() {
        return inventories;
    }
    public Integer demsoluong=0;
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getAllowsSale() {
        return allowsSale;
    }

    public void setAllowsSale(Boolean allowsSale) {
        this.allowsSale = allowsSale;
    }

    public Boolean getHasVariants() {
        return hasVariants;
    }

    public void setHasVariants(Boolean hasVariants) {
        this.hasVariants = hasVariants;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(Integer conversionValue) {
        this.conversionValue = conversionValue;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsRewardPoint() {
        return isRewardPoint;
    }

    public void setIsRewardPoint(Boolean isRewardPoint) {
        this.isRewardPoint = isRewardPoint;
    }
    public Integer getDemsoluong() {
        return demsoluong;
    }

    public void setDemsoluong(Integer demsoluong) {
        this.demsoluong = demsoluong;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    //setCost //setProductName setOnHand
    private Integer cost;

    private Integer onHand;

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }








}
