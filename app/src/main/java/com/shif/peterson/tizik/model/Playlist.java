package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Playlist implements Parcelable {

    private String id_playlist;
    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
    private String id_utilisateur;
    private String Nom;
    private String CreatedBy;
    private Date DateCreated;
    private String ModifBy;
    private Date DateModif;
    private String urlPoster;
    private boolean isPublic;

    public Playlist(String id_playlist, String id_utilisateur, String nom, String createdBy, Date dateCreated, String urlPoster, boolean isPublic) {
        this.id_playlist = id_playlist;
        this.id_utilisateur = id_utilisateur;
        Nom = nom;
        CreatedBy = createdBy;
        DateCreated = dateCreated;
        this.urlPoster = urlPoster;
        this.isPublic = isPublic;
    }

    public Playlist() {
    }

    protected Playlist(Parcel in) {
        this.id_playlist = in.readString();
        this.id_utilisateur = in.readString();
        this.Nom = in.readString();
        this.CreatedBy = in.readString();
        long tmpDateCreated = in.readLong();
        this.DateCreated = tmpDateCreated == -1 ? null : new Date(tmpDateCreated);
        this.ModifBy = in.readString();
        long tmpDateModif = in.readLong();
        this.DateModif = tmpDateModif == -1 ? null : new Date(tmpDateModif);
        this.urlPoster = in.readString();
        this.isPublic = in.readByte() != 0;
    }

    public String getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(String id_playlist) {
        this.id_playlist = id_playlist;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public Date getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        DateCreated = dateCreated;
    }

    public String getModifBy() {
        return ModifBy;
    }

    public void setModifBy(String modifBy) {
        ModifBy = modifBy;
    }

    public Date getDateModif() {
        return DateModif;
    }

    public void setDateModif(Date dateModif) {
        DateModif = dateModif;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_playlist);
        dest.writeString(this.id_utilisateur);
        dest.writeString(this.Nom);
        dest.writeString(this.CreatedBy);
        dest.writeLong(this.DateCreated != null ? this.DateCreated.getTime() : -1);
        dest.writeString(this.ModifBy);
        dest.writeLong(this.DateModif != null ? this.DateModif.getTime() : -1);
        dest.writeString(this.urlPoster);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
    }
}
