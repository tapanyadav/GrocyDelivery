package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import com.tapan.grocydelivery.R;

public class RequiredDocsActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_required_docs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.req_docs_toolbar);
    }
}