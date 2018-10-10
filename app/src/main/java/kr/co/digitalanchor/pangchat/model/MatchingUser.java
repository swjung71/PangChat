package kr.co.digitalanchor.pangchat.model;

/**
 * Created by user on 2016-12-20.
 */

public class MatchingUser extends PangChatUser {

    private int matchingID;
    private long chatTime;

    public long getChatTime() {
        return chatTime;
    }

    public void setChatTime(long chatTime) {
        this.chatTime = chatTime;
    }

    public int getMatchingID() {
        return matchingID;
    }
    public void setMatchingID(int matchingID) {
        this.matchingID = matchingID;
    }
}
