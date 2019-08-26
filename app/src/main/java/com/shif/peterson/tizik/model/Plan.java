package com.shif.peterson.tizik.model;

public class Plan {

    private  String Description;
    private String Id_Plan;
    private double Prix;
    private String Titre;
    private String UniteMonetaire;
    private double  tailleMax;
    private double  tempsUpload;

    public Plan(String description, String id_Plan, double prix, String titre, String uniteMonetaire, double tailleMax, double tempsUpload) {
        Description = description;
        Id_Plan = id_Plan;
        Prix = prix;
        Titre = titre;
        UniteMonetaire = uniteMonetaire;
        this.tailleMax = tailleMax;
        this.tempsUpload = tempsUpload;
    }


    public Plan() {
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getId_Plan() {
        return Id_Plan;
    }

    public void setId_Plan(String id_Plan) {
        Id_Plan = id_Plan;
    }

    public double getPrix() {
        return Prix;
    }

    public void setPrix(double prix) {
        Prix = prix;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        Titre = titre;
    }

    public String getUniteMonetaire() {
        return UniteMonetaire;
    }

    public void setUniteMonetaire(String uniteMonetaire) {
        UniteMonetaire = uniteMonetaire;
    }

    public double getTailleMax() {
        return tailleMax;
    }

    public void setTailleMax(double tailleMax) {
        this.tailleMax = tailleMax;
    }

    public double getTempsUpload() {
        return tempsUpload;
    }

    public void setTempsUpload(double tempsUpload) {
        this.tempsUpload = tempsUpload;
    }
}
