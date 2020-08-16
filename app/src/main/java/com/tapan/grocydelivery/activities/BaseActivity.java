package com.tapan.grocydelivery.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResourceId();

    public ProgressDialog dialog;
    Toolbar toolbar;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Query queryMain;
    StorageReference storageReference;
    DocumentReference documentReferenceDelPath, documentReferenceDefaultSavePath;
    String currentUser;
    // boolean toolbarCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        documentReferenceDelPath = firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId());
        queryMain = firebaseFirestore.collection(Constants.mainDelCollection);
        documentReferenceDefaultSavePath = firebaseFirestore.collection(Constants.mainDelCollection).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

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

    public String getCurrentUserId() {
        if (firebaseAuth.getCurrentUser() != null) {
            currentUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        }
        return currentUser;
    }

    void setToolbar(int toolbar_id) {
        toolbar = findViewById(toolbar_id);
        setSupportActionBar(toolbar);
//        if (toolbarCheck) {
//            toolbar.setNavigationIcon(R.drawable.icon_menu);
//        } else {
        toolbar.setNavigationIcon(R.drawable.ic_icon_back);
//        }
        toolbar.setNavigationOnClickListener(v -> {
                    onBackPressed();
                    finish();
                }
        );
    }

    void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
