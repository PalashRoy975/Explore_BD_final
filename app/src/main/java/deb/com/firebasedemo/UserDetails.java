package deb.com.firebasedemo;

import deb.com.*;

/**
 * Created by Deb on 11/3/2017.
 */

public class UserDetails {

    private String name, city, age, work, study, interest, email,dp;

    public UserDetails() {
    }

    public UserDetails(String name, String city, String age, String work, String study, String interest, String email, String dp) {
        this.name = name;
        this.city = city;
        this.age = age;
        this.work = work;
        this.study = study;
        this.interest = interest;
        this.email = email;
        this.dp=dp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }
}
