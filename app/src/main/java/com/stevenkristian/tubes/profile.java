package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.stevenkristian.tubes.Kamera.CameraActivity;
import com.stevenkristian.tubes.admin.viewsMotorAdmin;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.database.DatabaseClient;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class profile extends AppCompatActivity {
    private SharedPreferences preferences;
    public static final int mode = Activity.MODE_PRIVATE;
    TextView fullname_tv;
    TextInputLayout fullname_til, email_til, phone_til, ktp_til;
    TextInputEditText fullname_et, email_et, phone_et, ktp_et;
    MaterialButton btnSignOut, btnUpdate;
    ImageButton profil_ib;
    private String strUser;
    private User user;
    private FirebaseAuth mFirebaseAuth;

    private String selected;
    private Bitmap bitmap;
    private Uri selectedImage = null;
    private static final int PERMISSION_CODE = 1000;
    private String image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(loadPreferences()!=null){
           strUser = loadPreferences();
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

        mFirebaseAuth = FirebaseAuth.getInstance();

        getUser();


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
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),Histori.class));
                        overridePendingTransition(0,0);
                        return true;
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
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getApplication().checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    getApplication().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                            PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openCamera();
                            }
                        }
                        else{
                            openCamera();
                        }
                        alertD.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="galeri";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getApplication().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission,PERMISSION_CODE);
                            }
                            else{
                                openGallery();
                            }
                        }
                        else{
                            openGallery();
                        }
                        alertD.dismiss();
                    }
                });

                alertD.setView(view);
                alertD.show();
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
                mFirebaseAuth.signOut();
                startActivity(new Intent(profile.this, Login.class));
                finish();
            }

        });
    }

    private void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    if(selected.equals("kamera"))
                        openCamera();
                    else
                        openGallery();
                }else{
                    Toast.makeText(this ,"Permision denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null)
        {
            selectedImage = data.getData();
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            profil_ib.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);

        }
        else if(resultCode == RESULT_OK && requestCode == 2 & data != null)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            profil_ib.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
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

        update();
    }

    public void getUser() {
        //Tambahkan tampil buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data User");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_LOGIN+strUser
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    //Mengubah data jsonArray tertentu menjadi json Object
                    JSONObject jsonObject = (JSONObject) response.getJSONObject("data");

                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    String password = jsonObject.getString("password");
                    String phone = jsonObject.getString("phone");
                    String ktp = jsonObject.getString("ktp");
                    String imgURL = jsonObject.getString("imgURL");

                    //Membuat objek user
                    user = new User(id, name, email, password, phone, ktp, imgURL);
                    fullname_tv.setText(user.getFullname());
                    fullname_et.setText(user.getFullname());
                    email_et.setText(user.getEmail());
                    phone_et.setText(user.getPhone());
                    ktp_et.setText(user.getKtp());

                    if(!imgURL.equalsIgnoreCase("-")){
                        Glide.with(getApplication())
                                .load(UserAPI.URL_IMAGE + imgURL)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profil_ib);
                    }

                } catch (JSONException e) {
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

        queue.add(stringRequest);
    }

    public void update(){
        //Tambahkan tambah buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        //meminta tanggapan string dari url yang telah disediakan menggunakan method GET
        //tidak perlu parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading.....");
        progressDialog.setTitle("Mengupdate data User");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan requwst menghaspu data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_UPDATE + user.getId(), new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //disini bagian jika respon berhasil
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi object
                            JSONObject obj = new JSONObject(response);

                            //obj.getstring("message") digunakan untuk mengambil pesan message dari response
                            Toast.makeText(profile.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //respone error
                progressDialog.dismiss();
                Toast.makeText(profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String,String> getParams()
            {
                //disini proses kirim value parameter
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",user.getFullname());
                params.put("phone",user.getPhone());
                params.put("ktp", user.getKtp());
                if(bitmap != null)
                {
                    params.put("imgURL",imageToString(bitmap));
                }

                return params;
            }
        };
        //proses penambahan request yang sudah kiat buat ke requet queue
        queue.add(stringRequest);
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