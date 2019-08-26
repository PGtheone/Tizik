package com.shif.peterson.tizik.model;

import java.util.ArrayList;

public class SectionDataModel {

    private String headerTitle;
    private ArrayList<Audio_Artiste> allItemsInSection;

    public SectionDataModel() {

    }

    public SectionDataModel(String headerTitle, ArrayList<Audio_Artiste> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;

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


}
