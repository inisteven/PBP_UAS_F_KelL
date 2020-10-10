package com.stevenkristian.tubes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private MaterialButton signup,login;
    private ImageView image;
    private TextView logo;
    private TextInputLayout username, password;
    private TextInputEditText username_et, password_et;

    //Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private String CHANNEL_ID = "Channel 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Animation
        login = (MaterialButton) findViewById(R.id.login_btn);
        signup = (MaterialButton) findViewById(R.id.create_account_btn);
        image = findViewById(R.id.logo_iv);
        logo = findViewById(R.id.logo_tv);
        username = findViewById(R.id.username_til);
        password = findViewById(R.id.password_til);

        //SignIn
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);

        mAuth = FirebaseAuth.getInstance();
        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(Login.this, Home.class));
                }
            }
        };

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
                pairs[2] = new Pair<View,String>(username, "username_tran");
                pairs[3] = new Pair<View,String>(password, "password_tran");
                pairs[4] = new Pair<View,String>(login, "login_tran");
                pairs[5] = new Pair<View,String>(signup, "sign_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(stateListener);
    }

    //Mengecek Tulisan Email yang valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //Proses Sign in
    private void startSignIn(){
        String email = username_et.getText().toString();
        String password = password_et.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(Login.this, "Email empty", Toast.LENGTH_LONG).show();
        }
        else if(!isValidEmail(email)) {
            Toast.makeText(Login.this, "Email is invalid", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(Login.this, "Password empty", Toast.LENGTH_LONG).show();
        }
        else if(password.length()<6){
            Toast.makeText(Login.this, "Password too short", Toast.LENGTH_LONG).show();
        } else{
            mAuth.signInWithEmailAndPassword(email, password) //Sign in user ke firebase
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(Login.this, "Login Complete", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

    }
}