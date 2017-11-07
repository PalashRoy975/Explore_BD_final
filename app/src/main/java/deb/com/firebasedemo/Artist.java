package deb.com.firebasedemo;

import android.widget.RatingBar;

/**
 * Created by palash on 9/26/17.
 */

public class Artist
{
    String artistId;
    String artistName;
    String artistGenre;
    String Season;
    float rb;
    String Guname;
    String Guno;
    String Gudesc;
    public Artist()
    {

    }

    public Artist(String artistId, String artistName, String artistGenre, String Season, float rb, String Guname, String Guno, String Gudesc) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
        this.Season= Season;
        this.rb=rb;
        this.Guname = Guname;
        this.Guno = Guno;
        this.Gudesc = Gudesc;


    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public String getSeason() {
        return Season;
    }

    public float getRb() {
        return rb;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistGenre(String artistGenre) {
        this.artistGenre = artistGenre;
    }

    public void setSeason(String season) {
        Season = season;
    }

    public void setRb(float rb) {
        this.rb = rb;
    }

    public String getGuname() {
        return Guname;
    }

    public void setGuname(String guname) {
        Guname = guname;
    }

    public String getGuno() {
        return Guno;
    }

    public void setGuno(String guno) {
        Guno = guno;
    }

    public String getGudesc() {
        return Gudesc;
    }

    public void setGudesc(String gudesc) {
        Gudesc = gudesc;
    }
}
