package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    TextView textViewRegister;
    EditText editTextPhone;
    Button buttonSendOtp;
    String delPhone;

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
                    hideKeyboard();
                } else {
                    buttonSendOtp.setEnabled(false);
                }
            }
        });

        buttonSendOtp.setOnClickListener(v -> {
            showProgress(this);
            loginButtonAction();
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginButtonAction() {

        delPhone = editTextPhone.getText().toString();
        String phone_number = "+91" + delPhone;

        firebaseFirestore.collection(Constants.mainDelCollection).whereEqualTo("delNumber", delPhone).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();

                if (Objects.requireNonNull(task.getResult()).getDocuments().size() > 0) {
                    Intent otp_intent = new Intent(LoginActivity.this, OtpActivity.class);
                    otp_intent.putExtra("phone_number", phone_number);
                    startActivity(otp_intent);
                } else {
                    Intent signUp_intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    Toast.makeText(LoginActivity.this, "You are not Registered Yet!!", Toast.LENGTH_LONG).show();
                    startActivity(signUp_intent);

                }
            } else {
                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                buttonSendOtp.setEnabled(true);
                hideProgressDialog();
            }
        });
    }
}