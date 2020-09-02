package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUser {


        @SerializedName("userID")
        @Expose
        private Double userID;
        @SerializedName("createDate")
        @Expose
        private String createDate;
        @SerializedName("brancheID")
        @Expose
        private Object brancheID;
        @SerializedName("employeeID")
        @Expose
        private Object employeeID;
        @SerializedName("groupID")
        @Expose
        private Double groupID;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("userLoginName")
        @Expose
        private String userLoginName;
        @SerializedName("userEMail")
        @Expose
        private String userEMail;
        @SerializedName("userCellPhone")
        @Expose
        private String userCellPhone;
        @SerializedName("userPwd")
        @Expose
        private String userPwd;
        @SerializedName("isLock")
        @Expose
        private Object isLock;
        @SerializedName("isAdmin")
        @Expose
        private Object isAdmin;
        @SerializedName("kiotVietUserID")
        @Expose
        private Double kiotVietUserID;
        @SerializedName("sysUserRightsForForms")
        @Expose
        private Object sysUserRightsForForms;
        @SerializedName("sysUserGroup")
        @Expose
        private Object sysUserGroup;

        public Double getUserID() {
            return userID;
        }

        public void setUserID(Double userID) {
            this.userID = userID;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public Object getBrancheID() {
            return brancheID;
        }

        public void setBrancheID(Object brancheID) {
            this.brancheID = brancheID;
        }

        public Object getEmployeeID() {
            return employeeID;
        }

        public void setEmployeeID(Object employeeID) {
            this.employeeID = employeeID;
        }

        public Double getGroupID() {
            return groupID;
        }

        public void setGroupID(Double groupID) {
            this.groupID = groupID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserLoginName() {
            return userLoginName;
        }

        public void setUserLoginName(String userLoginName) {
            this.userLoginName = userLoginName;
        }

        public String getUserEMail() {
            return userEMail;
        }

        public void setUserEMail(String userEMail) {
            this.userEMail = userEMail;
        }

        public String getUserCellPhone() {
            return userCellPhone;
        }

        public void setUserCellPhone(String userCellPhone) {
            this.userCellPhone = userCellPhone;
        }

        public String getUserPwd() {
            return userPwd;
        }

        public void setUserPwd(String userPwd) {
            this.userPwd = userPwd;
        }

        public Object getIsLock() {
            return isLock;
        }

        public void setIsLock(Object isLock) {
            this.isLock = isLock;
        }

        public Object getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Object isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Double getKiotVietUserID() {
            return kiotVietUserID;
        }

        public void setKiotVietUserID(Double kiotVietUserID) {
            this.kiotVietUserID = kiotVietUserID;
        }

        public Object getSysUserRightsForForms() {
            return sysUserRightsForForms;
        }

        public void setSysUserRightsForForms(Object sysUserRightsForForms) {
            this.sysUserRightsForForms = sysUserRightsForForms;
        }

        public Object getSysUserGroup() {
            return sysUserGroup;
        }

        public void setSysUserGroup(Object sysUserGroup) {
            this.sysUserGroup = sysUserGroup;
        }
}
