package com.nailmehdiyev.myinstagramapplication.Model;

public class Comment {

   private String comment,gonderen,id;

    public Comment() {
    }


    public Comment(String comment, String gonderen, String id) {
        this.comment = comment;
        this.gonderen = gonderen;
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
