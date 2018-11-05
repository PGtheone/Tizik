package com.shif.peterson.tizik.model;

public class Favori {

    private String id_favori;
    private String id_utilisateur;
    private boolean always_inFavori;
    private String created_by;
    private String date_created;
    private String modify_by;
    private String date_modify;

    public Favori() {
    }

    public Favori(String id_favori, String id_utilisateur, boolean always_inFavori, String created_by, String date_created) {
        this.id_favori = id_favori;
        this.id_utilisateur = id_utilisateur;
        this.always_inFavori = always_inFavori;
        this.created_by = created_by;
        this.date_created = date_created;
    }

    public String getId_favori() {
        return id_favori;
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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getModify_by() {
        return modify_by;
    }

    public void setModify_by(String modify_by) {
        this.modify_by = modify_by;
    }

    public String getDate_modify() {
        return date_modify;
    }

    public void setDate_modify(String date_modify) {
        this.date_modify = date_modify;
    }
}
