package com.tapan.grocydelivery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.adapters.DeliveredAdapter;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

public class DeliveredFragment extends BaseFragment {

    DeliveredAdapter deliveredAdapter;
    RecyclerView recyclerViewDelivered;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        recyclerViewDelivered = view.findViewById(R.id.recycler_delivered);

        Query query = firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection(Constants.notificationCollection).whereEqualTo("fragmentStatus", "delivered");

        FirestoreRecyclerOptions<DeliveryModel> deliveryModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<DeliveryModel>()
                .setQuery(query, DeliveryModel.class).build();

        deliveredAdapter = new DeliveredAdapter(deliveryModelFirestoreRecyclerOptions, getActivity());
        recyclerViewDelivered.setHasFixedSize(true);
        recyclerViewDelivered.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDelivered.setAdapter(deliveredAdapter);
        deliveredAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        deliveredAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        deliveredAdapter.stopListening();
    }
}