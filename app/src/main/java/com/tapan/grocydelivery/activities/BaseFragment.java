package com.tapan.grocydelivery.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tapan.grocydelivery.R;

import java.util.Objects;

public abstract class BaseFragment extends Fragment {

    public ProgressDialog dialog;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseAuth firebaseAuth;

    String currentUserId;

    public BaseFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = provideYourFragmentView(inflater, container, savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    public abstract View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    protected void showProgress(Context context) {
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
            currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        }
        return currentUserId;
    }
}
