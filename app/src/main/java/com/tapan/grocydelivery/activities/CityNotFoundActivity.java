package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import com.tapan.grocydelivery.R;

public class CityNotFoundActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_city_not_found;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.city_not_toolbar);
    }
}