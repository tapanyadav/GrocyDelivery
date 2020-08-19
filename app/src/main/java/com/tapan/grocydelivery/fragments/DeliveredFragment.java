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
import com.tapan.grocydelivery.activities.BaseFragment;
import com.tapan.grocydelivery.adapters.DeliveredAdapter;
import com.tapan.grocydelivery.models.DeliveryModel;

import java.util.Objects;

public class DeliveredFragment extends BaseFragment {

    DeliveredAdapter deliveredAdapter;
    RecyclerView recyclerViewDelivered;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewDelivered = view.findViewById(R.id.recycler_delivered);

        Query query = firebaseFirestore.collection("DeliveryBoy").document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .collection("Notifications").whereEqualTo("fragmentStatus", "delivered");

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