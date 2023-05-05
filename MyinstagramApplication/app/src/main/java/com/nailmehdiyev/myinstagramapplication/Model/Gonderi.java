package com.nailmehdiyev.myinstagramapplication.Model;

public  class Gonderi {


    String postid, gonderiresmi, description, publisher;


    public Gonderi() {
    }


    public  Gonderi(String postid, String gonderiresmi, String description, String publisher) {
        this.postid = postid;
        this.gonderiresmi = gonderiresmi;
        this.description = description;
        this.publisher = publisher;
    }


    public  String getPostid() {
        return postid;
    }

    public  void setPostid(String postid) {
        this.postid = postid;
    }

    public String getGonderiresmi() {
        return gonderiresmi;
    }

    public void setGonderiresmi(String gonderiresmi) {
        this.gonderiresmi = gonderiresmi;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}