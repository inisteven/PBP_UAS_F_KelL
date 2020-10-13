package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.stevenkristian.tubes.database.DatabaseClient;
import com.stevenkristian.tubes.model.User;

public class Login extends AppCompatActivity {

    private SharedPreferences preferences;
    public static final int mode = Activity.MODE_PRIVATE;
    private MaterialButton signup,login;
    private ImageView image;
    private TextView logo;
    private TextInputLayout username_til, password_til;
    private TextInputEditText username_et, password_et;

    //Authentication
    private String CHANNEL_ID = "Channel 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadPreferences();

        //Animation
        login = (MaterialButton) findViewById(R.id.login_btn);
        signup = (MaterialButton) findViewById(R.id.create_account_btn);
        image = findViewById(R.id.logo_iv);
        logo = findViewById(R.id.logo_tv);
        username_til = findViewById(R.id.username_til);
        password_til = findViewById(R.id.password_til);

        //SignIn
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        //Animation to SignUp page
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);

                Pair[] pairs = new Pair[6];

                pairs[0] = new Pair<View,String>(image, "logo_image");
                pairs[1] = new Pair<View,String>(logo, "logo_text");
                pairs[2] = new Pair<View,String>(username_til, "username_tran");
                pairs[3] = new Pair<View,String>(password_til, "password_tran");
                pairs[4] = new Pair<View,String>(login, "login_tran");
                pairs[5] = new Pair<View,String>(signup, "sign_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    //Mengecek Tulisan Email yang valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Proses Sign in
    private void startSignIn(){
        String email = username_et.getText().toString();
        String password = password_et.getText().toString();

        if(!isValidEmail(email) || password.isEmpty()){
            if(!isValidEmail(email)){
                username_til.setError("Please fill Email Correctly!");
            }else{
                username_til.setError(null);
            }if(password.isEmpty()){
                password_til.setError("Please fill Password Correctly!");
            }else{
                password_til.setError(null);
            }
        }else{
            getUser(email, password);
        }
    }

    private void getUser(String email, String password){
        class GetUser extends AsyncTask<Void,Void, User>{

            @Override
            protected User doInBackground(Void...voids) {
                User result = DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase()
                        .userDao()
                        .getUser(email,password);
                return result;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if(user == null){
                    Toast.makeText(getApplicationContext(), "User not Found", Toast.LENGTH_SHORT).show();
                }else{
                    savePreferences(user);
                    startActivity(new Intent(Login.this, Home.class));
                }
            }
        }

        GetUser get = new GetUser();
        get.execute();
    }

    private void savePreferences(User user){
        Gson gson = new Gson();
        String strUser = gson.toJson(user);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("keyUser",strUser);
        editor.apply();
    }

    private void loadPreferences(){
        String name = "user";
        String strUser;
        preferences = getSharedPreferences(name, mode);
        if(preferences != null){
            strUser = preferences.getString("keyUser", null);
            if(strUser != null){
                startActivity(new Intent(Login.this, Home.class));
            }
        }
    }

}