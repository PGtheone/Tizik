package com.shif.peterson.tizik.model;

public class Commentaire_Audio {

    private String id_commentaireAudio;
    private String id_audio;
    private String id_utilisateur;
    private String commentaire;
    private float note;
    private String created_by;
    private String date_created;

    public Commentaire_Audio() {
    }

    public Commentaire_Audio(String id_commentaireAudio, String id_audio, String id_utilisateur, String commentaire, String created_by, String date_created) {
        this.id_commentaireAudio = id_commentaireAudio;
        this.id_audio = id_audio;
        this.id_utilisateur = id_utilisateur;
        this.commentaire = commentaire;
        this.created_by = created_by;
        this.date_created = date_created;
    }

    public Commentaire_Audio(String id_commentaireAudio, String id_audio, String id_utilisateur, String commentaire, float note, String created_by, String date_created) {
        this.id_commentaireAudio = id_commentaireAudio;
        this.id_audio = id_audio;
        this.id_utilisateur = id_utilisateur;
        this.commentaire = commentaire;
        this.note = note;
        this.created_by = created_by;
        this.date_created = date_created;
    }


    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getId_commentaireAudio() {
        return id_commentaireAudio;
    }

    public void setId_commentaireAudio(String id_commentaireAudio) {
        this.id_commentaireAudio = id_commentaireAudio;
    }

    public String getId_audio() {
        return id_audio;
    }

    public void setId_audio(String id_audio) {
        this.id_audio = id_audio;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
}
