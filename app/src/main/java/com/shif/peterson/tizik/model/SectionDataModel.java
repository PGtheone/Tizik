package com.shif.peterson.tizik.model;

import java.util.ArrayList;

public class SectionDataModel {

    private String headerTitle;
    private ArrayList<Audio_Artiste> allItemsInSection;
    private ArrayList<Audio_Artiste> allPhotoItemsInSection;

    public SectionDataModel() {

    }

    public SectionDataModel(String headerTitle, ArrayList<Audio_Artiste> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;

    }

    public SectionDataModel(String headerTitle, ArrayList<Audio_Artiste> allItemsInSection, ArrayList<Audio_Artiste> allPhotoItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
        this.allPhotoItemsInSection = allPhotoItemsInSection;
    }


    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Audio_Artiste> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<Audio_Artiste> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public ArrayList<Audio_Artiste> getAllPhotoItemsInSection() {
        return allPhotoItemsInSection;
    }

    public void setAllPhotoItemsInSection(ArrayList<Audio_Artiste> allPhotoItemsInSection) {
        this.allPhotoItemsInSection = allPhotoItemsInSection;
    }
}
