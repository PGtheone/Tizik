package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Abonnement implements Parcelable {

    public static final Creator<Abonnement> CREATOR = new Creator<Abonnement>() {
        @Override
        public Abonnement createFromParcel(Parcel source) {
            return new Abonnement(source);
        }

        @Override
        public Abonnement[] newArray(int size) {
            return new Abonnement[size];
        }
    };
   private String DateAbonnement;
   private String  DateComment;
   private boolean actif;
    private String avis;
    private String created_by;
   private  String  date_created;
   private String date_modifed;
    private String id_abonnement;
    private String id_fan;
    private String id_utilisateur;
    private String modified_by;



    public Abonnement() {
    }
    private String note;

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

    public Abonnement(String id_abonnement, String id_utilisateur, String id_fan, boolean actif, String created_by, String date_created, String note) {
        this.id_abonnement = id_abonnement;
        this.id_utilisateur = id_utilisateur;
        this.id_fan = id_fan;
        this.actif = actif;
        this.created_by = created_by;
        this.date_created = date_created;
        this.note = note;
    }

    protected Abonnement(Parcel in) {
        this.DateAbonnement = in.readString();
        this.DateComment = in.readString();
        this.actif = in.readByte() != 0;
        this.avis = in.readString();
        this.created_by = in.readString();
        this.date_created = in.readString();
        long tmpDate_modifed = in.readLong();
        this.date_modifed = in.readString();
        this.id_abonnement = in.readString();
        this.id_fan = in.readString();
        this.id_utilisateur = in.readString();
        this.modified_by = in.readString();
        this.note = in.readString();
    }

    public String getDateAbonnement() {
        return DateAbonnement;
    }

    public void setDateAbonnement(String dateAbonnement) {
        DateAbonnement = dateAbonnement;
    }

    public String getDateComment() {
        return DateComment;
    }

    public void setDateComment(String dateComment) {
        DateComment = dateComment;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
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

    public String getDate_modifed() {
        return date_modifed;
    }

    public void setDate_modifed(String date_modifed) {
        this.date_modifed = date_modifed;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DateAbonnement);
        dest.writeString(this.DateComment);
        dest.writeByte(this.actif ? (byte) 1 : (byte) 0);
        dest.writeString(this.avis);
        dest.writeString(this.created_by);
        dest.writeString(this.date_created);
        dest.writeString(this.date_modifed);
        dest.writeString(this.id_abonnement);
        dest.writeString(this.id_fan);
        dest.writeString(this.id_utilisateur);
        dest.writeString(this.modified_by);
        dest.writeString(this.note);
    }
}


