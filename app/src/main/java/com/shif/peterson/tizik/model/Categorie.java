package com.shif.peterson.tizik.model;

public class Tendance {

    private String id_categorie;
    private String nom_categorie;
    private String ceated_by;
    private String date_created;
    private String date_modified;

    public Tendance() {
    }

    public Tendance(String id_categorie, String nom_categorie, String ceated_by, String date_created) {
        this.id_categorie = id_categorie;
        this.nom_categorie = nom_categorie;
        this.ceated_by = ceated_by;
        this.date_created = date_created;
        this.date_modified = null;
    }

    public String getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(String id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getNom_categorie() {
        return nom_categorie;
    }

    public void setNom_categorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    public String getCeated_by() {
        return ceated_by;
    }

    public void setCeated_by(String ceated_by) {
        this.ceated_by = ceated_by;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }
}
