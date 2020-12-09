package com.stevenkristian.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.stevenkristian.tubes.admin.viewsMotorAdmin;
import com.stevenkristian.tubes.api.HistoryAPI;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.model.Motor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class booking extends AppCompatActivity {
    private MaterialButton btnTanggalAwal,btnTanggalAkhir;
    private TextView inputTglAwal,inputTglAkhir,totalHarga,merkMotor;
    private Button book;
    private ImageButton btnBack;
    private Motor mtr;
    String id_user;
    public static final int mode = Activity.MODE_PRIVATE;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getUser();
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
                    tambahHistory();
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

    public void tambahHistory(){
        //Tambahkan tambah buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        //meminta tanggapan string dari url yang telah disediakan menggunakan method GET
        //tidak perlu parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading.....");
        progressDialog.setTitle("Memesan Motor..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan requwst menghaspu data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, HistoryAPI.URL_ADD, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //disini bagian jika respon berhasil
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi object
                            JSONObject obj = new JSONObject(response);
                            //pbj.getString("status") digunakan untuk mengambil pesan status dari response

                            //obj.getstring("message") digunakan untuk mengambil pesan message dari response
                            Toast.makeText(booking.this, "Booking Success", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //respone error
                progressDialog.dismiss();
                Toast.makeText(booking.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String,String> getParams()
            {
                //disini proses kirim value parameter
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user",id_user);
                params.put("id_motor",String.valueOf(mtr.getIdMotor()));
                params.put("tglPinjam",inputTglAwal.getText().toString());
                params.put("tglKembali",inputTglAkhir.getText().toString());

                return params;
            }
        };
        //proses penambahan request yang sudah kiat buat ke requet queue
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