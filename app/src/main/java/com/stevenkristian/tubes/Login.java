package com.stevenkristian.tubes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    MaterialButton signup,login;
    ImageView image;
    TextView logo;
    TextInputLayout username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (MaterialButton) findViewById(R.id.login_btn);
        signup = (MaterialButton) findViewById(R.id.create_account_btn);
        image = findViewById(R.id.logo_iv);
        logo = findViewById(R.id.logo_tv);
        username = findViewById(R.id.username_til);
        password = findViewById(R.id.password_til);

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
}