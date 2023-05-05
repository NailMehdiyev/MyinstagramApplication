package com.nailmehdiyev.myinstagramapplication.Model;

public class Mesajlar {


    private String mesajlar,mesajgonderenid,mesajalanid,resm,tarix;

    private Boolean goruldu;



    public Mesajlar() {
    }


    public Mesajlar(String mesajlar, String mesajgonderenid, String mesajalanid, String resm, String tarix, Boolean goruldu) {
        this.mesajlar = mesajlar;
        this.mesajgonderenid = mesajgonderenid;
        this.mesajalanid = mesajalanid;
        this.resm = resm;
        this.tarix = tarix;
        this.goruldu = goruldu;
    }



    public String getMesajlar() {
        return mesajlar;
    }

    public void setMesajlar(String mesajlar) {
        this.mesajlar = mesajlar;
    }

    public String getMesajgonderenid() {
        return mesajgonderenid;
    }

    public void setMesajgonderenid(String mesajgonderenid) {
        this.mesajgonderenid = mesajgonderenid;
    }

    public String getMesajalanid() {
        return mesajalanid;
    }

    public void setMesajalanid(String mesajalanid) {
        this.mesajalanid = mesajalanid;
    }

    public String getResm() {
        return resm;
    }

    public void setResm(String resm) {
        this.resm = resm;
    }

    public String getTarix() {
        return tarix;
    }

    public void setTarix(String tarix) {
        this.tarix = tarix;
    }


    public Boolean getGoruldu() {
        return goruldu;
    }

    public void setGoruldu(Boolean goruldu) {
        this.goruldu = goruldu;
    }
}
