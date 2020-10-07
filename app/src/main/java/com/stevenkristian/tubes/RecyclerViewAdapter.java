package com.stevenkristian.tubes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.stevenkristian.tubes.databinding.AdapterRecyclerViewBinding;

import java.util.List;

//public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
//
//    private Context context;
//    private List<Motor>result;
//
//    public RecyclerViewAdapter(Context context, List<Motor> result){
//        this.context = context;
//        this.result = result;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        AdapterRecyclerViewBinding binding = AdapterRecyclerViewBinding.inflate(layoutInflater, parent, false);
//        return new MyViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Motor mtr = result.get(position);
//        holder.binding.setMhs(mtr)
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView npm, nama, fakultas, jurusan, ipk, hobi;
//        private ImageView foto_profil;
//        private CardView parent;
//        private AdapterRecyclerViewBinding binding;
//
//
//        public MyViewHolder(@NonNull AdapterRecyclerViewBinding binding){
//            super(binding.getRoot());
//            this.binding=binding;
//        }
//        public  ViewDataBinding getBinding(){
//            return binding;
//        }
//        public void onClick(View view) {
//            Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}
