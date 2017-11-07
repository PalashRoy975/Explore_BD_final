package deb.com.firebasedemo;

/**
 * Created by mirajul on 10/10/17.
 */

public class AddDetails {

    String First_Name;
    String Last_Name;
    String User_Name;

    AddDetails()
    {

    }

    public AddDetails(String first_Name, String last_Name, String user_Name) {
        First_Name = first_Name;
        Last_Name = last_Name;
        User_Name = user_Name;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }
}
