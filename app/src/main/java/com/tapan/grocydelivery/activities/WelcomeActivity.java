package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.tapan.grocydelivery.R;

public class WelcomeActivity extends BaseActivity {

    Button btnLogin, btnSignUp;
    String val;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);

        val = getIntent().getStringExtra("notificationFragment");
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });


        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            if (val != null) {
                mainIntent.putExtra("notificationFragment", val);
            }
            startActivity(mainIntent);
            finish();
        }
    }
}