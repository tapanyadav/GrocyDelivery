package com.tapan.grocydelivery.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.activities.AccountSettingActivity;
import com.tapan.grocydelivery.activities.EditProfileActivity;
import com.tapan.grocydelivery.activities.LoginActivity;
import com.tapan.grocydelivery.activities.ReviewsActivity;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

public class ProfileFragment extends BaseFragment {

    ImageView imageViewDelCount, imageViewReviewCount, imageViewEditProfile;
    Button button;
    LinearLayout linearLayoutSetting, linearLayoutReviews;
    SwitchMaterial switchMaterialDeliveryButton;
    HashMap<String, Object> switchStatus = new HashMap<>();
    boolean isSharedPrefs;
    TextView textViewCountDel, textViewCountReviews, textViewDelBoyName, textViewTodayPoint, textViewMonthlyPoint;
    int countDeliveries, countReviews, todayCount, monthlyCount;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewDelCount = view.findViewById(R.id.image_sea);
        imageViewReviewCount = view.findViewById(R.id.image_main);
        button = view.findViewById(R.id.btn_log_out);
        linearLayoutSetting = view.findViewById(R.id.linear_account_setting);
        linearLayoutReviews = view.findViewById(R.id.linear_reviews);
        imageViewEditProfile = Objects.requireNonNull(getActivity()).findViewById(R.id.image_editProf);
        switchMaterialDeliveryButton = view.findViewById(R.id.switchMaterialDelivery);
        textViewDelBoyName = view.findViewById(R.id.del_boy_name);
        textViewCountDel = view.findViewById(R.id.deliveriesCount);
        textViewCountReviews = view.findViewById(R.id.reviewsCount);
        textViewTodayPoint = view.findViewById(R.id.text_today_points);
        textViewMonthlyPoint = view.findViewById(R.id.text_monthly_points);

        Glide.with(view.getContext()).load(Constants.linkGradientMain).placeholder(R.drawable.loading_photo).into(imageViewReviewCount);
        Glide.with(view.getContext()).load(Constants.linkSubu).placeholder(R.drawable.loading_photo).into(imageViewDelCount);

        button.setOnClickListener(v -> logoutDialog(view));
        linearLayoutSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
            startActivity(intent);
        });

        linearLayoutReviews.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReviewsActivity.class);
            startActivity(intent);
        });

        switchDeliveryStatus();
        showProgress(view.getContext());
        loadData();
        return view;
    }

    private void loadData() {

        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();
                DocumentSnapshot document = task.getResult();
                assert document != null;
                countDeliveries = Integer.parseInt("" + document.get("totalDeliveries"));
                countReviews = Integer.parseInt("" + document.get("totalReviews"));
                textViewDelBoyName.setText(document.getString("delName"));
                todayCount = Integer.parseInt("" + document.get("dailyCounts"));
                monthlyCount = Integer.parseInt("" + document.get("monthlyPoints"));
                textViewCountDel.setText(String.valueOf(countDeliveries));
                textViewCountReviews.setText(String.valueOf(countReviews));
                textViewTodayPoint.setText(String.valueOf(todayCount));
                textViewMonthlyPoint.setText(String.valueOf(monthlyCount));
            }
        });
    }


    private void switchDeliveryStatus() {

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("sharedDeliveryPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        isSharedPrefs = sharedPreferences.getBoolean("isSharedPrefs", false);

        if (isSharedPrefs) {
            switchMaterialDeliveryButton.setChecked(true);
        } else {
            switchMaterialDeliveryButton.setChecked(false);
        }

        switchMaterialDeliveryButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchStatus.put("deliveryStatus", true);
                switchMaterialDeliveryButton.setChecked(true);
                editor.putBoolean("isSharedPrefs", true);
                Toast.makeText(getActivity(), "Now, you can get delivery requests..", Toast.LENGTH_SHORT).show();
            } else {
                switchStatus.put("deliveryStatus", false);
                switchMaterialDeliveryButton.setChecked(false);
                editor.putBoolean("isSharedPrefs", false);
                Toast.makeText(getActivity(), "Get back soon...", Toast.LENGTH_SHORT).show();
            }
            editor.apply();

            firebaseFirestore.collection("DeliveryBoy").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                    .update(switchStatus).addOnCompleteListener(task -> {
                //  switchStatus.put("deliveryStatus",false);
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Status changed successfully...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Status not changed...", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void logoutDialog(View v) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.logout_dialog, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        Button buttonYes = alertDialog.findViewById(R.id.btn_logout);
        Button buttonCancel = alertDialog.findViewById(R.id.btn_cancel);
        ImageView imageViewClose = alertDialog.findViewById(R.id.imageClose);


        assert imageViewClose != null;
        imageViewClose.setImageResource(R.drawable.ic_baseline_close);
        assert buttonYes != null;
        buttonYes.setOnClickListener(v1 -> logOut());
        assert buttonCancel != null;
        buttonCancel.setOnClickListener(v1 -> alertDialog.dismiss());

        imageViewClose.setOnClickListener(v1 -> alertDialog.dismiss());
    }

    private void logOut() {
        firebaseAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
        Toast.makeText(getActivity(), "Signed Out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        imageViewEditProfile.setVisibility(View.VISIBLE);
        imageViewEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        imageViewEditProfile.setVisibility(View.INVISIBLE);
    }
}