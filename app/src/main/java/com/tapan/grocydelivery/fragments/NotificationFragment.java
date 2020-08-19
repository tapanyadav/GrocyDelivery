package com.tapan.grocydelivery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.adapters.NotificationAdapter;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

public class NotificationFragment extends BaseFragment {
    RecyclerView recyclerViewNotification;
    NotificationAdapter notificationAdapter;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerViewNotification = view.findViewById(R.id.recycler_notification);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        showProgress(view.getContext());

        Query query = firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection(Constants.notificationCollection).whereEqualTo("fragmentStatus", "notification");
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();
            }
        });
        FirestoreRecyclerOptions<DeliveryModel> deliveryModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<DeliveryModel>()
                .setQuery(query, DeliveryModel.class).build();

        notificationAdapter = new NotificationAdapter(deliveryModelFirestoreRecyclerOptions, getActivity());
        recyclerViewNotification.setHasFixedSize(true);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewNotification.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }
}