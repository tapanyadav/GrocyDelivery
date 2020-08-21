package com.tapan.grocydelivery.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class ContactUsActivity extends BaseActivity {

    EditText editTextContactName, editTextContactEmail, editTextContactMessage;
    Button buttonSendMessage;
    String delName, delEmail;
    String name, email, message;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            name = editTextContactName.getText().toString().trim();
            email = editTextContactEmail.getText().toString().trim();
            message = editTextContactMessage.getText().toString().trim();
            buttonSendMessage.setEnabled(!name.isEmpty() && validEmail(email) && !message.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.contact_toolbar);
        editTextContactName = findViewById(R.id.contact_people_name);
        editTextContactEmail = findViewById(R.id.contact_people_email);
        editTextContactMessage = findViewById(R.id.contact_message);
        buttonSendMessage = findViewById(R.id.btn_send_message);

        loadDefaultData();

        editTextContactName.addTextChangedListener(textWatcher);
        editTextContactMessage.addTextChangedListener(textWatcher);
        editTextContactEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!validEmail(editTextContactEmail.getText().toString().trim())) {
                    editTextContactEmail.setBackgroundResource(R.drawable.round_corner_error);
                    editTextContactEmail.setError("Invalid email address");
                } else {
                    editTextContactEmail.setBackgroundResource(R.drawable.round_corner);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!editTextContactName.getText().toString().trim().isEmpty() && validEmail(editTextContactEmail.getText().toString().trim()) && !editTextContactMessage.getText().toString().trim().isEmpty()) {
                    buttonSendMessage.setEnabled(true);
                } else {
                    buttonSendMessage.setEnabled(false);
                }
            }
        });

        buttonSendMessage.setOnClickListener(v -> {
            name = editTextContactName.getText().toString().trim();
            email = editTextContactEmail.getText().toString().trim();
            message = editTextContactMessage.getText().toString().trim();

            if (!name.equals("") && !email.equals("") && !message.equals("")) {
                saveData();
            }
        });
    }

    private void loadDefaultData() {
        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                delName = Objects.requireNonNull(task.getResult()).getString("delName");
                delEmail = task.getResult().getString("delEmail");
                editTextContactName.setText(delName);
                editTextContactEmail.setText(delEmail);
            }
        });
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    void saveData() {
        showProgress(this);
        HashMap<String, Object> saveQuery = new HashMap<>();

        saveQuery.put("queryPersonName", name);
        saveQuery.put("queryPersonEmail", email);
        saveQuery.put("queryPersonMessage", message);
        saveQuery.put("status", "unresolved");

        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId())
                .collection("QueryContactUs").add(saveQuery).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();
                editTextContactMessage.setText("");
                editTextContactName.setText(delName);
                editTextContactEmail.setText(delEmail);
                Toast.makeText(this, "Your message is received successfully! we are in touch asap...", Toast.LENGTH_SHORT).show();
            } else {
                hideProgressDialog();
                Toast.makeText(this, "Firestore error!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}