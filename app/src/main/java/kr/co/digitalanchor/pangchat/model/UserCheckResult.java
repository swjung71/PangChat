package kr.co.digitalanchor.pangchat.model;

/**
 * Created by Peter Jung on 2016-08-12.
 * 서버에 사용자가 저장되어 있는 경우 user에 사용자 정보를 담아서 보냄
 */
public class UserCheckResult extends Result {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
