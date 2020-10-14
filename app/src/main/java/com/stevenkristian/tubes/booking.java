package com.stevenkristian.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
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
        //set to layout
        merkMotor.setText(mtr.merk);



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

//        SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
//        Date date1 = dates.(String.valueOf(materialDatePickerAwal));
//        Date date2 = dates.(String.valueOf(materialDatePickerAkhir));
//        long difference = Math.abs(date1.getTime() - date2.getTime());
//        long differenceDates = difference / (24 * 60 * 60 * 1000);
//        String dayDifference = Long.toString(differenceDates);
//        double total= differenceDates*150000;
//        totalHarga.setText("Rp " + total);


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(booking.this, Confirm.class);
                startActivity(intent);
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