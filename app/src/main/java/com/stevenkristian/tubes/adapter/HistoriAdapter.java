package com.stevenkristian.tubes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.stevenkristian.tubes.Detail;
import com.stevenkristian.tubes.Histori;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.admin.adapter.MotorAdapterAdmin;
import com.stevenkristian.tubes.databinding.AdapterRecyclerViewBinding;
import com.stevenkristian.tubes.model.History;
import com.stevenkristian.tubes.model.Motor;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HistoriAdapter extends RecyclerView.Adapter<HistoriAdapter.HistoriViewHolder>{
    private Context context;
    private List<History> list = new ArrayList<>();



    public HistoriAdapter(Context context, List<History> list){
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_history, parent, false);

        return new HistoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriViewHolder holder, int position) {
        History histori = list.get(position);

        int index = position+1;
        String txt = "HST"+index;
        holder.judul.setText(txt);
        holder.user.setText(histori.getId_User());
        holder.motor.setText(histori.getId_Motor());
        holder.tglPinjam.setText(histori.getTglPinjam());
        holder.tglKembali.setText(histori.getTglKembali());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoriViewHolder extends RecyclerView.ViewHolder {
        TextView judul, user, motor, tglPinjam, tglKembali;

        public HistoriViewHolder(View view){
            super(view);
            judul = view.findViewById(R.id.tv_judul);
            user = view.findViewById(R.id.tv_user);
            motor = view.findViewById(R.id.tv_motor);
            tglPinjam = view.findViewById(R.id.tv_tglPinjam);
            tglKembali = view.findViewById(R.id.tv_tglKembali);
        }

    }
}
