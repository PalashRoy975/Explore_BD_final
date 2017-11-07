package deb.com.showinghotels;

/**
 * Created by mirajul on 10/30/17.
 */

public class Hotels {
    String activities,description,general,image,internet,name,parking;

    public Hotels(String activities, String description, String general, String image, String internet, String name, String parking) {
        this.activities = activities;
        this.description = description;
        this.general = general;
        this.image = image;
        this.internet = internet;
        this.name = name;
        this.parking = parking;
    }

    public Hotels() {
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }
}
