package com.stevenkristian.tubes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.gson.Gson;
import com.stevenkristian.tubes.adapter.MotorAdapter;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.databinding.ActivityHomeBinding;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class Home extends AppCompatActivity {
    private SharedPreferences preferences;
    public static final int mode = Activity.MODE_PRIVATE;
    private ArrayList<Motor> ListMotor;
    private MotorAdapter adapter;
    private String user;
    private ActivityHomeBinding homeBinding;
    private SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        user = loadPreferences();
        Toast.makeText(this, "Welcome " + user, Toast.LENGTH_LONG).show();
        //get data motor
        ListMotor = new ArrayList<>();

        //recycler view Explore more
        homeBinding.recyclerViewMotor.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        homeBinding.recyclerViewMotor.setItemAnimator(new DefaultItemAnimator());
        getMotor();
        adapter = new MotorAdapter(Home.this, ListMotor);
        homeBinding.recyclerViewMotor.setAdapter(adapter);



        //Bottom Navigation
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
//                    case R.id.history:
//                        startActivity(new Intent(getApplicationContext(),Histori.class));
//                        overridePendingTransition(0,0);
//                        return true;
                    case R.id.store:
                        startActivity(new Intent(getApplicationContext(),Store.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        search();
    }

    private String loadPreferences(){
        String name = "user";
        String strUser;
        preferences = getSharedPreferences(name, mode);
        if(preferences != null){
            strUser = preferences.getString("keyUser", null);
            if(strUser != null){
                return strUser;
            }
        }

        return null;
    }

    private void search() {
        mySearchView=findViewById(R.id.searchView);
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    public void getMotor() {
        //Tambahkan tampil buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data buku");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MotorAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data buku
                    JSONArray jsonArray = response.getJSONArray("data");

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
                Toast.makeText(getApplication(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}