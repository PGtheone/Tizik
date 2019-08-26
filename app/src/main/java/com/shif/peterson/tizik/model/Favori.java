package com.shif.peterson.tizik.model;

public class Favori {

    private String id_favori;
    private String id_utilisateur;
    private String id_media;
    private boolean always_inFavori;
    private String CreatedBy;
    private String DateCreated;
    private String ModifBy;
    private String DateModif;


    public Favori() {
    }

    public Favori(String id_favori, String id_media, String id_utilisateur, boolean always_inFavori, String CreatedBy, String DateCreated) {
        this.id_favori = id_favori;
        this.id_utilisateur = id_utilisateur;
        this.always_inFavori = always_inFavori;
        this.CreatedBy = CreatedBy;
        this.DateCreated = DateCreated;
        this.id_media = id_media;
    }

    public String getId_favori() {
        return id_favori;
    }


    public String getId_media() {
        return id_media;
    }

    public void setId_media(String id_media) {
        this.id_media = id_media;
    }

    public void setId_favori(String id_favori) {
        this.id_favori = id_favori;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public boolean isAlways_inFavori() {
        return always_inFavori;
    }

    public void setAlways_inFavori(boolean always_inFavori) {
        this.always_inFavori = always_inFavori;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String DateCreated) {
        this.DateCreated = DateCreated;
    }

    public String getModifBy() {
        return ModifBy;
    }

    public void setModifBy(String ModifBy) {
        this.ModifBy = ModifBy;
    }

    public String getDateModif() {
        return DateModif;
    }

    public void setDateModif(String DateModif) {
        this.DateModif = DateModif;
    }
}
