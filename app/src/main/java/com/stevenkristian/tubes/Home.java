package com.stevenkristian.tubes;

import android.app.Activity;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.gson.Gson;
import com.stevenkristian.tubes.databinding.ActivityHomeBinding;
import com.stevenkristian.tubes.model.User;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private SharedPreferences preferences;
    public static final int mode = Activity.MODE_PRIVATE;
    private ArrayList<Motor> ListMotor;
    private RecyclerViewAdapter adapter;
    private User user;
    private ActivityHomeBinding homeBinding;
    private SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        user = loadPreferences();
        Toast.makeText(this, "Welcome " + user.getFullname(), Toast.LENGTH_SHORT).show();
        //get data motor
        ListMotor = new DaftarMotor().MOTOR;

        //recycler view Explore more
        homeBinding.recyclerViewMotor.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        homeBinding.recyclerViewMotor.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecyclerViewAdapter(Home.this, ListMotor);
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

    private User loadPreferences(){
        String name = "user";
        String strUser;
        preferences = getSharedPreferences(name, mode);
        if(preferences != null){
            strUser = preferences.getString("keyUser", null);
            if(strUser != null){
                Gson gson = new Gson();
                User user;
                user = gson.fromJson(strUser, User.class);

                return user;
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
}