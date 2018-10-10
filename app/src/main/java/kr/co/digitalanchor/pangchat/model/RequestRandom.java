package kr.co.digitalanchor.pangchat.model;

/**
 * Created by user on 2016-12-23.
 */

public class RequestRandom {

    private int userID;
    private int sex;
    private int city;
    private int interest;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }
}
