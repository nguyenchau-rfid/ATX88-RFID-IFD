package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RFIDTagReference {
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
    private ModelRFIDTagReference model;

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

    public ModelRFIDTagReference getModel() {
        return model;
    }

    public void setModel(ModelRFIDTagReference model) {
        this.model = model;
    }

}

