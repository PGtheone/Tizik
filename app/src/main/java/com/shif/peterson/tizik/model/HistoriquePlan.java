package com.shif.peterson.tizik.model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HistoriquePlan {

    private static final String COLLECTION_NAME = "HistoriquePlan";
    private String id_historiquePlan;
    private String id_utilisateur;
    private String id_NewPlan;
    private String CreatedBy;
    private String DateCreated;
    private String DateModif;
    public HistoriquePlan() {
    }

    public HistoriquePlan(String id_historiquePlan, String id_utilisateur, String id_NewPlan, String dateCreated) {
        this.id_historiquePlan = id_historiquePlan;
        this.id_utilisateur = id_utilisateur;
        this.id_NewPlan = id_NewPlan;
        DateCreated = dateCreated;
    }

    public HistoriquePlan(String id_historiquePlan, String id_utilisateur, String id_NewPlan, String createdBy, String dateCreated, String dateModif) {
        this.id_historiquePlan = id_historiquePlan;
        this.id_utilisateur = id_utilisateur;
        this.id_NewPlan = id_NewPlan;
        CreatedBy = createdBy;
        DateCreated = dateCreated;
        DateModif = dateModif;
    }

    public static CollectionReference getHistoriquePlanCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public String getId_historiquePlan() {
        return id_historiquePlan;
    }

    public void setId_historiquePlan(String id_historiquePlan) {
        this.id_historiquePlan = id_historiquePlan;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getId_NewPlan() {
        return id_NewPlan;
    }

    public void setId_NewPlan(String id_NewPlan) {
        this.id_NewPlan = id_NewPlan;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getDateModif() {
        return DateModif;
    }

    public void setDateModif(String dateModif) {
        DateModif = dateModif;
    }
}
