package com.shif.peterson.tizik.model;

public class Abonnement {
    private String id_abonnement;
    private String id_utilisateur;
    private String id_fan;
    private boolean actif;
    private String date_created;
    private String created_by;

    public Abonnement() {
    }

    public Abonnement(String id_abonnement, String id_utilisateur, String id_fan, boolean actif, String date_created) {
        this.id_abonnement = id_abonnement;
        this.id_utilisateur = id_utilisateur;
        this.id_fan = id_fan;
        this.actif = actif;
        this.date_created = date_created;
    }

    public Abonnement(String id_abonnement, String id_utilisateur, String id_fan, boolean actif, String date_created, String created_by) {
        this.id_abonnement = id_abonnement;
        this.id_utilisateur = id_utilisateur;
        this.id_fan = id_fan;
        this.actif = actif;
        this.date_created = date_created;
        this.created_by = created_by;
    }

    public String getId_abonnement() {
        return id_abonnement;
    }

    public void setId_abonnement(String id_abonnement) {
        this.id_abonnement = id_abonnement;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getId_fan() {
        return id_fan;
    }

    public void setId_fan(String id_fan) {
        this.id_fan = id_fan;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
