package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import com.tapan.grocydelivery.R;

public class UnderReviewActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_under_review;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.under_review_toolbar);
    }
}