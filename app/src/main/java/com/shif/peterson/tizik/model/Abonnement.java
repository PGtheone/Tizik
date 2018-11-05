package com.shif.peterson.tizik.model;

public class Abonnement {
    private String id_abonnement;
    private String id_utilisateur;
    private String id_utlisateurAbonne;
    private boolean subscribe;
    private double note;
    private String date_abonnement;

    public Abonnement() {
    }

    public Abonnement(String id_abonnement, String id_utilisateur, String id_utlisateurAbonne, boolean subscribe, double note, String date_abonnement) {
        this.id_abonnement = id_abonnement;
        this.id_utilisateur = id_utilisateur;
        this.id_utlisateurAbonne = id_utlisateurAbonne;
        this.subscribe = subscribe;
        this.note = note;
        this.date_abonnement = date_abonnement;
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

    public String getId_utlisateurAbonne() {
        return id_utlisateurAbonne;
    }

    public void setId_utlisateurAbonne(String id_utlisateurAbonne) {
        this.id_utlisateurAbonne = id_utlisateurAbonne;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getDate_abonnement() {
        return date_abonnement;
    }

    public void setDate_abonnement(String date_abonnement) {
        this.date_abonnement = date_abonnement;
    }
}
