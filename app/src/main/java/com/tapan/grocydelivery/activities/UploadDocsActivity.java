package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import com.tapan.grocydelivery.R;

public class UploadDocsActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_upload_docs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.upload_docs_toolbar);
    }
}