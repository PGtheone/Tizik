package com.shif.peterson.tizik.model;

public class Like_Audio {

    private String like_audio;
    private String id_audio;
    private String id_utlisateur;
    private double like;
    private String created_by;
    private String date_created;
    private String modify_by;
    private String date_modify;

    public Like_Audio() {
    }

    public Like_Audio(String like_audio, String id_audio, String id_utlisateur, double like, String created_by, String date_created) {
        this.like_audio = like_audio;
        this.id_audio = id_audio;
        this.id_utlisateur = id_utlisateur;
        this.like = like;
        this.created_by = created_by;
        this.date_created = date_created;
    }

    public String getLike_audio() {
        return like_audio;
    }

    public void setLike_audio(String like_audio) {
        this.like_audio = like_audio;
    }

    public String getId_audio() {
        return id_audio;
    }

    public void setId_audio(String id_audio) {
        this.id_audio = id_audio;
    }

    public String getId_utlisateur() {
        return id_utlisateur;
    }

    public void setId_utlisateur(String id_utlisateur) {
        this.id_utlisateur = id_utlisateur;
    }

    public double getLike() {
        return like;
    }

    public void setLike(double like) {
        this.like = like;
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
