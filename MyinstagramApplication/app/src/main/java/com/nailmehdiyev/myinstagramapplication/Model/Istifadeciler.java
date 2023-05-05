package com.nailmehdiyev.myinstagramapplication.Model;

public class Istifadeciler {

   private String name,username,emailadresse,password,id,bio,imageurl,durum;


    public Istifadeciler() {
    }

    public Istifadeciler(String name, String username, String emailadresse, String password, String id, String bio, String imageurl, String durum) {
        this.name = name;
        this.username = username;
        this.emailadresse = emailadresse;
        this.password = password;
        this.id = id;
        this.bio = bio;
        this.imageurl = imageurl;
        this.durum = durum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailadresse() {
        return emailadresse;
    }

    public void setEmailadresse(String emailadresse) {
        this.emailadresse = emailadresse;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
