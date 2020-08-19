package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import com.tapan.grocydelivery.R;

public class ReviewsActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reviews;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.reviews_toolbar);
    }
}