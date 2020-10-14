package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.stevenkristian.tubes.Kamera.CameraActivity;
import com.stevenkristian.tubes.database.DatabaseClient;
import com.stevenkristian.tubes.model.User;

public class profile extends AppCompatActivity {
    private SharedPreferences preferences;
    public static final int mode = Activity.MODE_PRIVATE;
    TextView fullname_tv;
    TextInputLayout fullname_til, email_til, phone_til, ktp_til;
    TextInputEditText fullname_et, email_et, phone_et, ktp_et;
    MaterialButton btnSignOut, btnUpdate;
    ImageButton profil_ib;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(loadPreferences()!=null){
            user = loadPreferences();
        }else{
            Toast.makeText(this, "User not Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Inisialisasi
        profil_ib = findViewById(R.id.fotoProfile);
        btnUpdate = findViewById(R.id.update_btn);
        btnSignOut=findViewById(R.id.btnSignOut);
        fullname_tv = findViewById(R.id.layoutNama);
        fullname_til = findViewById(R.id.layout_fullname);
        email_til = findViewById(R.id.layout_email);
        phone_til = findViewById(R.id.layout_phoneNumber);
        ktp_til = findViewById(R.id.layout_noKtp);
        fullname_et = findViewById(R.id.input_fullname);
        email_et = findViewById(R.id.input_email);
        phone_et = findViewById(R.id.input_phoneNumber);
        ktp_et = findViewById(R.id.input_noKtp);

        fullname_tv.setText(user.getFullname());
        fullname_et.setText(user.getFullname());
        email_et.setText(user.getEmail());
        phone_et.setText(user.getPhone());
        ktp_et.setText(user.getKtp());

        //Bottom navigation
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        return true;
//                    case R.id.history:
//                        startActivity(new Intent(getApplicationContext(),Histor.class));
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

        //Foto Profil
        profil_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                checkCameraPermission();
            }
        });

        //Proses Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdate();
            }
        });

        //Proses SignOut
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePreferences();
                startActivity(new Intent(profile.this, Login.class));
                finish();
            }

        });
    }

    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Error","Permission not available requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
        } else {
            Log.d("Error","Permission has already granted");
        }
    }

    private void startUpdate(){
        String fullname = fullname_et.getText().toString();
        String email = email_et.getText().toString();
        String phone = phone_et.getText().toString();
        String ktp = ktp_et.getText().toString();

        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setKtp(ktp);

        update(user);
    }

    private void update(final User user){
        class UpdateUser extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase()
                        .userDao()
                        .update(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                savePreferences(user);
                Toast.makeText(getApplicationContext(), "User updated", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        }
        UpdateUser updateUser = new UpdateUser();
        updateUser.execute();
    }

    private void deletePreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    private void savePreferences(User user){
        Gson gson = new Gson();
        String strUser = gson.toJson(user);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("keyUser",strUser);
        editor.apply();
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
}