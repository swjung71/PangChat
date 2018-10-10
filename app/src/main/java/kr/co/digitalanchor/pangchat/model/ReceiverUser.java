package kr.co.digitalanchor.pangchat.model;

import android.os.Build;

/**
 * Created by Peter Jung on 2016-11-24.
 */

public class ReceiverUser {

    private String userName;
    private String userAlias;
    private int age;
    private int sex;// 0 남자, 1 여자
    private String subject;
    private int subjectID;
    private String gcmID;
    private String deviceNumber;
    private String phoneNumber;
    private String osVersion = Build.VERSION.RELEASE;
    private String devModel = Build.MODEL;
    private String isAndroid = "1";
    private String nationalCode;
    private String imagePath;
    private String appVersion;
    private String job;
    private int jobID;
    private String city;
    private int cityID;
    private String passwd;
    private String nation;
    private int nationID;
    private String interestAge;
    private int interestAgeID;
    private String interestSex;
    private int interestSexID;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getGcmID() {
        return gcmID;
    }

    public void setGcmID(String gcmID) {
        this.gcmID = gcmID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
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

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getNationID() {
        return nationID;
    }

    public void setNationID(int nationID) {
        this.nationID = nationID;
    }

    public String getInterestAge() {
        return interestAge;
    }

    public void setInterestAge(String interestAge) {
        this.interestAge = interestAge;
    }

    public int getInterestAgeID() {
        return interestAgeID;
    }

    public void setInterestAgeID(int interestAgeID) {
        this.interestAgeID = interestAgeID;
    }

    public String getInterestSex() {
        return interestSex;
    }

    public void setInterestSex(String interestSex) {
        this.interestSex = interestSex;
    }

    public int getInterestSexID() {
        return interestSexID;
    }

    public void setInterestSexID(int interestSexID) {
        this.interestSexID = interestSexID;
    }
}
