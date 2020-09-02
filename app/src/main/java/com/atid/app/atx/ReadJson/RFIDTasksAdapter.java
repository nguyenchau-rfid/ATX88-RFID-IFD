package com.atid.app.atx.ReadJson;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RFIDTasksAdapter {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("didError")
    @Expose
    private Boolean didError;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("model")
    @Expose
    private ArrayList<ModelRFIDTasksAdapter> model = null;

    public ArrayList<ModelRFIDTasksAdapter> getModelRFIDTasksAdapter() {
        return model;
    }

    public void setModelRFIDTasksAdapter(ArrayList<ModelRFIDTasksAdapter> model) {
        this.model = model;
    }

    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("itemsCount")
    @Expose
    private Integer itemsCount;
    @SerializedName("pageCount")
    @Expose
    private Double pageCount;

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

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }



    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public Double getPageCount() {
        return pageCount;
    }

    public void setPageCount(Double pageCount) {
        this.pageCount = pageCount;
    }

}

