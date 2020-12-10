package com.stevenkristian.tubes.admin.adapter;

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
import com.stevenkristian.tubes.admin.TambahEditMotorAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class MotorAdapterAdmin extends RecyclerView.Adapter<MotorAdapterAdmin.adapterUserViewHolder> {

    private List<Motor> motorList;
    private List<Motor> motorListFiltered;
    private Context context;
    private int idMotor;
    private View view;
    private MotorAdapterAdmin.deleteItemListener mListener;


    public MotorAdapterAdmin(Context context, List<Motor> motorList,
                             MotorAdapterAdmin.deleteItemListener mListener) {
        this.context = context;
        this.motorList = motorList;
        this.motorListFiltered = motorList;
        this.mListener = mListener;
    }
    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_motor_admin, parent, false);
        return new adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterUserViewHolder holder, int position) {
        final Motor motor = motorListFiltered.get(position);

        holder.tvMerk.setText(motor.getMerk());
        holder.tvWarna.setText(motor.getWarna());
        holder.tvPlat.setText(motor.getPlat());
        holder.tvTahun.setText(motor.getTahun());
        holder.tvStatus.setText(motor.getStatus());
        holder.tvHarga.setText(motor.getStringHarga());
        Glide.with(context)
                .load(MotorAPI.URL_IMAGE+ motor.getImgURL())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle data = new Bundle();
                data.putSerializable("motor", motor);
                data.putString("status", "edit");
                TambahEditMotorAdmin tambahEditMotor = new TambahEditMotorAdmin();
                tambahEditMotor.setArguments(data);
                loadFragment(tambahEditMotor);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus motor ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMotor(motor.getIdMotor());

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public class adapterUserViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMerk,tvWarna,tvPlat,tvTahun,tvStatus,tvHarga,ivEdit,ivHapus;
        private ImageView ivGambar;

        public adapterUserViewHolder(@NonNull View itemView){
            super(itemView);
            tvMerk = itemView.findViewById(R.id.tvMerk);
            tvWarna = itemView.findViewById(R.id.tvWarna);
            tvPlat = itemView.findViewById(R.id.tvPlat);
            tvTahun = itemView.findViewById(R.id.tvTahun);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivHapus = itemView.findViewById(R.id.ivHapus);
        }
    }



    @Override
    public int getItemCount() {
        return (motorListFiltered != null) ? motorListFiltered.size() : 0;
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    motorListFiltered = motorList;
                }
                else {
                    List<Motor> filteredList = new ArrayList<>();
                    for(Motor motor : motorList) {
                        if(motor.getMerk().toLowerCase().contains(userInput))  {
                            filteredList.add(motor);
                        }
                    }
                    motorListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = motorListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                motorListFiltered = (ArrayList<Motor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //Fungsi menghapus data mahasiswa
    public void deleteMotor(int IdMotor){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data mahasiswa");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, MotorAPI.URL_DELETE + IdMotor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    mListener.deleteItem(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view_motor_admin,fragment)
                .addToBackStack(null)
                .commit();
    }


}
