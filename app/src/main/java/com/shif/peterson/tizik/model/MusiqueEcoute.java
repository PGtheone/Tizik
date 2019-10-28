package com.shif.peterson.tizik.model;

public class MusiqueEcoute {

   private String date_listened;
    private String date_modified;
    private String id_MusiqueEcoute;
    private String id_media;
    private int nombreFoisEcoute;

    public MusiqueEcoute() {
    }

    public MusiqueEcoute(String date_listened, String date_modified, String id_MusiqueEcoute, String id_media, int nombreFoisEcoute) {
        this.date_listened = date_listened;
        this.date_modified = date_modified;
        this.id_MusiqueEcoute = id_MusiqueEcoute;
        this.id_media = id_media;
        this.nombreFoisEcoute = nombreFoisEcoute;
    }


    public String getDate_listened() {
        return date_listened;
    }

    public void setDate_listened(String date_listened) {
        this.date_listened = date_listened;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getId_MusiqueEcoute() {
        return id_MusiqueEcoute;
    }

    public void setId_MusiqueEcoute(String id_MusiqueEcoute) {
        this.id_MusiqueEcoute = id_MusiqueEcoute;
    }

    public String getId_media() {
        return id_media;
    }

    public void setId_media(String id_media) {
        this.id_media = id_media;
    }

    public int getNombreFoisEcoute() {
        return nombreFoisEcoute;
    }

    public void setNombreFoisEcoute(int nombreFoisEcoute) {
        this.nombreFoisEcoute = nombreFoisEcoute;
    }
}
