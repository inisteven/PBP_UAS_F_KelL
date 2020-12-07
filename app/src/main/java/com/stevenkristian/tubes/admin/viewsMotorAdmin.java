package com.stevenkristian.tubes.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.stevenkristian.tubes.adapter.MotorAdapter;
import com.stevenkristian.tubes.admin.adapter.MotorAdapterAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class viewsMotorAdmin extends Fragment {

    private RecyclerView recyclerView;
    private MotorAdapterAdmin adapter;
    private List<Motor> ListMotor;
    private View view;
    private FloatingActionButton floatingButtonPlus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_views_motor_admin, container, false);

        floatingButtonPlus = view.findViewById(R.id.floatingButtonPlus);

//        floatingButtonPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(Fragment fragment) {
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                FragmentManager fragmentManager = activity.getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame_view_motor_admin,fragment)
//                        .addToBackStack(null)
//                        .commit();
//
//            }
//        });

        setAdapter();
        loadDaftarMotor();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void loadDaftarMotor(){
        setAdapter();
        getMotor();
    }



    public void setAdapter(){
        getActivity().setTitle("Data Motor");
        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/

        //biasa
        ListMotor = new ArrayList<Motor>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MotorAdapterAdmin(view.getContext(), ListMotor, new MotorAdapterAdmin.deleteItemListener() {
            @Override
            public void deleteItem(Boolean delete) {
                if(delete){
                    loadDaftarMotor();
                }
            }
        });



        //orientasi
        RecyclerView.LayoutManager layoutManager = null;
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            //landsacpe 2 baris
            layoutManager = new GridLayoutManager(view.getContext(),4);
        }else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            //landsacpe 4 baris
            layoutManager = new GridLayoutManager(view.getContext(),2);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getMotor() {
        //Tambahkan tampil motor disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data motor");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MotorAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data motor
                    JSONArray jsonArray = response.getJSONArray("motor");

                    if(!ListMotor.isEmpty())
                        ListMotor.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id               = jsonObject.optInt("id");
                        String merk             = jsonObject.optString("merk");
                        String warna            = jsonObject.optString("warna");
                        String plat                = jsonObject.optString("plat");
                        String tahun               = jsonObject.optString("tahun");
                        String status               = jsonObject.optString("status");
                        double harga               = jsonObject.optDouble("harga");
                        String imgURL               = jsonObject.optString("imgURL");

                        //Membuat objek user
                        Motor motor = new Motor(id, merk, warna, plat, tahun, status, harga, imgURL);

                        //Menambahkan objek user tadi ke list user
                        ListMotor.add(motor);
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