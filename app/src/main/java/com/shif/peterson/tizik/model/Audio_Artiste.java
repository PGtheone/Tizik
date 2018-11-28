package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Audio_Artiste implements Parcelable {


    private String id_musique;
    private String uploaded_by;
    private String nom_chanteur;
    private String id_album;
    private String titre_musique;
    private String description_musique;
    private double duree_musique;
    private String url_poster;
    private String Url_musique;
    private boolean is_Free;
    private double prix;
    private boolean is_actif;
    private String Date_upload;
    private String date_modified;
    boolean selected;


    public Audio_Artiste() {
    }

    public Audio_Artiste(String id_musique, String uploaded_by, String titre_musique, String nom_chanteur, String description_musique, double duree_musique, String url_poster, String url_musique, boolean is_Free, double prix, boolean is_actif, String date_upload, boolean selected) {
        this.id_musique = id_musique;
        this.uploaded_by = uploaded_by;
        this.titre_musique = titre_musique;
        this.nom_chanteur = nom_chanteur;
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
        this.uploaded_by = artiste;
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

    public String getNom_chanteur() {
        return nom_chanteur;
    }

    public void setNom_chanteur(String nom_chanteur) {
        this.nom_chanteur = nom_chanteur;
    }


    public String getId_musique() {
        return id_musique;
    }

    public void setId_musique(String id_musique) {
        this.id_musique = id_musique;
    }

    public String getuploaded_by() {
        return uploaded_by;
    }

    public void setuploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
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

    public boolean getIs_actif() {
        return is_actif;
    }

    public void setIs_actif(boolean is_actif) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_musique);
        dest.writeString(this.uploaded_by);
        dest.writeString(this.nom_chanteur);
        dest.writeString(this.id_album);
        dest.writeString(this.titre_musique);
        dest.writeString(this.description_musique);
        dest.writeDouble(this.duree_musique);
        dest.writeString(this.url_poster);
        dest.writeString(this.Url_musique);
        dest.writeByte(this.is_Free ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.prix);
        dest.writeByte(this.is_actif ? (byte) 1 : (byte) 0);
        dest.writeString(this.Date_upload);
        dest.writeString(this.date_modified);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected Audio_Artiste(Parcel in) {
        this.id_musique = in.readString();
        this.uploaded_by = in.readString();
        this.nom_chanteur = in.readString();
        this.id_album = in.readString();
        this.titre_musique = in.readString();
        this.description_musique = in.readString();
        this.duree_musique = in.readDouble();
        this.url_poster = in.readString();
        this.Url_musique = in.readString();
        this.is_Free = in.readByte() != 0;
        this.prix = in.readDouble();
        this.is_actif = in.readByte() != 0;
        this.Date_upload = in.readString();
        this.date_modified = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<Audio_Artiste> CREATOR = new Creator<Audio_Artiste>() {
        @Override
        public Audio_Artiste createFromParcel(Parcel source) {
            return new Audio_Artiste(source);
        }

        @Override
        public Audio_Artiste[] newArray(int size) {
            return new Audio_Artiste[size];
        }
    };
}
