package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

public class SplashActivity extends BaseActivity {

    Animation animationLogo, animationType;
    TextView textViewLogo, textViewType;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textViewLogo = findViewById(R.id.textViewHead);
        textViewType = findViewById(R.id.textView_type);

        animationLogo = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        animationType = AnimationUtils.loadAnimation(this, R.anim.type_animation);

        textViewLogo.setAnimation(animationLogo);
        textViewType.setAnimation(animationType);

        if (getIntent().getExtras() != null) {

            String mapLocation = getIntent().getStringExtra("mapLocation");
            String orderDate = getIntent().getStringExtra("orderDate");
            String orderDateTime = getIntent().getStringExtra("orderDateTime");
            String orderDeliveryStatus = getIntent().getStringExtra("orderDeliveryStatus");
            String orderNumberId = getIntent().getStringExtra("orderNumberId");
            String shopAddress = getIntent().getStringExtra("shopAddress");
            String shopImage = getIntent().getStringExtra("shopImage");
            String shopName = getIntent().getStringExtra("shopName");
            String userName = getIntent().getStringExtra("userName");
            String userAddress = getIntent().getStringExtra("userAddress");
            String userImage = getIntent().getStringExtra("userImage");
            String orderFragmentStatus = getIntent().getStringExtra("fragmentStatus");
            String pickStatus = getIntent().getStringExtra("pickStatus");

            HashMap<String, Object> notificationHashMap = new HashMap<>();
            notificationHashMap.put("userImage", userImage);
            notificationHashMap.put("userName", userName);
            notificationHashMap.put("userAddress", userAddress);
            notificationHashMap.put("shopImage", shopImage);
            notificationHashMap.put("shopName", shopName);
            notificationHashMap.put("shopAddress", shopAddress);
            notificationHashMap.put("orderDateTime", orderDateTime);
            notificationHashMap.put("orderNumberId", orderNumberId);
            notificationHashMap.put("mapLocation", mapLocation);
            notificationHashMap.put("orderDate", orderDate);
            notificationHashMap.put("orderDeliveryStatus", orderDeliveryStatus);
            notificationHashMap.put("fragmentStatus", orderFragmentStatus);
            notificationHashMap.put("pickStatus", pickStatus);

            firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (Objects.equals(document.get("deliveryStatus"), true)) {
                        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection(Constants.notificationCollection).add(notificationHashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(Constants.splashTag, "Notification data stores successfully.");
                            } else {
                                Log.d(Constants.splashTag, "Notification data not stores successfully.");
                            }
                        });
                    }
                }
            });
        }

        String notiValue = getIntent().getStringExtra("notificationFragment");

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    if (notiValue != null) {
                        intent.putExtra("notificationFragment", notiValue);
                    }
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