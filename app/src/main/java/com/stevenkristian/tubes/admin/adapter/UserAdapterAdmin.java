package com.stevenkristian.tubes.admin.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.admin.TambahEditMotorAdmin;
import com.stevenkristian.tubes.admin.TambahEditUserAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class UserAdapterAdmin extends RecyclerView.Adapter<UserAdapterAdmin.adapterUserViewHolder> {
    private List<User> userList;
    private List<User> userListFiltered;
    private Context context;
    private View view;
    private MotorAdapterAdmin.deleteItemListener mListener;


    public UserAdapterAdmin(Context context, List<User> userList,
                             MotorAdapterAdmin.deleteItemListener mListener) {
        this.context = context;
        this.userList = userList;
        this.userListFiltered = userList;
        this.mListener = mListener;
    }
    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public UserAdapterAdmin.adapterUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.activity_user_adapter_admin, parent, false);
        return new UserAdapterAdmin.adapterUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterAdmin.adapterUserViewHolder holder, int position) {
        final User user = userListFiltered.get(position);

        holder.tvFullname.setText(user.getFullname());
        holder.tvEmail.setText(user.getEmail());
        holder.tvPhone.setText(user.getPhone());
        holder.tvKTP.setText(user.getKtp());
        Glide.with(context)
                .load(UserAPI.URL_IMAGE+ user.getImgURL())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGambar);

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle data = new Bundle();
                data.putSerializable("user", user);
                data.putString("status", "edit");
                TambahEditUserAdmin tambahEditUser = new TambahEditUserAdmin();
                tambahEditUser.setArguments(data);
                loadFragment(tambahEditUser);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus user ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(user.getId());

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
        private TextView tvFullname,tvEmail,tvPhone,tvKTP,ivEdit,ivHapus;
        private ImageView ivGambar;

        public adapterUserViewHolder(@NonNull View itemView){
            super(itemView);
            tvFullname = itemView.findViewById(R.id.tvMerk);
            tvEmail = itemView.findViewById(R.id.tvTahun);
            tvPhone = itemView.findViewById(R.id.tvPlat);
            tvKTP = itemView.findViewById(R.id.tvWarna);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivHapus = itemView.findViewById(R.id.ivHapus);
        }
    }



    @Override
    public int getItemCount() {
        return (userListFiltered != null) ? userListFiltered.size() : 0;
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString().toLowerCase();
                if (userInput.isEmpty()) {
                    userListFiltered = userList;
                }
                else {
                    List<User> filteredList = new ArrayList<>();
                    for(User user : userList) {
                        if(user.getFullname().toLowerCase().contains(userInput))  {
                            filteredList.add(user);
                        }
                    }
                    userListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userListFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //Fungsi menghapus data mahasiswa
    public void deleteUser(int idUser){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data user");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, UserAPI.URL_DELETE + idUser, new Response.Listener<String>() {
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
        fragmentTransaction.replace(R.id.frame_view_user_admin,fragment)
                .addToBackStack(null)
                .commit();
    }

}