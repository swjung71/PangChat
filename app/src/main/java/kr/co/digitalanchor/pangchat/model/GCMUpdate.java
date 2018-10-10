package kr.co.digitalanchor.pangchat.model;

/**
 * Created by Peter Jung on 2016-11-21.
 * GCM ID를 update 하기 위한 request model
 */
public class GCMUpdate {

    private String gcm;
    private String userPK;
    private String appVersion;

    public String getGcm() {
        return gcm;
    }

    public void setGcm(String GCM) {
        this.gcm = GCM;
    }

    public String getUserPK() {
        return userPK;
    }

    public void setUserPK(String userPK) {
        this.userPK = userPK;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
