package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tapan.grocydelivery.R;

public class AccountSettingActivity extends BaseActivity {

    RadioGroup radioGroup;
    RadioButton radioButtonLight, radioButtonDark, radioButtonSystem;
    TextView textViewOpenClick;

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
        radioButtonSystem = findViewById(R.id.radioButtonSystem);
        textViewOpenClick = findViewById(R.id.text_open_click);

        setToolbar(R.id.setting_toolbar);

        textViewOpenClick.setOnClickListener(v -> {
            Intent intent = new Intent(this, OpenSourceLibActivity.class);
            startActivity(intent);
        });
    }
}