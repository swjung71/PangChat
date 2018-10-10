package kr.co.digitalanchor.pangchat.model;

import java.util.ArrayList;

/**
 * Created by user on 2016-12-22.
 */

public class ResultMatching extends Result {

    private ArrayList<MatchingUser> users;

    public ArrayList<MatchingUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<MatchingUser> users) {
        this.users = users;
    }
}
