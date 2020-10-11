package com.stevenkristian.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class Detail extends AppCompatActivity {
    private ImageView motor_iv;
    private Motor mtr;
    private TextView merk_tv, warna_tv, plat_tv, tahun_tv, status_tv, harga_tv;
    private Button pilih;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get Data Motor
        String strMtr = getIntent().getStringExtra("objMtr");
        Gson gson = new Gson();
        mtr = gson.fromJson(strMtr, Motor.class);

        //Find ID in layout
        motor_iv = findViewById(R.id.imageMotor);
        merk_tv = findViewById(R.id.merkMotor);
        warna_tv = findViewById(R.id.isi_warna);
        plat_tv = findViewById(R.id.isi_plat);
        tahun_tv = findViewById(R.id.isi_tahun);
        status_tv = findViewById(R.id.isi_status);
        harga_tv = findViewById(R.id.isi_harga);
        pilih = findViewById(R.id.pilih_btn);

        //Set to layout
        Glide.with(getApplicationContext())
                .load(mtr.imgURL).into(motor_iv);
        merk_tv.setText(mtr.merk);
        warna_tv.setText(mtr.warna);
        plat_tv.setText(mtr.plat);
        tahun_tv.setText(mtr.tahun);
        status_tv.setText(mtr.status);
        harga_tv.setText(mtr.harga);

        pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mtr.getStatus().equalsIgnoreCase("Tersedia")){
                    Intent intent = new Intent(Detail.this, booking.class);
                    Gson gson = new Gson();
                    String strMtr = gson.toJson(mtr);
                    intent.putExtra("objMtr", strMtr);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "This motor is already booked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}