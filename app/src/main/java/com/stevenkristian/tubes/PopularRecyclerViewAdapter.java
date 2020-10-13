//package com.stevenkristian.tubes;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.databinding.DataBindingUtil;
//import androidx.databinding.ViewDataBinding;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.gson.Gson;
//import com.stevenkristian.tubes.databinding.PopularRecylerViewBinding;
//
//import java.util.List;
//
//public class PopularRecyclerViewAdapter extends RecyclerView.Adapter<PopularRecyclerViewAdapter.MyViewHolder>{
//    private Context context;
//    private List<Motor> result;
//
//    public PopularRecyclerViewAdapter(Context context, List<Motor> motor){
//        this.context = context;
//        this.result = motor;
////        for(int i=0; i<result.size();i++){
////            if(!result.get(i).getStatus().equalsIgnoreCase("Tersedia")){
////                this.result.remove(motor.get(i));
////            }
////        }
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        PopularRecylerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.popular_recyler_view,parent,false);
//
//        return new MyViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
//        Motor mtr = result.get(position);
//        holder.recyclerViewBinding.setMtr(mtr);
//
//        final Intent intent = new Intent(context, Detail.class);
//        final Gson gson = new Gson();
//        final String[] strMtr = new String[1];
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (holder.getAdapterPosition()){
//                    case 0:
//                        strMtr[0] = gson.toJson(result.get(0));
//                        intent.putExtra("objMtr", strMtr[0]);
//                        break;
//                    case 1:
//                        strMtr[0] = gson.toJson(result.get(1));
//                        intent.putExtra("objMtr", strMtr[0]);
//                        break;
//                    case 2:
//                        strMtr[0] = gson.toJson(result.get(2));
//                        intent.putExtra("objMtr", strMtr[0]);
//                        break;
//
//                }
//                context.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return result.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        PopularRecylerViewBinding recyclerViewBinding;
//
//
//        public MyViewHolder(@NonNull PopularRecylerViewBinding binding){
//            super(binding.getRoot());
//            recyclerViewBinding = binding;
//        }
//        public ViewDataBinding getBinding(){
//            return recyclerViewBinding;
//        }
//        public void onClick(View view) {
//           Toast.makeText(context, "You touch me?", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}
