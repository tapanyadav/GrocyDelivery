package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.fragments.DeliveryFragment;
import com.tapan.grocydelivery.fragments.NotificationFragment;
import com.tapan.grocydelivery.fragments.ProfileFragment;
import com.tapan.grocydelivery.fragments.SupportFragment;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;
    public static HashMap<String, Object> profile_activity_data;
    String phoneNumber, email;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    TextView textViewDelName;
    ImageView imageViewDelImage;
    ScaleAnimation scaleAnimation;
    private DeliveryFragment deliveryFragment;
    private NotificationFragment notificationFragment;
    private SupportFragment supportFragment;
    private ProfileFragment profileFragment;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        frameLayout = findViewById(R.id.main_fragment);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        coordinatorLayout = findViewById(R.id.cardLay);

        deliveryFragment = new DeliveryFragment();
        supportFragment = new SupportFragment();
        profileFragment = new ProfileFragment();

        setToolbar(drawerLayout);
        checkDayNight();

        checkDocumentStatus();

        String notificationFragmentValue = getIntent().getStringExtra("notificationFragment");
        if (notificationFragmentValue != null) {
            if (notificationFragmentValue.equals("Notification Fragment"))
                notificationFragment = new NotificationFragment();
            setFragment(new NotificationFragment());

            bottomNavigationView.setSelectedItemId(R.id.menu_noti);
        } else {
            notificationFragment = new NotificationFragment();
            setFragment(new DeliveryFragment());

        }

        scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        setBottomNav();

        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar.setNavigationIcon(R.drawable.ic_icon_menu);
        animateNavigationDrawer();

        View view = navigationView.getHeaderView(0);
        textViewDelName = view.findViewById(R.id.delDrawerName);
        imageViewDelImage = view.findViewById(R.id.profile_pic);
        loadDrawerData();
    }

    private void setBottomNav() {

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_delivery:
                    setFragment(deliveryFragment);
                    bottomNavigationView.startAnimation(scaleAnimation);
                    return true;
                case R.id.menu_noti:
                    setFragment(notificationFragment);
                    bottomNavigationView.startAnimation(scaleAnimation);
                    return true;
                case R.id.menu_support:
                    setFragment(supportFragment);
                    bottomNavigationView.startAnimation(scaleAnimation);
                    return true;
                case R.id.menu_profile:
                    setFragment(profileFragment);
                    bottomNavigationView.startAnimation(scaleAnimation);
                    return true;
                default:
                    return false;
            }
        });
    }


    private void checkDocumentStatus() {
        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).exists()) {
                    phoneNumber = task.getResult().getString("delNumber");
                    firebaseFirestore.collection(Constants.mainDelCollection).whereEqualTo("delNumber", phoneNumber).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task1.getResult();
                            assert queryDocumentSnapshots != null;
                            if (Objects.equals(queryDocumentSnapshots.getDocuments().get(0).get("addDocumentStatus"), false)) {
                                Toast.makeText(MainActivity.this, "Please! upload documents first...", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, RequiredDocsActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (Objects.equals(queryDocumentSnapshots.getDocuments().get(0).get("reviewStatus"), false)) {
                                Toast.makeText(MainActivity.this, "Please wait! We are reviewing your documents...", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, UnderReviewActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            } else {
                String error = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out, R.anim.push_down_in, R.anim.push_down_out);
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

    void loadDrawerData() {
        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                textViewDelName.setText(document.getString("delName"));
                profile_activity_data = new HashMap<>();
                profile_activity_data = (HashMap<String, Object>) document.getData();
                assert profile_activity_data != null;
                profile_activity_data.put("userId", document.getId());
                profile_activity_data.put("delName", document.get("delName"));
                profile_activity_data.put("delCity", document.get("delCity"));
                profile_activity_data.put("delEmail", document.get("delEmail"));

                if (Objects.requireNonNull(document.getData()).containsKey("delProfileImage")) {
                    profile_activity_data.put("delImage", document.get("delProfileImage"));
                    Glide.with(getApplicationContext()).load((String) document.getData().get("delProfileImage")).placeholder(R.drawable.loading_photo).into(imageViewDelImage);
                }
            }
        });
    }

    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                coordinatorLayout.setScaleX(offsetScale);
                coordinatorLayout.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = coordinatorLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                coordinatorLayout.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.feedback:
                showProgress(this);
                showFeedbackDialog();
                return true;
            case R.id.about:
                Intent intentAboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(intentAboutUs);
                return true;
            case R.id.delivery:
                Intent intentHistory = new Intent(this, DeliveryHistoryActivity.class);
                startActivity(intentHistory);
                return true;

            case R.id.rate_us:
                showRatingDialog();
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFeedbackDialog() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.feedback_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().width = (int) (getDeviceMetrics(this).widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        EditText editTextFeedback = alertDialog.findViewById(R.id.edit_text_feedback);

        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();
                DocumentSnapshot document = task.getResult();
                assert document != null;
                email = document.getString("delEmail");
                TextView delEmail = alertDialog.findViewById(R.id.text_email);
                assert delEmail != null;
                delEmail.setText(email);
            } else {
                hideProgressDialog();
                Toast.makeText(this, "Data load error", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonFeedback = alertDialog.findViewById(R.id.btnFeed);
        ImageView imageViewClose = alertDialog.findViewById(R.id.imageClose);

        assert buttonFeedback != null;
        buttonFeedback.setOnClickListener(v -> {
            showProgress(this);
            assert editTextFeedback != null;
            String feedback = editTextFeedback.getText().toString().trim();
            if (!feedback.equals("")) {
                Map<String, Object> hashFeedback = new HashMap<>();
                hashFeedback.put("feedback", feedback);
                hashFeedback.put("email", email);
                firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection("Feedback").add(hashFeedback).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hideProgressDialog();
                        alertDialog.dismiss();
                        Toast.makeText(this, "Your feedback received!!", Toast.LENGTH_SHORT).show();
                    } else {
                        hideProgressDialog();
                        Toast.makeText(this, "Some error!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                hideProgressDialog();
                Toast.makeText(this, "Please add some feedback!!", Toast.LENGTH_SHORT).show();
            }
        });

        assert imageViewClose != null;
        imageViewClose.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void showRatingDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.rating_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().width = (int) (getDeviceMetrics(this).widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonRate = alertDialog.findViewById(R.id.btnRatingPlay);
        TextView textViewNo = alertDialog.findViewById(R.id.textNoThanks);
        assert buttonRate != null;
        buttonRate.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Play store!", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        assert textViewNo != null;
        textViewNo.setOnClickListener(v -> alertDialog.dismiss());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            doubleBackExit();
        }
    }

}