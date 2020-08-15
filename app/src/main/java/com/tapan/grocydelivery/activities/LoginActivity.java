package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tapan.grocydelivery.R;

public class LoginActivity extends BaseActivity {

    TextView textViewRegister;
    EditText editTextPhone;
    Button buttonSendOtp;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.login_toolbar);

        textViewRegister = findViewById(R.id.textViewRegister);
        editTextPhone = findViewById(R.id.login_NumText);
        buttonSendOtp = findViewById(R.id.btnSendOtp);

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (editTextPhone.getText().toString().trim().matches("0")) {

                    editTextPhone.setError("0 is not allowed at beginning");
                    editTextPhone.setText("");
                    editTextPhone.setBackgroundResource(R.drawable.round_corner_error);
                } else {
                    editTextPhone.setBackgroundResource(R.drawable.round_corner);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int lenNumber = editTextPhone.getText().length();

                if (lenNumber > 9) {

                    buttonSendOtp.setEnabled(true);
                } else {

                    buttonSendOtp.setEnabled(false);
                }
            }
        });

        buttonSendOtp.setOnClickListener(v -> {
            Intent intent = new Intent(this, OtpActivity.class);
            startActivity(intent);
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
}