package com.tapan.grocydelivery.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tapan.grocydelivery.R;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResourceId();

    public ProgressDialog dialog;
    Toolbar toolbar;
    // boolean toolbarCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
    }

    void showProgress(Context context) {
        dialog = new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.process_dialog);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    public void hideProgressDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    void setToolbar(int toolbar_id) {
        toolbar = findViewById(toolbar_id);
        setSupportActionBar(toolbar);
//        if (toolbarCheck) {
//            toolbar.setNavigationIcon(R.drawable.icon_menu);
//        } else {
        toolbar.setNavigationIcon(R.drawable.icon_back_new);
//        }
        toolbar.setNavigationOnClickListener(v -> {
                    onBackPressed();
                    finish();
                }
        );


    }
}
