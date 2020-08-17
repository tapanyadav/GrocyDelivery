package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tapan.grocydelivery.R;

public class SplashActivity extends AppCompatActivity {

    Animation animationLogo, animationType;
    TextView textViewLogo, textViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        textViewLogo = findViewById(R.id.textViewHead);
        textViewType = findViewById(R.id.textView_type);

        animationLogo = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        animationType = AnimationUtils.loadAnimation(this, R.anim.type_animation);

        textViewLogo.setAnimation(animationLogo);
        textViewType.setAnimation(animationType);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}