package kr.co.digitalanchor.pangchat.model;

import java.util.ArrayList;

/**
 * Created by Peter Jung on 2016-09-14.
 */
public class GuestInfoResult extends Result {

    private ArrayList<GuestInfo> guestInfos;

    public ArrayList<GuestInfo> getGuestInfos() {
        return guestInfos;
    }

    public void setGuestInfos(ArrayList<GuestInfo> guestInfos) {
        this.guestInfos = guestInfos;
    }
}
