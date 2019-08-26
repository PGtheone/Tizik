package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final Parcelable.Creator<Audio_Artiste> CREATOR = new Parcelable.Creator<Audio_Artiste>() {
        @Override
        public Audio_Artiste createFromParcel(Parcel source) {
            return new Audio_Artiste(source);
        }

        @Override
        public Audio_Artiste[] newArray(int size) {
            return new Audio_Artiste[size];
        }
    };
    private String date_modified;
    @Exclude
    public List<Categorie> selectedCategorie;
    @Exclude boolean selected;


    public Audio_Artiste() {
    }
    private  Date Date_upload;

    public Audio_Artiste(String id_musique, String uploaded_by, String titre_musique, String nom_chanteur, String description_musique, double duree_musique, String url_poster, String url_musique, boolean is_Free, double prix, boolean is_actif, Date date_upload, boolean selected) {
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
        this.selectedCategorie = new ArrayList<>();
    }

    public Audio_Artiste(String id_musique, String url_musique,String titre_musique, String artiste, double duree_musique, String url_poster) {
        this.id_musique = id_musique;
        this.titre_musique = titre_musique;
        this.uploaded_by = artiste;
        this.duree_musique = duree_musique;
        this.Url_musique = url_musique;
        this.url_poster = url_poster;
        this.selectedCategorie = new ArrayList<>();
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
        long tmpDate_upload = in.readLong();
        this.Date_upload = tmpDate_upload == -1 ? null : new Date(tmpDate_upload);
        this.date_modified = in.readString();
        this.selected = in.readByte() != 0;
        this.selectedCategorie = in.createTypedArrayList(Categorie.CREATOR);
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

    public List<Categorie> getSelectedCategorie() {
        return selectedCategorie;
    }

    public void setSelectedCategorie(List<Categorie> selectedCategorie) {
        this.selectedCategorie = selectedCategorie;
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

    public Date getDate_upload() {
        return Date_upload;
    }

    public void setDate_upload(Date date_upload) {
        Date_upload = date_upload;
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
        dest.writeLong(this.Date_upload != null ? this.Date_upload.getTime() : -1);
        dest.writeString(this.date_modified);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.selectedCategorie);
    }
}
