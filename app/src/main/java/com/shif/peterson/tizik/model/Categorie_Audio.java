package com.shif.peterson.tizik.model;

import java.util.Date;

public class Categorie_Audio {

    private String id_Audio;
    private String id_Categorie;
    private String id_AudioTendance;
    private String createdBy;
    private Date dateCreated;


    public Categorie_Audio(String id_Audio, String id_Categorie, String id_AudioTendance, String createdBy, Date dateCreated) {
        this.id_Audio = id_Audio;
        this.id_Categorie = id_Categorie;
        this.id_AudioTendance = id_AudioTendance;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
    }


    public Categorie_Audio() {
    }


    public String getId_Audio() {
        return id_Audio;
    }

    public void setId_Audio(String id_Audio) {
        this.id_Audio = id_Audio;
    }

    public String getId_Categorie() {
        return id_Categorie;
    }

    public void setId_Categorie(String id_Categorie) {
        this.id_Categorie = id_Categorie;
    }

    public String getId_AudioTendance() {
        return id_AudioTendance;
    }

    public void setId_AudioTendance(String id_AudioTendance) {
        this.id_AudioTendance = id_AudioTendance;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}




