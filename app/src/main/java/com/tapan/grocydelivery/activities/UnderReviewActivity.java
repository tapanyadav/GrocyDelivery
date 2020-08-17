package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.tapan.grocydelivery.R;

public class UnderReviewActivity extends BaseActivity {

    LinearLayout linearLayoutFaq, linearLayoutContactUs;

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

        linearLayoutContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        });

        linearLayoutFaq.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });
    }
}