package kr.co.digitalanchor.pangchat.model;

/**
 * Created by Thomas on 2015-08-27.
 */
//@Root(name = "Good", strict = false)
public class Item {

    //@Element(name = "GoodName", required = false)
    String name;

    //@Element(name = "GoodID", required = false)
    String key;

    //@Element(name = "Desc", required = false)
    String info;

    //@Element(name = "Price", required = false)
    String cost;

    //@Element(name = "Heart", required = false)
    String heart;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }
}
