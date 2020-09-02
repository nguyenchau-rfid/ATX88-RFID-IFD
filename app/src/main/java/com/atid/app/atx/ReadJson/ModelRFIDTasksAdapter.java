package com.atid.app.atx.ReadJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRFIDTasksAdapter {

    @SerializedName("rfidTaskID")
    @Expose
    private Double rfidTaskID;
    @SerializedName("isDataReadIn")
    @Expose
    private Boolean isDataReadIn;
    @SerializedName("voucherDate")
    @Expose
    private String voucherDate;
    @SerializedName("statusID")
    @Expose
    private Integer statusID;
    @SerializedName("statusDescription")
    @Expose
    private String statusDescription;
    @SerializedName("deletedByUserID")
    @Expose
    private Object deletedByUserID;
    @SerializedName("deletedByUserName")
    @Expose
    private Object deletedByUserName;
    @SerializedName("deletedDate")
    @Expose
    private Object deletedDate;
    @SerializedName("workStationEditTimeLock")
    @Expose
    private Object workStationEditTimeLock;
    @SerializedName("workStationInstanceID")
    @Expose
    private Object workStationInstanceID;
    @SerializedName("workStationHostName")
    @Expose
    private Object workStationHostName;
    @SerializedName("workStationIPAddress")
    @Expose
    private Object workStationIPAddress;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("wareHouseID")
    @Expose
    private Object wareHouseID;
    @SerializedName("wareHouseName")
    @Expose
    private Object wareHouseName;
    @SerializedName("responsibleUserID")
    @Expose
    private Double responsibleUserID;
    @SerializedName("responsibleUserName")
    @Expose
    private String responsibleUserName;
    @SerializedName("responsibleUserLoginName")
    @Expose
    private String responsibleUserLoginName;
    @SerializedName("voucherIDInventoryCheck")
    @Expose
    private Object voucherIDInventoryCheck;
    @SerializedName("voucherIDRFIDAssign")
    @Expose
    private Double voucherIDRFIDAssign;
    @SerializedName("voucherIDSaleEvent")
    @Expose
    private Object voucherIDSaleEvent;
    @SerializedName("voucherIDWHExport")
    @Expose
    private Object voucherIDWHExport;
    @SerializedName("voucherIDWHImport")
    @Expose
    private Object voucherIDWHImport;
    @SerializedName("voucherID")
    @Expose
    private Double voucherID;
    @SerializedName("voucherNumber")
    @Expose
    private String voucherNumber;
    @SerializedName("rfidTaskDataRead_InventoryChecks")
    @Expose
    private Object rfidTaskDataReadInventoryChecks;
    @SerializedName("rfidTaskDataRead_WHExports")
    @Expose
    private Object rfidTaskDataReadWHExports;
    @SerializedName("rfidTaskDataRead_WHImports")
    @Expose
    private Object rfidTaskDataReadWHImports;
    @SerializedName("rfidAssignVoucherTagGens")
    @Expose
    private Object rfidAssignVoucherTagGens;
    @SerializedName("voucherType")
    @Expose
    private Integer voucherType;
    @SerializedName("voucherTypeDesc")
    @Expose
    private String voucherTypeDesc;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("createdByUserID")
    @Expose
    private String createdByUserID;
    @SerializedName("createdByUserName")
    @Expose
    private String createdByUserName;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;


    public Double getRfidTaskID() {
        return rfidTaskID;
    }

    public void setRfidTaskID(Double rfidTaskID) {
        this.rfidTaskID = rfidTaskID;
    }

    public Boolean getIsDataReadIn() {
        return isDataReadIn;
    }

    public void setIsDataReadIn(Boolean isDataReadIn) {
        this.isDataReadIn = isDataReadIn;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Object getDeletedByUserID() {
        return deletedByUserID;
    }

    public void setDeletedByUserID(Object deletedByUserID) {
        this.deletedByUserID = deletedByUserID;
    }

    public Object getDeletedByUserName() {
        return deletedByUserName;
    }

    public void setDeletedByUserName(Object deletedByUserName) {
        this.deletedByUserName = deletedByUserName;
    }

    public Object getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Object deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Object getWorkStationEditTimeLock() {
        return workStationEditTimeLock;
    }

    public void setWorkStationEditTimeLock(Object workStationEditTimeLock) {
        this.workStationEditTimeLock = workStationEditTimeLock;
    }

    public Object getWorkStationInstanceID() {
        return workStationInstanceID;
    }

    public void setWorkStationInstanceID(Object workStationInstanceID) {
        this.workStationInstanceID = workStationInstanceID;
    }

    public Object getWorkStationHostName() {
        return workStationHostName;
    }

    public void setWorkStationHostName(Object workStationHostName) {
        this.workStationHostName = workStationHostName;
    }

    public Object getWorkStationIPAddress() {
        return workStationIPAddress;
    }

    public void setWorkStationIPAddress(Object workStationIPAddress) {
        this.workStationIPAddress = workStationIPAddress;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getWareHouseID() {
        return wareHouseID;
    }

    public void setWareHouseID(Object wareHouseID) {
        this.wareHouseID = wareHouseID;
    }

    public Object getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(Object wareHouseName) {
        this.wareHouseName = wareHouseName;
    }

    public Double getResponsibleUserID() {
        return responsibleUserID;
    }

    public void setResponsibleUserID(Double responsibleUserID) {
        this.responsibleUserID = responsibleUserID;
    }

    public String getResponsibleUserName() {
        return responsibleUserName;
    }

    public void setResponsibleUserName(String responsibleUserName) {
        this.responsibleUserName = responsibleUserName;
    }

    public String getResponsibleUserLoginName() {
        return responsibleUserLoginName;
    }

    public void setResponsibleUserLoginName(String responsibleUserLoginName) {
        this.responsibleUserLoginName = responsibleUserLoginName;
    }

    public Object getVoucherIDInventoryCheck() {
        return voucherIDInventoryCheck;
    }

    public void setVoucherIDInventoryCheck(Object voucherIDInventoryCheck) {
        this.voucherIDInventoryCheck = voucherIDInventoryCheck;
    }

    public Double getVoucherIDRFIDAssign() {
        return voucherIDRFIDAssign;
    }

    public void setVoucherIDRFIDAssign(Double voucherIDRFIDAssign) {
        this.voucherIDRFIDAssign = voucherIDRFIDAssign;
    }

    public Object getVoucherIDSaleEvent() {
        return voucherIDSaleEvent;
    }

    public void setVoucherIDSaleEvent(Object voucherIDSaleEvent) {
        this.voucherIDSaleEvent = voucherIDSaleEvent;
    }

    public Object getVoucherIDWHExport() {
        return voucherIDWHExport;
    }

    public void setVoucherIDWHExport(Object voucherIDWHExport) {
        this.voucherIDWHExport = voucherIDWHExport;
    }

    public Object getVoucherIDWHImport() {
        return voucherIDWHImport;
    }

    public void setVoucherIDWHImport(Object voucherIDWHImport) {
        this.voucherIDWHImport = voucherIDWHImport;
    }

    public Double getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(Double voucherID) {
        this.voucherID = voucherID;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Object getRfidTaskDataReadInventoryChecks() {
        return rfidTaskDataReadInventoryChecks;
    }

    public void setRfidTaskDataReadInventoryChecks(Object rfidTaskDataReadInventoryChecks) {
        this.rfidTaskDataReadInventoryChecks = rfidTaskDataReadInventoryChecks;
    }

    public Object getRfidTaskDataReadWHExports() {
        return rfidTaskDataReadWHExports;
    }

    public void setRfidTaskDataReadWHExports(Object rfidTaskDataReadWHExports) {
        this.rfidTaskDataReadWHExports = rfidTaskDataReadWHExports;
    }

    public Object getRfidTaskDataReadWHImports() {
        return rfidTaskDataReadWHImports;
    }

    public void setRfidTaskDataReadWHImports(Object rfidTaskDataReadWHImports) {
        this.rfidTaskDataReadWHImports = rfidTaskDataReadWHImports;
    }

    public Object getRfidAssignVoucherTagGens() {
        return rfidAssignVoucherTagGens;
    }

    public void setRfidAssignVoucherTagGens(Object rfidAssignVoucherTagGens) {
        this.rfidAssignVoucherTagGens = rfidAssignVoucherTagGens;
    }

    public Integer getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(Integer voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherTypeDesc() {
        return voucherTypeDesc;
    }

    public void setVoucherTypeDesc(String voucherTypeDesc) {
        this.voucherTypeDesc = voucherTypeDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(String createdByUserID) {
        this.createdByUserID = createdByUserID;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
