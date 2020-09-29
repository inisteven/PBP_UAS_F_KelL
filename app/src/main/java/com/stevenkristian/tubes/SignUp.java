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

public class SignUp extends AppCompatActivity {

    MaterialButton register, have_account;
    ImageView image;
    TextView logo;
    TextInputLayout username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        have_account = findViewById(R.id.have_account_btn);
        register = findViewById(R.id.register_btn);
        username = findViewById(R.id.username_til);
        password = findViewById(R.id.password_til);
        image = findViewById(R.id.logo_iv);
        logo = findViewById(R.id.logo_tv);

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
}