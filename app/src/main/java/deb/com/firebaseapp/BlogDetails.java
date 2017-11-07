package deb.com.firebaseapp;;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by palash on 9/26/17.
 */

public class BlogDetails implements Parcelable {
    String tourId,tourName,tourdis;
    String tourGenre;
    String season;
    float rb;
    String guname;
    String email;
    String guno;
    String gudesc;
    String desc;
    String image;
    private long timeMilli;
    int likes;

    public BlogDetails() {

    }

    public BlogDetails(String image, String tourId, String tourName,String tourdis, String email, String tourGenre, String season, float rb, String guname, String guno, String gudesc, String desc, int likes) {
        this.image = image;
        this.tourId = tourId;
        this.tourName = tourName;
        this.tourdis = tourdis;
        this.tourGenre = tourGenre;
        this.season = season;
        this.rb = rb;
        this.guname = guname;
        this.guno = guno;
        this.gudesc = gudesc;
        this.desc = desc;
        this.email = email;
        this.likes=likes;
        //   this.timeMilli=timeMilli;


    }

    public String getTourdis() {
        return tourdis;
    }

    public void setTourdis(String tourdis) {
        this.tourdis = tourdis;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourGenre() {
        return tourGenre;
    }

    public void setTourGenre(String tourGenre) {
        this.tourGenre = tourGenre;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public float getRb() {
        return rb;
    }

    public void setRb(float rb) {
        this.rb = rb;
    }

    public String getGuname() {
        return guname;
    }

    public void setGuname(String guname) {
        this.guname = guname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuno() {
        return guno;
    }

    public void setGuno(String guno) {
        this.guno = guno;
    }

    public String getGudesc() {
        return gudesc;
    }

    public void setGudesc(String gudesc) {
        this.gudesc = gudesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimeMilli() {
        return timeMilli;
    }

    public void setTimeMilli(long timeMilli) {
        this.timeMilli = timeMilli;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public static Creator<BlogDetails> getCREATOR() {
        return CREATOR;
    }

    protected BlogDetails(Parcel in) {
        image = in.readString();
        tourId = in.readString();
        tourName = in.readString();
        tourdis=in.readString();
        tourGenre = in.readString();
        season = in.readString();
        rb = in.readFloat();
        guname = in.readString();
        email = in.readString();
        guno = in.readString();
        gudesc = in.readString();
        desc = in.readString();
        likes=in.readInt();
        // timeMilli=in.readLong();
    }

    public static final Creator<BlogDetails> CREATOR = new Creator<BlogDetails>() {
        @Override
        public BlogDetails createFromParcel(Parcel in) {
            return new BlogDetails(in);
        }

        @Override
        public BlogDetails[] newArray(int size) {
            return new BlogDetails[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(tourId);
        parcel.writeString(tourName);
        parcel.writeString(tourdis);
        parcel.writeString(tourGenre);
        parcel.writeString(season);
        parcel.writeFloat(rb);
        parcel.writeString(guname);
        parcel.writeString(email);
        parcel.writeString(guno);
        parcel.writeString(gudesc);
        parcel.writeString(desc);
        parcel.writeInt(likes);
        // parcel.writeLong(timeMilli);
    }
}
