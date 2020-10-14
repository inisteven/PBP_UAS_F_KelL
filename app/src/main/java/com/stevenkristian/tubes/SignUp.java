package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stevenkristian.tubes.database.DatabaseClient;
import com.stevenkristian.tubes.model.User;

public class SignUp extends AppCompatActivity {

    //Material in Layout
    private MaterialButton register, have_account;
    private ImageView image;
    private TextView logo;
    private TextInputLayout username_til, password_til, fullname_til, phoneNumber_til, ktp_til, confirm_til;
    private TextInputEditText fullname_et, username_et, phoneNumber_et, ktp_et, password_et, confirm_et;

    //Authentication
    private String CHANNEL_ID = "Channel 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
            addUser();
        }
    }

    private void addUser(){
        final String fullname = fullname_et.getText().toString();
        final String username = username_et.getText().toString();
        final String phone = phoneNumber_et.getText().toString();
        final String ktp = ktp_et.getText().toString();
        final String password = password_et.getText().toString();

        class AddUser extends AsyncTask<Void, Void , Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                User user = new User();
                user.setFullname(fullname);
                user.setEmail(username);
                user.setPhone(phone);
                user.setKtp(ktp);
                user.setPassword(password);

                DatabaseClient.getInstance(getApplicationContext()).getDatabase()
                        .userDao()
                        .insert(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "User Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, Login.class));
            }
        }

        AddUser add = new AddUser();
        add.execute();
    }
}