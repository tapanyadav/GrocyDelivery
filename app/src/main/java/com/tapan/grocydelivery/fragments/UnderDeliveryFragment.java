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
import com.tapan.grocydelivery.adapters.UnderDeliveryAdapter;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

public class UnderDeliveryFragment extends BaseFragment {
    UnderDeliveryAdapter underDeliveryAdapter;
    RecyclerView recyclerViewUnderDelivery;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_under_delivery, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewUnderDelivery = view.findViewById(R.id.recycler_under_delivery);

        Query query = firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection(Constants.notificationCollection).whereEqualTo("fragmentStatus", "uDelivery");

        FirestoreRecyclerOptions<DeliveryModel> deliveryModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<DeliveryModel>()
                .setQuery(query, DeliveryModel.class).build();

        underDeliveryAdapter = new UnderDeliveryAdapter(deliveryModelFirestoreRecyclerOptions, getActivity());
        recyclerViewUnderDelivery.setHasFixedSize(true);
        recyclerViewUnderDelivery.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewUnderDelivery.setAdapter(underDeliveryAdapter);
        underDeliveryAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        underDeliveryAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        underDeliveryAdapter.stopListening();
    }
}