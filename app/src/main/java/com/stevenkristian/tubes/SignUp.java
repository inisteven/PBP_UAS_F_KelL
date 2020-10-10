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

public class SignUp extends AppCompatActivity {

    //Material in Layout
    private MaterialButton register, have_account;
    private ImageView image;
    private TextView logo;
    private TextInputLayout username, password;
    private TextInputEditText fullname_et, username_et, phoneNumber_et, ktp_et, password_et, confirm_et;

    //Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private String CHANNEL_ID = "Channel 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Animation
        have_account = findViewById(R.id.have_account_btn);
        register = findViewById(R.id.register_btn);
        username = findViewById(R.id.username_til);
        password = findViewById(R.id.password_til);
        image = findViewById(R.id.logo_iv);
        logo = findViewById(R.id.logo_tv);

        //SignUp
        fullname_et = findViewById(R.id.fullName_et);
        username_et = findViewById(R.id.username_et);
        phoneNumber_et = findViewById(R.id.phone_et);
        ktp_et = findViewById(R.id.ktp_et);
        password_et = findViewById(R.id.password_et);
        confirm_et = findViewById(R.id.confirm_password_et);

        mAuth = FirebaseAuth.getInstance();
        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(SignUp.this, Home.class));
                }
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password_et.getText().toString().equalsIgnoreCase(confirm_et.getText().toString())){
                    startSignUp();
                }
                else{
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
                pairs[2] = new Pair<View,String>(username, "username_tran");
                pairs[3] = new Pair<View,String>(password, "password_tran");
                pairs[4] = new Pair<View,String>(register, "login_tran");
                pairs[5] = new Pair<View,String>(have_account, "sign_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
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

    //Proses Sign up
    private void startSignUp(){
        String email = username_et.getText().toString();
        String password = password_et.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUp.this, "Email empty", Toast.LENGTH_LONG).show();
        }
        else if(!isValidEmail(email)) {
            Toast.makeText(SignUp.this, "Email is invalid", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUp.this, "Password empty", Toast.LENGTH_LONG).show();
        }
        else if(password.length()<6){
            Toast.makeText(SignUp.this, "Password too short", Toast.LENGTH_LONG).show();
        } else{
            mAuth.createUserWithEmailAndPassword(email, password) //membuat user ke firebase
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_LONG).show();
                            } else{
                                Toast.makeText(SignUp.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                startActivity(new Intent(SignUp.this, Login.class));
                            }
                        }
                    });
        }
    }
}