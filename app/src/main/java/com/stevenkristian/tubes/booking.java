package com.stevenkristian.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.stevenkristian.tubes.model.Motor;

import java.util.Calendar;
import java.util.TimeZone;

public class booking extends AppCompatActivity {
    private MaterialButton btnTanggalAwal,btnTanggalAkhir;
    private TextView inputTglAwal,inputTglAkhir,totalHarga,merkMotor;
    private Button book;
    private ImageButton btnBack;
    private Motor mtr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //initiate
        btnTanggalAwal = findViewById(R.id.btnTanggalAwal);
        btnTanggalAkhir = findViewById(R.id.btnTanggalAkhir);
        inputTglAwal = findViewById(R.id.inputTglAwal);
        inputTglAkhir = findViewById(R.id.inputTglAkhir);

        book = findViewById(R.id.book_btn);
        btnBack = findViewById(R.id.btnBack);

        //Get Data Motor
        String strMtr = getIntent().getStringExtra("objMtr");
        Gson gson = new Gson();
        mtr = gson.fromJson(strMtr, Motor.class);

        //find layout id
        merkMotor = findViewById(R.id.merkMotor);
        totalHarga = findViewById(R.id.totalHarga);
        //set to layout
        merkMotor.setText(mtr.merk);
        totalHarga.setText("Rp. "+mtr.harga+ " /Hari");



        //datepicker

        Calendar calendar =Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        final long today= MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Pilih Tanggal");
        builder.setSelection(today);
        final MaterialDatePicker materialDatePickerAwal = builder.build();
        final MaterialDatePicker materialDatePickerAkhir = builder.build();

        //tgl awal
        btnTanggalAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePickerAwal.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePickerAwal.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                inputTglAwal.setText(materialDatePickerAwal.getHeaderText());
            }
        });

        //tgl akhir
        btnTanggalAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePickerAkhir.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePickerAkhir.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                inputTglAkhir.setText(materialDatePickerAkhir.getHeaderText());
            }
        });


//        long difference = Math.abs(materialDatePickerAkhir - materialDatePickerAwal);
//        long differenceDates = difference / (24 * 60 * 60 * 1000);
//        String dayDifference = Long.toString(differenceDates);
//        double total= differenceDates*150000;
//        totalHarga.setText("Rp " + total);


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputTglAwal.getText().toString().isEmpty()
                        || inputTglAkhir.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please input the Date!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(booking.this, Confirm.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });


    }
}