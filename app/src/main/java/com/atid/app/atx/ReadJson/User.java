package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("IDLogin")
    @Expose
    private Integer iDLogin;
    @SerializedName("Usernam")
    @Expose
    private String usernam;

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("IsAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("pass")
    @Expose
    private String pass;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getIDLogin() {
        return iDLogin;
    }

    public void setIDLogin(Integer iDLogin) {
        this.iDLogin = iDLogin;
    }

    public String getUsernam() {
        return usernam;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}