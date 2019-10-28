package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Playlist_Audio implements Parcelable {


    private String id_playlist;
    private String Id_PlaylistAudio;
    private String CreatedBy;
    public static final Creator<Playlist_Audio> CREATOR = new Creator<Playlist_Audio>() {
        @Override
        public Playlist_Audio createFromParcel(Parcel source) {
            return new Playlist_Audio(source);
        }

        @Override
        public Playlist_Audio[] newArray(int size) {
            return new Playlist_Audio[size];
        }
    };
    private String ModifBy;
    private String DateModif;
    private Date DateCreated;

    public Playlist_Audio() {
    }
    private String Id_Audio;

    public String getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(String id_playlist) {
        this.id_playlist = id_playlist;
    }

    public String getId_PlaylistAudio() {
        return Id_PlaylistAudio;
    }

    public Playlist_Audio(String id_playlist, String id_PlaylistAudio, String createdBy, Date dateCreated, String modifBy, String dateModif, String id_Audio) {
        this.id_playlist = id_playlist;
        Id_PlaylistAudio = id_PlaylistAudio;
        CreatedBy = createdBy;
        DateCreated = dateCreated;
        ModifBy = modifBy;
        DateModif = dateModif;
        Id_Audio = id_Audio;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    protected Playlist_Audio(Parcel in) {
        this.id_playlist = in.readString();
        this.Id_PlaylistAudio = in.readString();
        this.CreatedBy = in.readString();
        long tmpDateCreated = in.readLong();
        this.DateCreated = tmpDateCreated == -1 ? null : new Date(tmpDateCreated);
        this.ModifBy = in.readString();
        this.DateModif = in.readString();
        this.Id_Audio = in.readString();
    }

    public void setId_PlaylistAudio(String id_PlaylistAudio) {
        Id_PlaylistAudio = id_PlaylistAudio;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifBy() {
        return ModifBy;
    }

    public Date getDateCreated() {
        return DateCreated;
    }

    public String getDateModif() {
        return DateModif;
    }

    public void setDateCreated(Date dateCreated) {
        DateCreated = dateCreated;
    }

    public void setModifBy(String modifBy) {
        ModifBy = modifBy;
    }

    public void setDateModif(String dateModif) {
        DateModif = dateModif;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getId_Audio() {
        return Id_Audio;
    }

    public void setId_Audio(String id_Audio) {
        Id_Audio = id_Audio;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_playlist);
        dest.writeString(this.Id_PlaylistAudio);
        dest.writeString(this.CreatedBy);
        dest.writeLong(this.DateCreated != null ? this.DateCreated.getTime() : -1);
        dest.writeString(this.ModifBy);
        dest.writeString(this.DateModif);
        dest.writeString(this.Id_Audio);
    }
}
