package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.shif.peterson.tizik.R;

import java.util.List;

public class TendanceRecyclerViewAdapter extends RecyclerView.Adapter<TendanceRecyclerViewAdapter.TendanceRecyclerViewHolder>{

    public List<String> listTendance;
    public Context mCOntext;

    public TendanceRecyclerViewAdapter(Context mCOntext, List<String> listTendance) {
        this.listTendance = listTendance;
        this.mCOntext = mCOntext;
    }

    @NonNull
    @Override
    public TendanceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mCOntext).inflate(R.layout.item_recycler_tendance, viewGroup,false);
        return new TendanceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TendanceRecyclerViewHolder holder, int i) {

        String tendance = listTendance.get(i);
        if(null != tendance){

            holder.chip.setText(tendance);

        }
    }

    @Override
    public int getItemCount() {
        return (null != listTendance ? listTendance.size() : 0);
    }

    public class TendanceRecyclerViewHolder extends RecyclerView.ViewHolder{

        public Chip chip;


        public TendanceRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            chip = itemView.findViewById(R.id.chip);
        }
    }
}
