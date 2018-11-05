package com.shif.peterson.tizik.model;

public class Groupe {

    private String id_groupe;
    private String nom_groupe;
    private String description;
    private String date_created;
    private String date_modified;

    public Groupe() {
    }

    public Groupe(String id_groupe, String nom_groupe, String description, String date_created) {
        this.id_groupe = id_groupe;
        this.nom_groupe = nom_groupe;
        this.description = description;
        this.date_created = date_created;
        this.date_modified = null;
    }

    public String getId_groupe() {
        return id_groupe;
    }

    public void setId_groupe(String id_groupe) {
        this.id_groupe = id_groupe;
    }

    public String getNom_groupe() {
        return nom_groupe;
    }

    public void setNom_groupe(String nom_groupe) {
        this.nom_groupe = nom_groupe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
