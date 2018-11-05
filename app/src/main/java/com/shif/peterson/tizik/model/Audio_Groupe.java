package com.shif.peterson.tizik.model;

public class Audio_Groupe {

    private String id_AudioGroupe;
    private String id_groupe;
    private String id_albumGroupe;
    private String titre_audio;
    private String description_audio;
    private double duree_musique;
    private String url_poster;
    private String Url_musique;
    private boolean is_Free;
    private double prix;
    private String is_actif;
    private String Date_upload;


    public Audio_Groupe() {
    }

    public Audio_Groupe(String id_AudioGroupe, String id_groupe, String titre_audio, String description_audio, double duree_musique, String url_poster, String url_musique, boolean is_Free, double prix, String is_actif, String date_upload) {
        this.id_AudioGroupe = id_AudioGroupe;
        this.id_groupe = id_groupe;
        this.titre_audio = titre_audio;
        this.description_audio = description_audio;
        this.duree_musique = duree_musique;
        this.url_poster = url_poster;
        Url_musique = url_musique;
        this.is_Free = is_Free;
        this.prix = prix;
        this.is_actif = is_actif;
        Date_upload = date_upload;
    }

    public String getId_albumGroupe() {
        return id_albumGroupe;
    }

    public void setId_albumGroupe(String id_albumGroupe) {
        this.id_albumGroupe = id_albumGroupe;
    }

    public String getId_AudioGroupe() {
        return id_AudioGroupe;
    }

    public void setId_AudioGroupe(String id_AudioGroupe) {
        this.id_AudioGroupe = id_AudioGroupe;
    }

    public String getId_groupe() {
        return id_groupe;
    }

    public void setId_groupe(String id_groupe) {
        this.id_groupe = id_groupe;
    }

    public String getTitre_audio() {
        return titre_audio;
    }

    public void setTitre_audio(String titre_audio) {
        this.titre_audio = titre_audio;
    }

    public String getDescription_audio() {
        return description_audio;
    }

    public void setDescription_audio(String description_audio) {
        this.description_audio = description_audio;
    }

    public double getDuree_musique() {
        return duree_musique;
    }

    public void setDuree_musique(double duree_musique) {
        this.duree_musique = duree_musique;
    }

    public String getUrl_poster() {
        return url_poster;
    }

    public void setUrl_poster(String url_poster) {
        this.url_poster = url_poster;
    }

    public String getUrl_musique() {
        return Url_musique;
    }

    public void setUrl_musique(String url_musique) {
        Url_musique = url_musique;
    }

    public boolean isIs_Free() {
        return is_Free;
    }

    public void setIs_Free(boolean is_Free) {
        this.is_Free = is_Free;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getIs_actif() {
        return is_actif;
    }

    public void setIs_actif(String is_actif) {
        this.is_actif = is_actif;
    }

    public String getDate_upload() {
        return Date_upload;
    }

    public void setDate_upload(String date_upload) {
        Date_upload = date_upload;
    }
}
