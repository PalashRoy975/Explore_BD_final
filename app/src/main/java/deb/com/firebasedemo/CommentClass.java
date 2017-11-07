package deb.com.firebasedemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Deb on 10/9/2017.
 */

public class CommentClass{
    private String Comment, Poster;
    private long TimeMilli;
    public CommentClass() {
    }


    public CommentClass(String comment, String poster, long timeMilli) {
        Comment = comment;
        Poster = poster;
        TimeMilli= timeMilli;
    }


    public long getTimeMilli() {
        return TimeMilli;
    }

    public void setTimeMilli(long timeMilli) {
        TimeMilli = timeMilli;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }
}
