package com.tapan.grocydelivery.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class OtpActivity extends BaseActivity {

    EditText et1, et2, et3, et4, et5, et6;
    Button btnOtpLogin;
    TextView tvResend, textViewResendTimer;
    Bundle bundle;
    HashMap<String, Object> userData = new HashMap<>();
    String phone_number, phoneNumber;
    CountDownTimer countDownTimer = null;
    private EditText[] editTexts;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.id.otp_toolbar);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        editTexts = new EditText[]{et1, et2, et3, et4, et5, et6};
        btnOtpLogin = findViewById(R.id.otp_login_btn);
        tvResend = findViewById(R.id.textViewResend);
        textViewResendTimer = findViewById(R.id.resendCounter);
        TextView textViewNumber = findViewById(R.id.textViewNumber);

        phoneNumber = getIntent().getStringExtra("phone_number");
        bundle = getIntent().getExtras();
        assert bundle != null;
        phone_number = bundle.getString("delNumber");
        textViewNumber.setText(phoneNumber);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                String str = credential.getSmsCode();
                assert str != null;
                et1.setText("" + str.charAt(0));
                et2.setText("" + str.charAt(1));
                et3.setText("" + str.charAt(2));
                et4.setText("" + str.charAt(3));
                et5.setText("" + str.charAt(4));
                et6.setText("" + str.charAt(5));
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseTooManyRequestsException)
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                else
                    Toast.makeText(OtpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                et1.addTextChangedListener(new PinTextWatcher(0));
                et2.addTextChangedListener(new PinTextWatcher(1));
                et3.addTextChangedListener(new PinTextWatcher(2));
                et4.addTextChangedListener(new PinTextWatcher(3));
                et5.addTextChangedListener(new PinTextWatcher(4));
                et6.addTextChangedListener(new PinTextWatcher(5));

                et1.setOnKeyListener(new PinOnKeyListener(0));
                et2.setOnKeyListener(new PinOnKeyListener(1));
                et3.setOnKeyListener(new PinOnKeyListener(2));
                et4.setOnKeyListener(new PinOnKeyListener(3));
                et5.setOnKeyListener(new PinOnKeyListener(4));
                et6.setOnKeyListener(new PinOnKeyListener(5));

                Toast.makeText(OtpActivity.this, "Code sent to: " + phoneNumber, Toast.LENGTH_LONG).show();

                countDownTimer = new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000);
                        String finalCount = "00:" + String.format(Locale.US, "%02d", seconds);
                        textViewResendTimer.setText(finalCount);
                    }

                    public void onFinish() {
                        tvResend.setVisibility(View.VISIBLE);
                    }
                };
                countDownTimer.start();
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, OtpActivity.this, mCallbacks);
        btnOtpLogin.setOnClickListener(v -> {
            String code = et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString() + et5.getText().toString() + et6.getText().toString();
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);

            } catch (Exception e) {
                Toast toast = Toast.makeText(OtpActivity.this, "Verification Code is wrong", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        tvResend.setOnClickListener(v -> {
            resendVerificationCode(phoneNumber, mResendToken);
            tvResend.setVisibility(View.INVISIBLE);
        });
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showProgress(this);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, task -> {
                    if (task.isSuccessful()) {
                        hideProgressDialog();
                        if (bundle.size() > 1)
                            saveUserData();
                        else {
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } else {
                        hideProgressDialog();
                        System.out.println("Sign in failed");
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(OtpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    void saveUserData() {
        userData.put("addDocumentStatus", false);
        userData.put("delName", bundle.getString("delName"));
        userData.put("delNumber", bundle.getString("delNumber"));
        userData.put("delCity", bundle.getString("delCity"));
        userData.put("delEmail", bundle.getString("delEmail"));
        userData.put("totalDeliveries", 0);
        userData.put("totalReviews", 0);

        HashMap<String, Object> updateStatus = new HashMap<>();

        firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(userData).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                updateStatus.put("create", true);
                firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection("Documents").document("docs").set(updateStatus);
                Toast.makeText(this, "Upload user data successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpActivity.this, RequiredDocsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Firestore error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;
            String et1Input = et1.getText().toString().trim();
            String et2Input = et2.getText().toString().trim();
            String et3Input = et3.getText().toString().trim();
            String et4Input = et4.getText().toString().trim();
            String et5Input = et5.getText().toString().trim();
            String et6Input = et6.getText().toString().trim();

            btnOtpLogin.setEnabled(!et1Input.isEmpty() && !et2Input.isEmpty() && !et3Input.isEmpty() && !et4Input.isEmpty() && !et5Input.isEmpty() && !et6Input.isEmpty());

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast) {
                editTexts[currentIndex + 1].requestFocus();
                editTexts[currentIndex].clearFocus();
            }

            if (isAllEditTextsFilled()) { // isLast is optional
//                editTexts[currentIndex].clearFocus();
                hideKeyboard();
//                btnOtpLogin.setEnabled(true);
            }
        }

        private void moveToPrevious() {
            if (!isFirst) {
                editTexts[currentIndex - 1].requestFocus();
                editTexts[currentIndex].clearFocus();
            }

        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }
}