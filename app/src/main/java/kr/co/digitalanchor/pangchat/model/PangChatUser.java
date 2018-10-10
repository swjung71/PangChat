package kr.co.digitalanchor.pangchat.model;

import java.io.Serializable;

/**
 * Created by Peter Jung on 2016-09-11.
 */
public class PangChatUser implements Serializable {

    private static final long serialVersionUID=1L;

    private String alias;
    private Integer userID;
    private Integer sex;
    private Integer isOnAir;//0이면 on 1이면 off, 2이면 화상중,
    private String imageURL;
    private String city;//현재 나의 도시
    private int cityID;
    private Integer age;
    private int interestAgeID;
    private int interestSexID;
    private int interestSubjectID;
    private int jobID;
    private int numOfLike;
    private int numOfFriend;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsOnAir() {
        return isOnAir;
    }

    public void setIsOnAir(Integer isOnAir) {
        this.isOnAir = isOnAir;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getInterestAgeID() {
        return interestAgeID;
    }

    public void setInterestAgeID(int interestAgeID) {
        this.interestAgeID = interestAgeID;
    }

    public int getInterestSexID() {
        return interestSexID;
    }

    public void setInterestSexID(int interestSexID) {
        this.interestSexID = interestSexID;
    }

    public int getInterestSubjectID() {
        return interestSubjectID;
    }

    public void setInterestSubjectID(int interestSubjectID) {
        this.interestSubjectID = interestSubjectID;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getNumOfLike() {
        return numOfLike;
    }

    public void setNumOfLike(int numOfLike) {
        this.numOfLike = numOfLike;
    }

    public int getNumOfFriend() {
        return numOfFriend;
    }

    public void setNumOfFriend(int numOfFriend) {
        this.numOfFriend = numOfFriend;
    }
}
