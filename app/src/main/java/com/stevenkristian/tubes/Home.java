package com.stevenkristian.tubes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.stevenkristian.tubes.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private ArrayList<Motor> ListMotor;
    private RecyclerViewAdapter adapter;

    private ActivityHomeBinding homeBinding;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        //get data motor
        ListMotor = new DaftarMotor().MOTOR;

        //recycler view Explore more
        homeBinding.recyclerViewMotor.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        homeBinding.recyclerViewMotor.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecyclerViewAdapter(Home.this, ListMotor);
        homeBinding.recyclerViewMotor.setAdapter(adapter);


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

    }

}