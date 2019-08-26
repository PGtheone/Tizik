package com.shif.peterson.tizik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Playlist_Audio implements Parcelable {

    public static final Parcelable.Creator<Playlist_Audio> CREATOR = new Parcelable.Creator<Playlist_Audio>() {
        @Override
        public Playlist_Audio createFromParcel(Parcel source) {
            return new Playlist_Audio(source);
        }

        @Override
        public Playlist_Audio[] newArray(int size) {
            return new Playlist_Audio[size];
        }
    };
    private String id_playlist;
    private String Id_PlaylistAudio;
    private String CreatedBy;
    private String DateCreated;
    private String ModifBy;
    private String DateModif;


    public Playlist_Audio() {
    }
    private String Id_Audio;


    public Playlist_Audio(String Id_PlaylistAudio, String id_playlist, String CreatedBy, String DateCreated) {
        this.Id_PlaylistAudio = Id_PlaylistAudio;
        this.id_playlist = id_playlist;
        this.CreatedBy = CreatedBy;
        this.DateCreated = DateCreated;
    }

    protected Playlist_Audio(Parcel in) {
        this.Id_PlaylistAudio = in.readString();
        this.id_playlist = in.readString();
        this.CreatedBy = in.readString();
        this.DateCreated = in.readString();
        this.ModifBy = in.readString();
        this.DateModif = in.readString();
        this.Id_Audio = in.readString();
    }

    public String getId_Audio() {
        return Id_Audio;
    }

    public void setId_Audio(String id_Audio) {
        Id_Audio = id_Audio;
    }

    public String getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(String id_playlist) {
        this.id_playlist = id_playlist;
    }
    
    public String getId_PlaylistAudio() {
        return Id_PlaylistAudio;
    }

    public void setId_PlaylistAudio(String Id_PlaylistAudio) {
        this.Id_PlaylistAudio = Id_PlaylistAudio;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String DateCreated) {
        this.DateCreated = DateCreated;
    }

    public String getModifBy() {
        return ModifBy;
    }

    public void setModifBy(String ModifBy) {
        this.ModifBy = ModifBy;
    }

    public String getDateModif() {
        return DateModif;
    }

    public void setDateModif(String DateModif) {
        this.DateModif = DateModif;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id_PlaylistAudio);
        dest.writeString(this.id_playlist);
        dest.writeString(this.CreatedBy);
        dest.writeString(this.DateCreated);
        dest.writeString(this.ModifBy);
        dest.writeString(this.DateModif);
        dest.writeString(this.Id_Audio);
    }
}
