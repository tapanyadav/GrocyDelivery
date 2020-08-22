package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tapan.grocydelivery.R;

import java.util.Objects;

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

        buttonAgree.setOnClickListener(v -> showConfirmDialog());
    }

    private void showConfirmDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.req_docs_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().width = (int) (getDeviceMetrics(this).widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        TextView textViewClose = alertDialog.findViewById(R.id.doc_confirm_head);
        Button buttonReqAgree = alertDialog.findViewById(R.id.btn_req_agree);
        Button buttonReqCancel = alertDialog.findViewById(R.id.btn_req_cancel);

        assert textViewClose != null;
        textViewClose.setOnClickListener(v -> alertDialog.dismiss());

        assert buttonReqAgree != null;
        buttonReqAgree.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadDocsActivity.class);
            startActivity(intent);
            finish();
        });

        assert buttonReqCancel != null;
        buttonReqCancel.setOnClickListener(v -> alertDialog.dismiss());
    }
}