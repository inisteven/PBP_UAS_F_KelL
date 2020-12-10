package com.stevenkristian.tubes.admin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.admin.adapter.MotorAdapterAdmin;
import com.stevenkristian.tubes.admin.adapter.UserAdapterAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class viewsUserAdmin extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapterAdmin adapter;
    private List<User> ListUser;
    private View view;
    private FloatingActionButton floatingButtonPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_user_admin, container, false);

        floatingButtonPlus = (FloatingActionButton) view.findViewById(R.id.floatingButtonPlus);

        floatingButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putString("status", "tambah");
                TambahEditUserAdmin tambahEditUser = new TambahEditUserAdmin();
                tambahEditUser.setArguments(data);
                floatingButtonPlus.hide();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager .beginTransaction()
                        .replace(R.id.frame_view_user_admin, tambahEditUser)
                        .commit();


            }
        });

        loadDaftarUser();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void loadDaftarUser(){
        setAdapter();
        getUser();
    }



    public void setAdapter(){
        getActivity().setTitle("Data User");
        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/

        //biasa
        ListUser = new ArrayList<User>();
        getUser();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new UserAdapterAdmin(view.getContext(), ListUser, new MotorAdapterAdmin.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarUser();
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getUser() {
        //Tambahkan tampil motor disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data user");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data motor
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!ListUser.isEmpty())
                        ListUser.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id               = jsonObject.optInt("id");
                        String name             = jsonObject.optString("name");
                        String email            = jsonObject.optString("email");
                        String password                = jsonObject.optString("password");
                        String phone               = jsonObject.optString("phone");
                        String ktp               = jsonObject.optString("ktp");
                        String imgURL               = jsonObject.optString("imgURL");

                        //Membuat objek user
                        User user = new User(id,name,email, password, phone,ktp,imgURL);

                        //Menambahkan objek user tadi ke list user
                        ListUser.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}