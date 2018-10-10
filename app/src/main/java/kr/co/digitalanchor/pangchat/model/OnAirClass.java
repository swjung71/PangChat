package kr.co.digitalanchor.pangchat.model;

/**
 * Created by user on 2016-12-24.
 */

public class OnAirClass {

    private int isOnAir; //0 on, 1 off, 2 onAir;
    private int userID;

    public int getIsOnAir() {
        return isOnAir;
    }
    public void setIsOnAir(int isOnAir) {
        this.isOnAir = isOnAir;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
