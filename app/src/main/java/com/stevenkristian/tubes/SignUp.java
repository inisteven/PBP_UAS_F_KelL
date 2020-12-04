package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.stevenkristian.tubes.api.MotorAPI;
import com.stevenkristian.tubes.api.UserAPI;
import com.stevenkristian.tubes.database.DatabaseClient;
import com.stevenkristian.tubes.model.Motor;
import com.stevenkristian.tubes.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class SignUp extends AppCompatActivity {

    //Material in Layout
    private MaterialButton register, have_account;
    private ImageView image;
    private TextView logo;
    private TextInputLayout username_til, password_til, fullname_til, phoneNumber_til, ktp_til, confirm_til;
    private TextInputEditText fullname_et, username_et, phoneNumber_et, ktp_et, password_et, confirm_et;

    //Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;

    private String CHANNEL_ID = "Channel 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        //Animation
        have_account = findViewById(R.id.have_account_btn);
        register = findViewById(R.id.register_btn);
        username_til = findViewById(R.id.username_til);
        password_til = findViewById(R.id.password_til);
        image = findViewById(R.id.logo_iv);
        logo = findViewById(R.id.logo_tv);

        //SignUp
        fullname_et = findViewById(R.id.fullName_et);
        username_et = findViewById(R.id.username_et);
        phoneNumber_et = findViewById(R.id.phone_et);
        ktp_et = findViewById(R.id.ktp_et);
        password_et = findViewById(R.id.password_et);
        confirm_et = findViewById(R.id.confirm_password_et);

        fullname_til = findViewById(R.id.fullName_til);
        phoneNumber_til = findViewById(R.id.phone_til);
        ktp_til = findViewById(R.id.ktp_til);
        confirm_til = findViewById(R.id.confirm_password_til);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password_et.getText().toString().equalsIgnoreCase(confirm_et.getText().toString())){
                    startSignUp();
                }
                else{
                    if(confirm_et.getText().toString().isEmpty()){
                        confirm_til.setError("Please fill Confirm Password Correctly!");
                    }else{
                        confirm_til.setError(null);
                    }if(password_et.getText().toString().isEmpty()){
                        password_til.setError("Please fill Password Correctly!");
                    }else{
                        password_til.setError(null);
                    }
                    Toast.makeText(getApplicationContext(), "Confirm Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //animation to signin page
        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);

                Pair[] pairs = new Pair[6];

                pairs[0] = new Pair<View,String>(image, "logo_image");
                pairs[1] = new Pair<View,String>(logo, "logo_text");
                pairs[2] = new Pair<View,String>(username_til, "username_tran");
                pairs[3] = new Pair<View,String>(password_til, "password_tran");
                pairs[4] = new Pair<View,String>(register, "login_tran");
                pairs[5] = new Pair<View,String>(have_account, "sign_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }



    //Mengecek Tulisan Email yang valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Proses Sign up
    public void startSignUp(){
        String fullname = fullname_et.getText().toString();
        String username = username_et.getText().toString();
        String phone = phoneNumber_et.getText().toString();
        String ktp = ktp_et.getText().toString();
        String password = password_et.getText().toString();
        String confirm = confirm_et.getText().toString();

        if(fullname.isEmpty() || !isValidEmail(username) || phone.isEmpty() ||
        ktp.isEmpty() || password.isEmpty() || confirm.isEmpty()){
            if(fullname.isEmpty()){
                fullname_til.setError("Please fill Full Name Correctly!");
            }else{
                fullname_til.setError(null);
            }if(!isValidEmail(username)){
                username_til.setError("Please fill Email Correctly!");
            }else{
                username_til.setError(null);
            }if(phone.isEmpty()){
                phoneNumber_til.setError("Please fill Phone Number Correctly!");
            }else{
                phoneNumber_til.setError(null);
            }if(ktp.isEmpty()){
                ktp_til.setError("Please fill KTP Correctly!");
            }else{
                ktp_til.setError(null);
            }if(password.isEmpty()){
                password_til.setError("Please fill Password Correctly!");
            }else{
                password_til.setError(null);
            }if(confirm.isEmpty()){
                confirm_til.setError("Please fill Confirm Password Correctly!");
            }else{
                confirm_til.setError(null);
            }
        }else{
            mAuth.createUserWithEmailAndPassword(username, password) //membuat user ke firebase
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_LONG).show();
                            } else{
                                mAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            tambahUser(fullname, username, password, phone, ktp);

                                        }else{
                                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
        }
    }


    public void tambahUser(final String name, final String email, final String password, final String phone, final String ktp){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data user");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, UserAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(SignUp.this, "Registration Success! Please check your email for verification !", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this, Login.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(SignUp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("ktp", ktp);
                params.put("imgURL", "-");

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}