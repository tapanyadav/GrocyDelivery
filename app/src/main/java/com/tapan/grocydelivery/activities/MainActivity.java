package com.tapan.grocydelivery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.QuerySnapshot;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.Objects;

public class MainActivity extends BaseActivity {

    String phoneNumber;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDocumentStatus();
    }

    private void checkDocumentStatus() {
        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (Objects.requireNonNull(task.getResult()).exists()) {
                    phoneNumber = task.getResult().getString("delNumber");
                    firebaseFirestore.collection(Constants.mainDelCollection).whereEqualTo("delNumber", phoneNumber).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                            QuerySnapshot queryDocumentSnapshots = task1.getResult();
                            assert queryDocumentSnapshots != null;
                            if (Objects.equals(queryDocumentSnapshots.getDocuments().get(0).get("addDocumentStatus"), false)) {
                                Toast.makeText(MainActivity.this, "Please! upload documents...", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, RequiredDocsActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (Objects.equals(queryDocumentSnapshots.getDocuments().get(0).get("reviewStatus"), false)) {
                                Toast.makeText(MainActivity.this, "Please wait! We are reviewing your documents...", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, UnderReviewActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            } else {
                String error = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}