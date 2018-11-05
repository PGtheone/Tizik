package com.shif.peterson.tizik.model;

public class Vente_Audio {

    private String id_venteAudio;
    private String id_audio;
    private String date_achat;
    private boolean purchase;


    public Vente_Audio() {
    }

    public Vente_Audio(String id_venteAudio, String id_audio, String date_achat, boolean purchase) {
        this.id_venteAudio = id_venteAudio;
        this.id_audio = id_audio;
        this.date_achat = date_achat;
        this.purchase = purchase;
    }

    public String getId_venteAudio() {
        return id_venteAudio;
    }

    public void setId_venteAudio(String id_venteAudio) {
        this.id_venteAudio = id_venteAudio;
    }

    public String getId_audio() {
        return id_audio;
    }

    public void setId_audio(String id_audio) {
        this.id_audio = id_audio;
    }

    public String getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(String date_achat) {
        this.date_achat = date_achat;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }
}
