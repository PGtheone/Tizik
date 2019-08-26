package com.shif.peterson.tizik.model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserPlan {

    private static final String COLLECTION_NAME = "UserPlan";
    private String Id_UserPlan;
    private String Id_User;
    private String id_Plan;
    private int Quantite_used;
    private String date_created;
    private boolean is_active;
    private String date_modified;
    public UserPlan() {
    }

    public UserPlan(String id_UserPlan, String id_User, String id_Plan, int Quantite_used, String date_created, boolean is_active) {
        Id_UserPlan = id_UserPlan;
        Id_User = id_User;
        this.id_Plan = id_Plan;
        this.Quantite_used = Quantite_used;
        this.date_created = date_created;
        this.is_active = is_active;
    }

    public static CollectionReference getUserPlanCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public String getId_UserPlan() {
        return Id_UserPlan;
    }

    public void setId_UserPlan(String id_UserPlan) {
        Id_UserPlan = id_UserPlan;
    }

    public String getId_User() {
        return Id_User;
    }

    public void setId_User(String id_User) {
        Id_User = id_User;
    }

    public String getId_Plan() {
        return id_Plan;
    }

    public void setId_Plan(String id_Plan) {
        this.id_Plan = id_Plan;
    }

    public int getQuantite_used() {
        return Quantite_used;
    }

    public void setQuantite_used(int Quantite_used) {
        this.Quantite_used = Quantite_used;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }
}
