package com.stevenkristian.tubes.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.admin.adapter.MotorAdapterAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.model.History;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class HistoryAdapterAdmin extends RecyclerView.Adapter<HistoryAdapterAdmin.adapterUserViewHolder> {

    private List<History> historyList;
    private List<History> hitoryListFiltered;
    private Context context;
    private int idHistory;
    private View view;
    private HistoryAdapterAdmin.deleteItemListener mListener;


    public HistoryAdapterAdmin(Context context, List<History> historyList,
                             HistoryAdapterAdmin.deleteItemListener mListener) {
        this.context = context;
        this.historyList = historyList;
        this.hitoryListFiltered = historyList;
        this.mListener = mListener;
    }
    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public HistoryAdapterAdmin.adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_history_admin, parent, false);
        return new HistoryAdapterAdmin.adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapterAdmin.adapterUserViewHolder holder, int position) {
        final History history = hitoryListFiltered.get(position);

        holder.tvNomor.setText(history.getStringId());
        holder.tvID.setText(history.getId_User());
        holder.tvIDMotor.setText(history.getId_Motor());
        holder.tvTglPinjam.setText(history.getTglPinjam());
        holder.tvTglKembali.setText(history.getTglKembali());

    }

    @Override
    public int getItemCount() {
        return hitoryListFiltered.size();
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNomor, tvID, tvIDMotor, tvTglKembali, tvTglPinjam;

        public adapterUserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomor = itemView.findViewById(R.id.tvNomor);
            tvID = itemView.findViewById(R.id.tvID);
            tvIDMotor = itemView.findViewById(R.id.tvIDMotor);
            tvTglKembali = itemView.findViewById(R.id.tvTglKembali);
            tvTglPinjam = itemView.findViewById(R.id.tvTglPinjam);
        }
//
//

//        @Override
//        public int getItemCount() {
//            return hitoryListFiltered.size() : 0;
//        }
////    public Filter getFilter() {
////        return new Filter() {
////            @Override
////            protected FilterResults performFiltering(CharSequence charSequence) {
////                String userInput = charSequence.toString().toLowerCase();
////                if (userInput.isEmpty()) {
////                    hitoryListFiltered = historyList;
////                }
////                else {
////                    List<History> filteredList = new ArrayList<>();
////                    for(History history : historyList) {
////                        if(history.getMerk().toLowerCase().contains(userInput))  {
////                            filteredList.add(history);
////                        }
////                    }
////                    hitoryListFiltered = filteredList;
////                }
////                FilterResults filterResults = new FilterResults();
////                filterResults.values = hitoryListFiltered;
////                return filterResults;
////            }
////            @Override
////            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
////                hitoryListFiltered = (ArrayList<Motor>) filterResults.values;
////                notifyDataSetChanged();
////            }
////        };
//    }


    }
}
