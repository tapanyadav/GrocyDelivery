package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tapan.grocydelivery.R;

public class AccountSettingActivity extends BaseActivity {

    RadioGroup radioGroup;
    RadioButton radioButtonLight, radioButtonDark;
    TextView textViewOpenClick, textViewTermsClick, textViewPrivacyClick;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_account_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonLight = findViewById(R.id.radioButtonLight);
        radioButtonDark = findViewById(R.id.radioButtonDark);
        textViewOpenClick = findViewById(R.id.text_open_click);
        textViewPrivacyClick = findViewById(R.id.text_privacy_click);
        textViewTermsClick = findViewById(R.id.text_terms_click);

        setToolbar(R.id.setting_toolbar);

        switchDayNight(radioGroup, radioButtonLight, radioButtonDark);
        textViewOpenClick.setOnClickListener(v -> {
            Intent intent = new Intent(this, OpenSourceLibActivity.class);
            startActivity(intent);
        });

        textViewPrivacyClick.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("textName", "Privacy");
            startActivity(intent);
        });

        textViewTermsClick.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("textName", "Terms");
            startActivity(intent);
        });
    }
}