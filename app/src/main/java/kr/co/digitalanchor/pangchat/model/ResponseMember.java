package kr.co.digitalanchor.pangchat.model;

import java.util.ArrayList;

/**
 * Created by user on 2016-12-19.
 */

public class ResponseMember extends Result {

    private ArrayList<PangChatUser> users;

    public ArrayList<PangChatUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<PangChatUser> users) {
        this.users = users;
    }
}
