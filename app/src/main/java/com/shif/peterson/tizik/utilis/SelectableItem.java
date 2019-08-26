package com.shif.peterson.tizik.utilis;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.Date;

@IgnoreExtraProperties
public class SelectableItem extends Audio_Artiste {


    @Exclude private boolean isSelected = false;
    @Exclude private String artiste;
    @Exclude private String nomAlbum;




    public SelectableItem(Audio_Artiste item,String artiste,String album,boolean selected) {
        super(item.getId_musique(), item.getUrl_musique(), item.getTitre_musique(), item.getuploaded_by(), item.getDuree_musique(),item.getUrl_poster());
    this.isSelected = isSelected();
    this.artiste = artiste;
    this.nomAlbum = album;

    }

    public SelectableItem(String id_musique, String uploaded_by, String titre_musique, String nom_chanteur, String description_musique, double duree_musique, String url_poster, String url_musique, boolean is_Free, double prix, boolean is_actif, Date date_upload, boolean selected, boolean isSelected) {
        super(id_musique, uploaded_by, titre_musique, nom_chanteur, description_musique, duree_musique, url_poster, url_musique, is_Free, prix, is_actif, date_upload, selected);
        this.isSelected = isSelected;
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