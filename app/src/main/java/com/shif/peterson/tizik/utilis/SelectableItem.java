package com.shif.peterson.tizik.utilis;

import com.shif.peterson.tizik.model.Audio_Artiste;

public class SelectableItem extends Audio_Artiste {


    private boolean isSelected = false;


    public SelectableItem(Audio_Artiste item,boolean selected) {
        super(item.getId_musique(), item.getUrl_musique(), item.getTitre_musique(), item.getId_artiste(), item.getDuree_musique(),item.getUrl_poster());
    this.isSelected = isSelected();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}