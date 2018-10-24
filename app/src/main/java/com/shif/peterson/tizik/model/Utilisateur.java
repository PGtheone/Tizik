package com.shif.peterson.tizik.model;

import android.app.Activity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utilisateur {

    private static final String COLLECTION_NAME = "Utilisateur";

    private static Utilisateur FirebaseUser ;

    public static CollectionReference getUtilisateurCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    private String id_utilisateur;
    private String username;
    private String nom_complet;
    private String sexe;
    private String email;
    private String telephone;
    private String password;
    private String pays;
    private String url_photo;
    private String type_utilisateur;
    private String biographie;
    private String id_plan;
    private String date_inscription;

    public Utilisateur() {
    }

    public Utilisateur(String id_utilisateur, String username, String nom_complet, String sexe, String email, String telephone, String password, String url_photo, String type_utilisateur, String biographie, String id_plan, String date_inscription) {
        this.id_utilisateur = id_utilisateur;
        this.username = username;
        this.nom_complet = nom_complet;
        this.sexe = sexe;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.url_photo = url_photo;
        this.type_utilisateur = type_utilisateur;
        this.biographie = biographie;
        this.id_plan = id_plan;
        this.date_inscription = date_inscription;
    }

    public Utilisateur(String id_utilisateur, String nom_complet, String sexe, String email, String telephone, String password, String url_photo, String type_utilisateur, String id_plan, String date_inscription) {
        this.id_utilisateur = id_utilisateur;
        this.nom_complet = nom_complet;
        this.sexe = sexe;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.url_photo = url_photo;
        this.type_utilisateur = type_utilisateur;
        this.id_plan = id_plan;
        this.date_inscription = date_inscription;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public String getType_utilisateur() {
        return type_utilisateur;
    }

    public void setType_utilisateur(String type_utilisateur) {
        this.type_utilisateur = type_utilisateur;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public String getId_plan() {
        return id_plan;
    }

    public void setId_plan(String id_plan) {
        this.id_plan = id_plan;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDate_inscription() {
        return date_inscription;
    }

    public void setDate_inscription(String date_inscription) {
        this.date_inscription = date_inscription;
    }

    public static Utilisateur getUserById(Activity context, String id){


        Utilisateur.getUtilisateurCollectionReference().document(id).get().addOnSuccessListener(context, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.exists()){

                    FirebaseUser = documentSnapshot.toObject(Utilisateur.class);
                }

            }
        });

        return  FirebaseUser;
    }
}
