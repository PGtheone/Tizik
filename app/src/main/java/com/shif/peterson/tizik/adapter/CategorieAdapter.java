package com.shif.peterson.tizik.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Categorie;

import java.util.ArrayList;
import java.util.List;

public class CategorieAdapter extends ArrayAdapter<Categorie> {


    private Context context;
    private int resourceId;
    private List<Categorie> items, tempItems, suggestions;
    private String query = "";
    /**
     * Custom filter that filters categories according to their name.
     */
    private Filter categorieFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Categorie categorie = (Categorie) resultValue;
            return categorie.getNom_categorie();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if (charSequence != null) {
                query = charSequence.toString();
                suggestions.clear();

                for (Categorie categorie : tempItems) {
                    if (categorie.getNom_categorie().toLowerCase()
                            .startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(categorie);
                    }
                }

                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Categorie> categories = (ArrayList<Categorie>) filterResults.values;
            if (filterResults.count > 0) {
                clear();
                for (Categorie categorie : categories) {
                    add(categorie);
                }
            } else {
                clear();
            }

            notifyDataSetChanged();
        }
    };


    public CategorieAdapter(@NonNull Context context, int resourceId, List<Categorie> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
        }

        Categorie categorie = getItem(position);
        TextView personName = view.findViewById(R.id.person_name);
        ImageView personAvatar = view.findViewById(R.id.person_avatar);

        if (categorie != null) {

           // personAvatar.setImageResource(categorie.getAvatarResource());

            if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.acoustic))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_acoustic)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.blue))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_blues)
                        .into(personAvatar);





            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.compas))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_kompa)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.carnaval))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_carnival)
                        .into(personAvatar);



            }
            else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.kids))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_childrens_music)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.danceedm))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_chill_out)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.classique))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_clasical)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.electro))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_edm)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.musiquer))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_gospel)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.hiphop))
                    || categorie.getNom_categorie().equals(view.getContext().getString(R.string.hiphopk))
                    || categorie.getNom_categorie().equals(view.getContext().getString(R.string.rap))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_hip_hop)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.jazz))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_jazz)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.salsa))
                    || categorie.getNom_categorie().equals(view.getContext().getString(R.string.zouk))
                    || categorie.getNom_categorie().equals(view.getContext().getString(R.string.latino))
                    || categorie.getNom_categorie().equals(view.getContext().getString(R.string.reggaeton))
            ){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_latin)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.metal))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_metal)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.pop))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_pop)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.rnb))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_rnb)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.reggae))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_reggae)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.rock))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_rock)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.musiquea))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_africa)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.soul))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_soul_funk)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.folk))
                    || categorie.getNom_categorie().equals(view.getContext().getString(R.string.voudou))
            ){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_voodoo)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.musiquelove))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_romantic)
                        .into(personAvatar);



            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.mix))){

                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_mix)
                        .into(personAvatar);


            }else if(categorie.getNom_categorie().equals(view.getContext().getString(R.string.retro))){


                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_mix)
                        .into(personAvatar);


            }else{


                Glide.
                        with(view.getContext()).
                        load(R.drawable.ic_world)
                        .into(personAvatar);


            }


            // Highlight the typed query in the suggestions
            SpannableStringBuilder builder = new SpannableStringBuilder(categorie.getNom_categorie());

            builder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, query.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, query.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            personName.setText(builder);
        }

        return view;
    }

    /**
     * Returns the data item associated with the specified position in the data set.
     * @param position position of the item within the adapter's data set of the item we want
     * @return item at the specified position
     */
    @Nullable
    @Override
    public Categorie getItem(int position) {
        return items.get(position);
    }

    /**
     * Returns the item count represented by this adapter.
     * @return item count
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * Returns the row id associated with the specified position in the list.
     * @param position position of the item within the adapter's data set of the item whose id we want
     * @return item id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *  Returns a filter that can be used to constrain data with a filtering pattern.
     * @return filter
     */
    @NonNull
    @Override
    public Filter getFilter() {
        return categorieFilter;
    }
}
