package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stevenkristian.tubes.adapter.HistoriAdapter;
import com.stevenkristian.tubes.api.HistoryAPI;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.model.History;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class Histori extends AppCompatActivity {
    HistoriAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences preferences;
    private List<History> list;
    public static final int mode = Activity.MODE_PRIVATE;
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);

        getUser();
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view_history);
        adapter = new HistoriAdapter(Histori.this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //Bottom Navigation
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.history);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.history:
                        return true;
                    case R.id.store:
                        startActivity(new Intent(getApplicationContext(),Store.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void getHistory() {
        //Tambahkan tampil motor disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data history transaksi");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Log.i("QUDA", "ID USER:: " + id_user);
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, HistoryAPI.URL_SEARCH+id_user,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), ""+ HistoryAPI.URL_SEARCH+id_user, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("hist");
                    Log.i("QUDA", "Array :: " + jsonArray);
                    if(!list.isEmpty())
                        list.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject   = (JSONObject) jsonArray.get(i);
                        Log.i("QUDA", "json onResponse: " + jsonObject.toString());

                        int id                  = jsonObject.optInt("id");
                        String id_user          = jsonObject.optString("id_user");
                        String id_motor         = jsonObject.optString("id_motor");
                        String tglPinjam        = jsonObject.optString("tglPinjam");
                        String tglKembali       = jsonObject.optString("tglKembali");

                        //Membuat objek user
                        History history = new History(id,id_user,id_motor,tglPinjam,tglKembali);
                        //Menambahkan objek user tadi ke list user
                        list.add(history);
//                        Toast.makeText(Histori.this, list.get(0).getId(), Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    Log.i("QUDA", "MASUK CATCH");
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

    public void getUser() {
        //Tambahkan tampil motor disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_LOGIN+ loadPreferences()
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Mengambil data response json object yang berupa data motor
                    JSONObject jsonArray = response.getJSONObject("data");
                    int id               = jsonArray.optInt("id");

                    id_user = String.valueOf(id);
                    getHistory();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
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
}