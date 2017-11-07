package deb.com.firebasedemo;

/**
 * Created by Deb on 11/1/2017.
 */

public class Noti {
    private String postKey, place, date;

    public Noti() {
    }

    public Noti(String postKey, String place, String date) {
        this.postKey = postKey;
        this.place = place;
        this.date = date;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
