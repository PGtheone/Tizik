package com.shif.peterson.tizik.model;

public class Audio_Artiste {

    public long id;
    private String id_musique;
    private String id_artiste;
    private String id_album;
    private String titre_musique;
    private String description_musique;
    private double duree_musique;
    private String url_poster;
    private String Url_musique;
    private boolean is_Free;
    private double prix;
    private String is_actif;
    private String Date_upload;
    private String date_modified;
    boolean selected;


    public Audio_Artiste() {
    }

    public Audio_Artiste(String id_musique, String id_artiste, String titre_musique, String description_musique, double duree_musique, String url_poster, String url_musique, boolean is_Free, double prix, String is_actif, String date_upload, boolean selected) {
        this.id_musique = id_musique;
        this.id_artiste = id_artiste;
        this.titre_musique = titre_musique;
        this.description_musique = description_musique;
        this.duree_musique = duree_musique;
        this.url_poster = url_poster;
        Url_musique = url_musique;
        this.is_Free = is_Free;
        this.prix = prix;
        this.is_actif = is_actif;
        Date_upload = date_upload;
        this.selected = selected;
    }

    public Audio_Artiste(String id_musique, String url_musique,String titre_musique, String artiste, double duree_musique, String url_poster) {
        this.id_musique = id_musique;
        this.titre_musique = titre_musique;
        this.id_artiste = artiste;
        this.duree_musique = duree_musique;
        this.Url_musique = url_musique;
        this.url_poster = url_poster;
    }


    public String getId_album() {
        return id_album;
    }

    public void setId_album(String id_album) {
        this.id_album = id_album;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_musique() {
        return id_musique;
    }

    public void setId_musique(String id_musique) {
        this.id_musique = id_musique;
    }

    public String getId_artiste() {
        return id_artiste;
    }

    public void setId_artiste(String id_artiste) {
        this.id_artiste = id_artiste;
    }

    public String getTitre_musique() {
        return titre_musique;
    }

    public void setTitre_musique(String titre_musique) {
        this.titre_musique = titre_musique;
    }

    public String getDescription_musique() {
        return description_musique;
    }

    public void setDescription_musique(String description_musique) {
        this.description_musique = description_musique;
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

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
