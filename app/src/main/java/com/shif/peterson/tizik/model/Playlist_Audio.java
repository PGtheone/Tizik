package com.shif.peterson.tizik.model;

public class Playlist_Audio {

    private String id_playlistAudio;
    private String id_playlist;
    private String id_categorie;
    private String created_by;
    private String date_created;
    private String modify_by;
    private String date_modify;

    public Playlist_Audio() {
    }

    public Playlist_Audio(String id_playlistAudio, String id_playlist, String id_categorie, String created_by, String date_created) {
        this.id_playlistAudio = id_playlistAudio;
        this.id_playlist = id_playlist;
        this.id_categorie = id_categorie;
        this.created_by = created_by;
        this.date_created = date_created;
    }

    public String getId_playlistAudio() {
        return id_playlistAudio;
    }

    public void setId_playlistAudio(String id_playlistAudio) {
        this.id_playlistAudio = id_playlistAudio;
    }

    public String getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(String id_playlist) {
        this.id_playlist = id_playlist;
    }

    public String getId_categorie() {
        return id_categorie;
    }

    public void setId_categorie(String id_categorie) {
        this.id_categorie = id_categorie;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
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
