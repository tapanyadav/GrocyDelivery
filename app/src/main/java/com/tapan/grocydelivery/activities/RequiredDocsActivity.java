package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.tapan.grocydelivery.R;

public class RequiredDocsActivity extends BaseActivity {

    Button buttonAgree;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_required_docs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.req_docs_toolbar);

        buttonAgree = findViewById(R.id.btn_agree_req_docs);

        buttonAgree.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadDocsActivity.class);
            startActivity(intent);
        });
    }
}