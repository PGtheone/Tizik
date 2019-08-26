package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Categorie implements Parcelable {

    private String id_categorie;
    private String nom_categorie;
    private String ceated_by;
    private String date_created;
    private String date_modified;

    public static final Parcelable.Creator<Categorie> CREATOR = new Parcelable.Creator<Categorie>() {
        @Override
        public Categorie createFromParcel(Parcel source) {
            return new Categorie(source);
        }

        @Override
        public Categorie[] newArray(int size) {
            return new Categorie[size];
        }
    };

    public Categorie() {
    }

    public String getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(String id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getNom_categorie() {
        return nom_categorie;
    }

    public void setNom_categorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    public String getCeated_by() {
        return ceated_by;
    }

    public void setCeated_by(String ceated_by) {
        this.ceated_by = ceated_by;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public Categorie(String id_categorie, String nom_categorie, String ceated_by, String date_created) {
        this.id_categorie = id_categorie;
        this.nom_categorie = nom_categorie;
        this.ceated_by = ceated_by;
        this.date_created = date_created;
        this.date_modified = null;
    }

    protected Categorie(Parcel in) {
        this.id_categorie = in.readString();
        this.nom_categorie = in.readString();
        this.ceated_by = in.readString();
        this.date_created = in.readString();
        this.date_modified = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_categorie);
        dest.writeString(this.nom_categorie);
        dest.writeString(this.ceated_by);
        dest.writeString(this.date_created);
        dest.writeString(this.date_modified);
    }
}
