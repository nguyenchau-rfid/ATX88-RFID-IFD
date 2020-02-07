package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RfidToProductCode {
    @SerializedName("Shoes_IsLeftFoot")
    @Expose
    private Boolean Shoes_IsLeftFoot;

    public Boolean getShoes_IsRightFoot() {
        return Shoes_IsRightFoot;
    }

    public void setShoes_IsRightFoot(Boolean shoes_IsRightFoot) {
        Shoes_IsRightFoot = shoes_IsRightFoot;
    }

    private Boolean Shoes_IsRightFoot;
    @SerializedName("Shoes_PairedWithTagID")
    @Expose
    private Integer Shoes_PairedWithTagID;

    public void setRFIDTag(Integer RFIDTag) {
        this.RFIDTag = RFIDTag;
    }

    @SerializedName("RFIDTag")
    @Expose
    private Integer RFIDTag;

    @SerializedName("KiotVietProductID")
    @Expose
    private Integer KiotVietProductID;

    public Integer getRFIDTagCode() {
        return RFIDTag;
    }

    public Integer getProductcodeKiot() {
        return KiotVietProductID;
    }
    public void setKiotVietProductID(Integer kiotVietProductID) {
        KiotVietProductID = kiotVietProductID;
    }
    public Boolean getShoes_IsLeftFoot() {
        return Shoes_IsLeftFoot;
    }
    public void setShoes_IsLeftFoot(Boolean shoes_IsLeftFoot) {
        Shoes_IsLeftFoot = shoes_IsLeftFoot;
    }
    public Integer getShoes_PairedWithTagID() {
        return Shoes_PairedWithTagID;
    }

    public void setShoes_PairedWithTagID(Integer shoes_PairedWithTagID) {
        Shoes_PairedWithTagID = shoes_PairedWithTagID;
    }

}
