package com.shif.peterson.tizik.model;

public class Achat_Album {

    private String id_achatAlbum;
    private String id_album;
    private String id_utilisateur;
    private String date_achat;
    private boolean purchase;


    public Achat_Album() {
    }

    public Achat_Album(String id_achatAlbum, String id_album, String id_utilisateur, String date_achat, boolean purchase) {
        this.id_achatAlbum = id_achatAlbum;
        this.id_album = id_album;
        this.id_utilisateur = id_utilisateur;
        this.date_achat = date_achat;
        this.purchase = purchase;
    }

    public String getId_achatAlbum() {
        return id_achatAlbum;
    }

    public void setId_achatAlbum(String id_achatAlbum) {
        this.id_achatAlbum = id_achatAlbum;
    }

    public String getId_album() {
        return id_album;
    }

    public void setId_album(String id_album) {
        this.id_album = id_album;
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
