package com.shif.peterson.tizik.model;

public class Achat_Audio {

    private String id_achatAudio;
    private String id_audio;
    private String id_utilisateur;
    private String date_achat;
    private boolean purchase;

    public Achat_Audio() {
    }

    public Achat_Audio(String id_achatAudio, String id_audio, String id_utilisateur, String date_achat, boolean purchase) {
        this.id_achatAudio = id_achatAudio;
        this.id_audio = id_audio;
        this.id_utilisateur = id_utilisateur;
        this.date_achat = date_achat;
        this.purchase = purchase;
    }

    public String getId_achatAudio() {
        return id_achatAudio;
    }

    public void setId_achatAudio(String id_achatAudio) {
        this.id_achatAudio = id_achatAudio;
    }

    public String getId_audio() {
        return id_audio;
    }

    public void setId_audio(String id_audio) {
        this.id_audio = id_audio;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
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
