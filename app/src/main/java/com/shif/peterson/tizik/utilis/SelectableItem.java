package com.shif.peterson.tizik.utilis;

import com.shif.peterson.tizik.model.Audio_Artiste;

public class SelectableItem extends Audio_Artiste {


    private boolean isSelected = false;
    private String artiste;
    private String nomAlbum;



    public SelectableItem(Audio_Artiste item,String artiste,String album,boolean selected) {
        super(item.getId_musique(), item.getUrl_musique(), item.getTitre_musique(), item.getuploaded_by(), item.getDuree_musique(),item.getUrl_poster());
    this.isSelected = isSelected();
    this.artiste = artiste;
    this.nomAlbum = album;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getNomAlbum() {
        return nomAlbum;
    }

    public void setNomAlbum(String nomAlbum) {
        this.nomAlbum = nomAlbum;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}