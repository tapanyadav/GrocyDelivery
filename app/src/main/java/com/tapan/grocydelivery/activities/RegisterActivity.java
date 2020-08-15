package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tapan.grocydelivery.R;

public class RegisterActivity extends BaseActivity {


    CheckBox checkBox;
    SpannableStringBuilder spannableStringBuilder;
    EditText editTextName, editTextEmail, editTextPhone, editTextCity;
    TextView textViewRegLogin;
    Button buttonNext;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkBox = findViewById(R.id.check_box);
        editTextName = findViewById(R.id.reg_name);
        editTextEmail = findViewById(R.id.reg_email);
        editTextPhone = findViewById(R.id.reg_number);
        editTextCity = findViewById(R.id.reg_city_name);
        textViewRegLogin = findViewById(R.id.sign_in);
        buttonNext = findViewById(R.id.btn_reg_next);

        setToolbar(R.id.register_toolbar);

        setCheckText();

        setAsterisk("Name");
        editTextName.setHint(spannableStringBuilder);

        setAsterisk("Email");
        editTextEmail.setHint(spannableStringBuilder);

        setAsterisk("Mobile number exclude +91");
        editTextPhone.setHint(spannableStringBuilder);

        setAsterisk("City");
        editTextCity.setHint(spannableStringBuilder);

        textViewRegLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lenNumber = editTextPhone.getText().length();

                if (!editTextName.getText().toString().isEmpty() && editTextEmail.getText().toString().trim().matches(emailPattern) && lenNumber > 9 && !editTextCity.getText().toString().isEmpty() && checkBox.isChecked()) {

                    buttonNext.setEnabled(true);
                } else {

                    buttonNext.setEnabled(false);
                }
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!editTextEmail.getText().toString().trim().matches(emailPattern)) {
                    editTextEmail.setBackgroundResource(R.drawable.round_corner_error);
                    editTextEmail.setError("Invalid email address");
                } else {
                    editTextEmail.setBackgroundResource(R.drawable.round_corner);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int lenNumber = editTextPhone.getText().length();

                if (editTextEmail.getText().toString().trim().matches(emailPattern)) {
                    editTextEmail.setError(null);
                }

                if (!editTextName.getText().toString().isEmpty() && editTextEmail.getText().toString().trim().matches(emailPattern) && lenNumber > 9 && !editTextCity.getText().toString().isEmpty() && checkBox.isChecked()) {

                    buttonNext.setEnabled(true);
                } else {

                    buttonNext.setEnabled(false);
                }

            }

        });

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

                if (!editTextName.getText().toString().isEmpty() && editTextEmail.getText().toString().trim().matches(emailPattern) && lenNumber > 9 && !editTextCity.getText().toString().isEmpty() && checkBox.isChecked()) {

                    buttonNext.setEnabled(true);
                } else {

                    buttonNext.setEnabled(false);
                }
            }
        });

        editTextCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                int lenNumber = editTextPhone.getText().toString().length();

                if (!editTextName.getText().toString().isEmpty() && editTextEmail.getText().toString().trim().matches(emailPattern) && lenNumber > 9 && !editTextCity.getText().toString().isEmpty() && checkBox.isChecked()) {

                    buttonNext.setEnabled(true);
                } else {

                    buttonNext.setEnabled(false);
                }
            }
        });

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!editTextName.getText().toString().isEmpty() && editTextEmail.getText().toString().trim().matches(emailPattern) && !editTextPhone.getText().toString().isEmpty() && !editTextCity.getText().toString().isEmpty() && checkBox.isChecked()) {

                buttonNext.setEnabled(true);

            } else {

                buttonNext.setEnabled(false);

            }

        });

    }

    void setCheckText() {
        String text = "Accept the Terms and Conditions";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(RegisterActivity.this, "One", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.headingColor, getTheme()));
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(RegisterActivity.this, "Two", Toast.LENGTH_SHORT).show();
            }
        };

        ss.setSpan(clickableSpan1, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 21, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        checkBox.setText(ss);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
    }

    void setAsterisk(String astText) {
        String colored = "*";
        spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(astText);
        int start = spannableStringBuilder.length();
        spannableStringBuilder.append(colored);
        int end = spannableStringBuilder.length();
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lightRed, getTheme())), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }
}