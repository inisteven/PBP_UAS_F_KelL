package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stevenkristian.tubes.databinding.AdapterRecyclerViewBinding;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    private Context context;
    private List<Popular> popularList;

    public PopularAdapter(Context context, List<Popular> popularList) {
        this.context = context;
        this.popularList = popularList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AdapterRecyclerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.popular_recyler_view,parent,false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Popular popular = popularList.get(position);
        holder.recyclerViewBinding.setMtr(popular);

        final Intent intent = new Intent(context, Detail.class);
        final Gson gson = new Gson();
        final String[] strMtr = new String[1];
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.getAdapterPosition()){
                    case 0:
                        strMtr[0] = gson.toJson(popularList.get(0));
                        intent.putExtra("objMtr", strMtr[0]);
                        break;
                    case 1:
                        strMtr[0] = gson.toJson(popularList.get(1));
                        intent.putExtra("objMtr", strMtr[0]);
                        break;
                    case 2:
                        strMtr[0] = gson.toJson(popularList.get(2));
                        intent.putExtra("objMtr", strMtr[0]);
                        break;

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterRecyclerViewBinding recyclerViewBinding;

        public MyViewHolder(@NonNull AdapterRecyclerViewBinding binding){
            super(binding.getRoot());
            recyclerViewBinding = binding;
        }
        public ViewDataBinding getBinding(){
            return recyclerViewBinding;
        }
        public void onClick(AdapterRecyclerViewBinding view) {
            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
        }

    }
}