package kr.co.digitalanchor.pangchat.model;

/**
 * Created by user on 2016-12-22.
 */

public class RequestMatching {
    private int userID;
    private int from;
    private int end;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
