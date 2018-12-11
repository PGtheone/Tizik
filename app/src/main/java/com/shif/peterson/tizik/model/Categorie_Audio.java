package com.shif.peterson.tizik.model;

public class Categorie_Audio {

    private String id_categorieAudio;
    private String id_audio;
    private String id_categorie;
    private String created_by;
    private String date_created;

    public Categorie_Audio() {
    }

    public Categorie_Audio(String id_categorieAudio, String id_audio, String id_categorie, String created_by, String date_created) {
        this.id_categorieAudio = id_categorieAudio;
        this.id_audio = id_audio;
        this.id_categorie = id_categorie;
        this.created_by = created_by;
        this.date_created = date_created;
    }

    public String getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(String id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getId_categorieAudio() {
        return id_categorieAudio;
    }

    public void setId_categorieAudio(String id_categorieAudio) {
        this.id_categorieAudio = id_categorieAudio;
    }

    public String getId_audio() {
        return id_audio;
    }

    public void setId_audio(String id_audio) {
        this.id_audio = id_audio;
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
}
