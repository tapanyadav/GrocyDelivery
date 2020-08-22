package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

public class UnderReviewActivity extends BaseActivity {

    LinearLayout linearLayoutFaq, linearLayoutContactUs;
    ConstraintLayout constraintLayoutUnder, constraintLayoutAccept, constraintLayoutReject;
    Button buttonNext;
    TextView textViewCancelReason;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_under_review;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.under_review_toolbar);

        linearLayoutContactUs = findViewById(R.id.linear_contact_us);
        linearLayoutFaq = findViewById(R.id.linear_all_faq);
        constraintLayoutAccept = findViewById(R.id.constraintLayoutAccept);
        constraintLayoutReject = findViewById(R.id.constraintLayoutReject);
        constraintLayoutUnder = findViewById(R.id.constraintLayoutUnder);
        buttonNext = findViewById(R.id.btn_review_next);
        textViewCancelReason = findViewById(R.id.text_review_cancel_reason);

        setCheckText();

        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                assert value != null;
                if (Objects.equals(value.get("applicationStatus"), "reject")) {
                    constraintLayoutReject.setVisibility(View.VISIBLE);
                    constraintLayoutUnder.setVisibility(View.GONE);
                    constraintLayoutAccept.setVisibility(View.GONE);
                    linearLayoutFaq.setVisibility(View.VISIBLE);
                    linearLayoutContactUs.setVisibility(View.VISIBLE);
                    buttonNext.setVisibility(View.GONE);
                } else if (Objects.equals(value.get("applicationStatus"), "accept")) {
                    constraintLayoutAccept.setVisibility(View.VISIBLE);
                    linearLayoutFaq.setVisibility(View.GONE);
                    linearLayoutContactUs.setVisibility(View.GONE);
                    constraintLayoutUnder.setVisibility(View.GONE);
                    constraintLayoutReject.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.VISIBLE);
                } else {
                    constraintLayoutUnder.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonNext.setOnClickListener(v -> changeReviewStatus());

        linearLayoutContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        });

        linearLayoutFaq.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });
    }

    void setCheckText() {
        String text = "Click here to know the reason for application rejection.";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showReasonDialog();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.headingColor, getTheme()));
            }
        };

        ss.setSpan(clickableSpan1, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewCancelReason.setText(ss);
        textViewCancelReason.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showReasonDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.reject_reason_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().width = (int) (getDeviceMetrics(this).widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        TextView textViewClose = alertDialog.findViewById(R.id.reason_head);
        Button buttonUploadAgain = alertDialog.findViewById(R.id.btn_reject_upload);
    }

    private void changeReviewStatus() {
        HashMap<String, Object> hashMapChange = new HashMap<>();
        hashMapChange.put("reviewStatus", true);
        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).update(hashMapChange).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}