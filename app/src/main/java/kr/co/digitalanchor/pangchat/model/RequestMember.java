package kr.co.digitalanchor.pangchat.model;

/**
 * Created by user on 2016-12-19.
 */

public class RequestMember {

    private int sex;
    private int city;
    private int from;
    private int end;

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
