package deb.com.firebasedemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Deb on 10/9/2017.
 */

public class EventClass implements Parcelable{
    private String Title, Date, Image, User, Time, Duration, eventID;
    private int Likes, Space, Going;
    private long TimeMilli;

    public EventClass() {
    }

    public EventClass(String title, String date, String image, String user, String time, String duration, String id, int likes, long timeMilli, int space, int going) {
        Title = title;
        Date = date;
        Image = image;
        Time=time;
        User=user;
        eventID=id;
        Duration=duration;
        Likes=likes;
        TimeMilli=timeMilli;
        Space= space;
        Going=going;

    }

    protected EventClass(Parcel in) {
        Title = in.readString();
        Date = in.readString();
        Image = in.readString();
        User = in.readString();
        Time = in.readString();
        Duration = in.readString();
        eventID = in.readString();
        Likes = in.readInt();
        TimeMilli=in.readLong();
        Space = in.readInt();
        Going = in.readInt();
    }

    public static final Creator<EventClass> CREATOR = new Creator<EventClass>() {
        @Override
        public EventClass createFromParcel(Parcel in) {
            return new EventClass(in);
        }

        @Override
        public EventClass[] newArray(int size) {
            return new EventClass[size];
        }
    };


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getSpace() {
        return Space;
    }

    public void setSpace(int space) {
        Space = space;
    }

    public int getGoing() {
        return Going;
    }

    public void setGoing(int going) {
        Going = going;
    }

    public long getTimeMilli() {
        return TimeMilli;
    }

    public void setTimeMilli(long timeMilli) {
        TimeMilli = timeMilli;
    }

    public static Creator<EventClass> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Title);
        parcel.writeString(Date);
        parcel.writeString(Image);
        parcel.writeString(User);
        parcel.writeString(Time);
        parcel.writeString(Duration);
        parcel.writeString(eventID);
        parcel.writeInt(Likes);
        parcel.writeLong(TimeMilli);
        parcel.writeInt(Space);
        parcel.writeInt(Going);
    }
}
