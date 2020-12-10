package com.stevenkristian.tubes.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.stevenkristian.tubes.R;
import com.stevenkristian.tubes.SignUp;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class TambahEditUserAdmin extends Fragment {
    private TextInputEditText txtFullname,txtEmail, txtPassword, txtPhone, txtKtp;
    private ImageView ivGambar;
    private Button btnBatal, btnSimpan, btnUpload;
    private int idUser;
    private String status, selected;
    private User user;
    private View view;
    private int EditImg;
    private Bitmap bitmap = null;
    private Bitmap editBitmap = null;
    private BitmapDrawable drawable;
    private Uri selectedImage = null;
    private static final int PERMISSION_CODE = 1000;

    //Authentication
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tambah_edit_user_admin, container, false);
        init();
        setAttribut();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void init(){
        user       = (User) getArguments().getSerializable("user");
        txtFullname     = view.findViewById(R.id.txtMerk);
        txtEmail     = view.findViewById(R.id.txtPlat);
        txtPassword    = view.findViewById(R.id.txtWarna);
        txtPhone    = view.findViewById(R.id.txtTahun);
        txtKtp   = view.findViewById(R.id.txtStatus);
        ivGambar    = view.findViewById(R.id.ivGambar);
        btnBatal    = view.findViewById(R.id.btnBatal);
        btnSimpan   = view.findViewById(R.id.btnSimpan);
        btnUpload   = view.findViewById(R.id.btnUpload);
        ivGambar    = view.findViewById(R.id.ivGambar);
        mAuth = FirebaseAuth.getInstance();

        status = getArguments().getString("status");
        if(status.equals("edit"))
        {
            idUser = user.getId();
            txtFullname.setText(user.getFullname());
            txtEmail.setText(user.getEmail());
            txtEmail.setClickable(false);
            txtEmail.setFocusable(false);
            txtPassword.setClickable(false);
            txtPassword.setFocusable(false);
            txtPhone.setText(user.getPhone());
            txtKtp.setText(user.getKtp());

            Glide.with(view.getContext())
                    .load(UserAPI.URL_IMAGE +user.getImgURL())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivGambar);
        }
    }

    private void setAttribut() {
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View view = layoutInflater.inflate(R.layout.pilih_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(view.getContext()).create();

                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selected="kamera";
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==
                                    PackageManager.PERMISSION_DENIED ||
                                    getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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
                            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
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

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname  = txtFullname.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String phone = txtPhone.getText().toString();
                String ktp = txtKtp.getText().toString();
                String imgURL = "-";

                if(status.equals("edit")){
                    if(password.isEmpty())
                    {
                        password = "-";
                    }
                }

                if(bitmap != null){
                    imgURL = imageToString(bitmap);
                }

                if(fullname.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || ktp.isEmpty())
                    Toast.makeText(getContext(), "Data Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                else{
//                    user = new Motor(merk, warna, plat,tahun,statusMotor,harga,imgUrl);

                    if(status.equals("tambah"))
                        tambahMotor(fullname, email, password, phone,ktp,imgURL);
                    else
                        editMotor(user.getId(),fullname, email, password,phone,ktp,imgURL);
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new viewsUserAdmin());
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
                    Toast.makeText(getContext() ,"Permision denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            selectedImage = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            ivGambar.setImageBitmap(null);
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
        else if(resultCode == RESULT_OK && requestCode == 2)
        {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ivGambar.setImageBitmap(null);
            ivGambar.setImageBitmap(bitmap);
            bitmap = getResizedBitmap(bitmap, 512);
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            fragmentTransaction.setReorderingAllowed(false);
        }
        fragmentTransaction.replace(R.id.fragment_tambah_edit_user_admin, fragment)
                .detach(this)
                .attach(this)
                .commit();
    }

    public void closeFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(TambahEditUserAdmin.this).detach(this)
                .attach(this).commit();
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

    //Create
    public void tambahMotor(String fullname, String email, String password, String phone, String ktp, String imgURL){
        //Tambahkan tambah buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //meminta tanggapan string dari url yang telah disediakan menggunakan method GET
        //tidak perlu parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading.....");
        progressDialog.setTitle("Menambahkan data User");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan requwst menghaspu data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_ADD, new
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
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            mAuth.createUserWithEmailAndPassword(email, password) //membuat user ke firebase
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(!task.isSuccessful()){
                                                Toast.makeText(view.getContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                                            } else{
                                                mAuth.getCurrentUser().sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(!task.isSuccessful()){
                                                                    Toast.makeText(view.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });

                            loadFragment(new viewsUserAdmin());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //respone error
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String,String> getParams()
            {
                //disini proses kirim value parameter
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",fullname);
                params.put("email",email);
                params.put("password",password);
                params.put("phone",phone);
                params.put("ktp",ktp);
                params.put("imgURL",imgURL);

                return params;
            }
        };
        //proses penambahan request yang sudah kiat buat ke requet queue
        queue.add(stringRequest);
    }

    public void editMotor(int idUser,String name, String email, String password, String phone, String ktp, String imgURL) {

        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //meminta tanggapan string dari url yang telah disediakan menggunakan method GET
        //tidak perlu parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading.....");
        progressDialog.setTitle("Mengubah data user");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(PUT, UserAPI.URL_UPDATE+ idUser, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //disini bagian jika respon berhasil
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi object
                            JSONObject obj = new JSONObject(response);

                            //obj.getstring("message") digunakan untuk mengambil pesan message dari response
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            loadFragment(new viewsUserAdmin());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //respone error
                progressDialog.dismiss();
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected  Map<String,String>getParams()
            {
                //disini proses kirim value parameter
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",password);
                params.put("phone",phone);
                params.put("ktp",ktp);
                params.put("imgURL", imgURL);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}