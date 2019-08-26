package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Categorie;

import java.util.List;
import java.util.Random;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder>{

    final CategorieClickHandler categorieClickHandler;
    int[] genresColors;
    private List<Categorie> categorieList;
    private Context mContext;

    public GenreAdapter(Context mContext, List<Categorie> categorieList, CategorieClickHandler categorieClickHandler) {
        this.categorieList = categorieList;
        this.mContext = mContext;
        genresColors =  mContext.getResources().getIntArray(R.array.genres_bg_arrays);
        this.categorieClickHandler = categorieClickHandler;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_genres, null);
        return new GenreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Categorie categorie = categorieList.get(position);
        if(categorie != null){

            holder.txtgenres.setText(categorie.getNom_categorie());
            int randomAndroidColor = genresColors[new Random().nextInt(genresColors.length)];
           // holder.imgbackground.setBackgroundColor(randomAndroidColor);



            if(categorie.getNom_categorie().equals(mContext.getString(R.string.acoustic))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_acoustic)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Faccoustic.jpg?alt=media&token=ed0e11dd-f39b-4e9b-abf3-f58ac5ca8cb0")
                        .into(holder.imagebackground);


            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.blue))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_blues)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fblues.jpg?alt=media&token=c69e50e3-2d43-410d-b685-d7d4d489c377")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.compas))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_kompa)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fkonpa.jpg?alt=media&token=d45129f6-dd75-4c46-86ce-178645142e4d")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.carnaval))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_carnival)
                        .into(holder.imgicon);


                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fcarnival-476816_1280.jpg?alt=media&token=7166de46-5c27-480c-a84a-3ee864100bfc")
                        .into(holder.imagebackground);


            }
            else if(categorie.getNom_categorie().equals(mContext.getString(R.string.kids))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_childrens_music)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fkids.jpg?alt=media&token=013561ed-ead0-4bf3-baf9-736e9b4773ee")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.danceedm))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_chill_out)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fedm.jpg?alt=media&token=8bb64451-864e-44fd-b6b0-c7168b504672")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.classique))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_clasical)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fclassic.jpg?alt=media&token=6c4931ba-d791-488e-9828-6cb392bf0ce7")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.electro))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_edm)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Felectro.jpg?alt=media&token=ba794171-63b9-4756-bd9b-970ff8dbf57f")
                        .into(holder.imagebackground);


            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.musiquer))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_gospel)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fmusic_rel.jpg?alt=media&token=992cc0d3-af28-4fab-abde-a75d6980856c")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.hiphop))
                    || categorie.getNom_categorie().equals(mContext.getString(R.string.hiphopk))
                    || categorie.getNom_categorie().equals(mContext.getString(R.string.rap))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_hip_hop)
                        .into(holder.imgicon);

                if(categorie.getNom_categorie().equals(mContext.getString(R.string.rap))){

                    Glide.with(mContext)
                            .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Ffolk.jpg?alt=media&token=ded94486-33bd-4334-b221-eb5659f836a4")
                            .into(holder.imagebackground);

                }else{

                    Glide.with(mContext)
                            .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frap.jpg?alt=media&token=209310f1-dace-4098-aaef-e3387b979c23")
                            .into(holder.imagebackground);

                }

            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.jazz))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_jazz)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fjazz.jpg?alt=media&token=856a9e55-b34a-4983-a28a-0cc846e056b0")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.salsa))
                    || categorie.getNom_categorie().equals(mContext.getString(R.string.zouk))
                    || categorie.getNom_categorie().equals(mContext.getString(R.string.latino))
                    || categorie.getNom_categorie().equals(mContext.getString(R.string.reggaeton))
            ){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_latin)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Flatino.jpg?alt=media&token=18cdc2f6-c1eb-4a78-9383-da4c8d6bada6")
                        .into(holder.imagebackground);


            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.metal))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_metal)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frock.jpg?alt=media&token=cb461755-3a18-40a9-9bbc-f076b82aa7c4")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.pop))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_pop)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fpop.jpg?alt=media&token=ab05055d-4196-4c74-9fbd-89b33520952e")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.rnb))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_rnb)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frnb.jpg?alt=media&token=ce946d4e-c829-4e56-b2e2-e7669632acb7")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.reggae))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_reggae)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frasta-1915004_1280.jpg?alt=media&token=fd6bf66e-83ad-404f-a465-c0a8d32cb0e7")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.rock))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_rock)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frock.jpg?alt=media&token=cb461755-3a18-40a9-9bbc-f076b82aa7c4")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.musiquea))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_africa)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2FAFmusic.jpg?alt=media&token=d06dc09b-c625-4283-828e-18258ec0f323")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.soul))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_soul_funk)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Ffunk.jpg?alt=media&token=6585f5ef-4198-4622-a5f3-4522a66f552c")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.folk))
                    || categorie.getNom_categorie().equals(mContext.getString(R.string.voudou))
            ){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_voodoo)
                        .into(holder.imgicon);

                if(categorie.getNom_categorie().equals(mContext.getString(R.string.folk))){

                    Glide.with(mContext)
                            .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Ffolk.jpg?alt=media&token=ded94486-33bd-4334-b221-eb5659f836a4")
                            .into(holder.imagebackground);

                }else{

                    Glide.with(mContext)
                            .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fvoodoo-402035_1280.jpg?alt=media&token=7ce4d13c-ab1a-4419-bd2a-342b8efbc02d")
                            .into(holder.imagebackground);

                }

            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.musiquelove))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_romantic)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Flove.jpg?alt=media&token=c540076a-fc4e-4ae2-b392-741d03d2faf7")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.mix))){

                Glide.
                        with(mContext).
                        load(R.drawable.ic_mix)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fmix.jpg?alt=media&token=2efdc60c-8a89-44f2-8b6c-ff0a1c9213a1")
                        .into(holder.imagebackground);



            }else if(categorie.getNom_categorie().equals(mContext.getString(R.string.retro))){


                Glide.
                        with(mContext).
                        load(R.drawable.ic_mix)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fretro.jpg?alt=media&token=a62c6367-9609-4364-ade8-b2cdd61040a5")
                        .into(holder.imagebackground);


            }else{


                Glide.
                        with(mContext).
                        load(R.drawable.ic_world)
                        .into(holder.imgicon);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fworld_music.jpg?alt=media&token=f55dbe63-5002-46cd-ae52-530f1e6dfa36")
                         .into(holder.imagebackground);



            }




        }


    }

    @Override
    public int getItemCount() {
        return  (null != categorieList ? categorieList.size() : 0);
    }


    public interface CategorieClickHandler{

         void OnCategorieClickListener(Categorie categorie);
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView imgbackground;
        private ImageView imagebackground;
    private ImageView imgicon;
    private MyTextView_Ubuntu_Regular txtgenres;


    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);

        imgbackground = itemView.findViewById(R.id.imgbackground);
        imagebackground = itemView.findViewById(R.id.imgbg);
        imgicon = itemView.findViewById(R.id.imgicon);
        txtgenres = itemView.findViewById(R.id.txtgenre);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        categorieClickHandler.OnCategorieClickListener(categorieList.get(getAdapterPosition()));

    }
}


}
