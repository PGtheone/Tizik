package com.shif.peterson.tizik.model;

public class Album_Artiste {

    private String id_AlbumArtiste;
    private String id_utilisateur;
    private String titre;
    private String description;
    private int nombre_audio;
    private String url_demo;
    private String url_poster;
    private String date_creation;
    private String  date_venteSignature;
    private boolean is_free;
    private double prix;
    private boolean is_actif;
    private String created_by;
    private String date_creted;
    private String modify_by;
    private String date_modify;

    public Album_Artiste() {
    }

    public Album_Artiste(String id_AlbumArtiste, String id_utilisateur, String titre, String description, int nombre_audio, String url_demo, String url_poster, String date_creation, String date_venteSignature, boolean is_free, double prix, boolean is_actif, String created_by, String date_creted) {
        this.id_AlbumArtiste = id_AlbumArtiste;
        this.id_utilisateur = id_utilisateur;
        this.titre = titre;
        this.description = description;
        this.nombre_audio = nombre_audio;
        this.url_demo = url_demo;
        this.url_poster = url_poster;
        this.date_creation = date_creation;
        this.date_venteSignature = date_venteSignature;
        this.is_free = is_free;
        this.prix = prix;
        this.is_actif = is_actif;
        this.created_by = created_by;
        this.date_creted = date_creted;
    }

    public String getId_AlbumArtiste() {
        return id_AlbumArtiste;
    }

    public void setId_AlbumArtiste(String id_AlbumArtiste) {
        this.id_AlbumArtiste = id_AlbumArtiste;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNombre_audio() {
        return nombre_audio;
    }

    public void setNombre_audio(int nombre_audio) {
        this.nombre_audio = nombre_audio;
    }

    public String getUrl_demo() {
        return url_demo;
    }

    public void setUrl_demo(String url_demo) {
        this.url_demo = url_demo;
    }

    public String getUrl_poster() {
        return url_poster;
    }

    public void setUrl_poster(String url_poster) {
        this.url_poster = url_poster;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getDate_venteSignature() {
        return date_venteSignature;
    }

    public void setDate_venteSignature(String date_venteSignature) {
        this.date_venteSignature = date_venteSignature;
    }

    public boolean isIs_free() {
        return is_free;
    }

    public void setIs_free(boolean is_free) {
        this.is_free = is_free;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public boolean isIs_actif() {
        return is_actif;
    }

    public void setIs_actif(boolean is_actif) {
        this.is_actif = is_actif;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate_creted() {
        return date_creted;
    }

    public void setDate_creted(String date_creted) {
        this.date_creted = date_creted;
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
