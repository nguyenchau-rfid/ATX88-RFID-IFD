package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListChiNhanh {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;

    public ArrayList<ChiNhanhChiTiet> getChiNhanhChiTiet() {
        return ChiNhanhChiTiet;
    }

    public void setChiNhanhChiTiet(ArrayList<ChiNhanhChiTiet> chiNhanhChiTiet) {
        ChiNhanhChiTiet = chiNhanhChiTiet;
    }

    @SerializedName("data")
    @Expose
    private ArrayList<ChiNhanhChiTiet> ChiNhanhChiTiet = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


}
