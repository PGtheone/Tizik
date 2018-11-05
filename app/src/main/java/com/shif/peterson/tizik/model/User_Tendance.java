package com.shif.peterson.tizik.model;

public class User_Tendance {

    private String id_userTendance;
    private String id_utilisateur;
    private String id_tendance;
    private String is_Actif;
    private String date_created;

    public User_Tendance() {
    }

    public User_Tendance(String id_userTendance, String id_utilisateur, String id_tendance, String is_Actif, String date_created) {
        this.id_userTendance = id_userTendance;
        this.id_utilisateur = id_utilisateur;
        this.id_tendance = id_tendance;
        this.is_Actif = is_Actif;
        this.date_created = date_created;
    }

    public String getId_userTendance() {
        return id_userTendance;
    }

    public void setId_userTendance(String id_userTendance) {
        this.id_userTendance = id_userTendance;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getId_tendance() {
        return id_tendance;
    }

    public void setId_tendance(String id_tendance) {
        this.id_tendance = id_tendance;
    }

    public String getIs_Actif() {
        return is_Actif;
    }

    public void setIs_Actif(String is_Actif) {
        this.is_Actif = is_Actif;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
