package kr.co.digitalanchor.pangchat.model;

import android.os.Build;

/**
 * Created by Xian on 2016-08-14.
 */
public class UpdateUser {

    //존재하는 것
    private int userPK;
    private String userAlias;
    private int age;
    private String subject;
    private String gcmID;
    private String phoneNumber;
    private String appVersion;//AppVersion
    private String isAndroid = "1";
    private String nationalCode;
    private String imagePath;
    private String deviceNumber;
    private String job;
    private String city;
    private int subjectID;
    private int cityID;
    private int jobID;
    private String passwd;
    private String nation;
    private int nationID;
    private String interestAge;
    private int interestAgeID;
    private String interestSex;
    private int interestSexID;
    private String osVersion = Build.VERSION.RELEASE;
    private String devModel = Build.MODEL;

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

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getGcmID() {
        return gcmID;
    }
    public void setGcmID(String gcmID) {
        this.gcmID = gcmID;
    }

    public String getOsVersion() {
        return osVersion;
    }
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    public String getDevModel() {
        return devModel;
    }
    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getDeviceNumber() {
        return deviceNumber;
    }
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getUserPK() {
        return userPK;
    }

    public void setUserPK(int userPK) {
        this.userPK = userPK;
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

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public int getInterestSexID() {
        return interestSexID;
    }

    public void setInterestSexID(int interestSexID) {
        this.interestSexID = interestSexID;
    }

}
