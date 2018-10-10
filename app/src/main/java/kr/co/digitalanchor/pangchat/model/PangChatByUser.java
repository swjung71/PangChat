package kr.co.digitalanchor.pangchat.model;

/**
 * Created by user on 2016-12-22.
 */

public class PangChatByUser {

    private int sex;
    private int age;
    private String userAlias;
    private String userID;
    private String imageURL;
    private String city;
    private String receiveAlias;
    private String receiveID;
    private String channelID;

    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getChannelID() {
        return channelID;
    }
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
    public String getUserAlias() {
        return userAlias;
    }
    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
    public String getReceiveAlias() {
        return receiveAlias;
    }
    public void setReceiveAlias(String receiveAlias) {
        this.receiveAlias = receiveAlias;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(String receiveID) {
        this.receiveID = receiveID;
    }
}
