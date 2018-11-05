package com.shif.peterson.tizik.model;

public class User_Groupe {

    private String id_userGroupe;
    private String id_utilisateur;
    private boolean not_InGroupe;
    private String fonction;
    private String date_created;

    public User_Groupe() {
    }

    public User_Groupe(String id_userGroupe, String id_utilisateur, boolean not_InGroupe, String fonction, String date_created) {
        this.id_userGroupe = id_userGroupe;
        this.id_utilisateur = id_utilisateur;
        this.not_InGroupe = not_InGroupe;
        this.fonction = fonction;
        this.date_created = date_created;
    }

    public String getId_userGroupe() {
        return id_userGroupe;
    }

    public void setId_userGroupe(String id_userGroupe) {
        this.id_userGroupe = id_userGroupe;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public boolean isNot_InGroupe() {
        return not_InGroupe;
    }

    public void setNot_InGroupe(boolean not_InGroupe) {
        this.not_InGroupe = not_InGroupe;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}
