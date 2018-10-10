package kr.co.digitalanchor.pangchat.model;

import android.os.Build;

/**
 * Created by Peter Jung on 2016-11-23.
 */

public class Login {

    //존재하는 것
    //userName이 ID임
    private String userName;
    private String gcmID;
    private String deviceNumber;
    private String phoneNumber;
    private String nationalCode;
    private String appVersion;
    private String passwd;

    private String osVersion = Build.VERSION.RELEASE;
    private String devModel = Build.MODEL;
    private String isAndroid = "1";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGcmID() {
        return gcmID;
    }

    public void setGcmID(String gcmID) {
        this.gcmID = gcmID;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getIsAndroid() {
        return isAndroid;
    }

    public void setIsAndroid(String isAndroid) {
        this.isAndroid = isAndroid;
    }
}
