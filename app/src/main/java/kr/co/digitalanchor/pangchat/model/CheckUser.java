package kr.co.digitalanchor.pangchat.model;

/**
 * Created by Peter Jung on 2016-11-21.
 * device Number에 해당하는 사용자가 있는지 확인하기 위한 request model
 */
public class CheckUser {

    private String deviceNumber;

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}
