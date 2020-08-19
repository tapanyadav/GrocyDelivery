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
import com.tapan.grocydelivery.adapters.DeliveryAllAdapter;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

public class AllFragment extends BaseFragment {

    DeliveryAllAdapter deliveryAllAdapter;
    RecyclerView recyclerViewDeliveryAll;


    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        recyclerViewDeliveryAll = view.findViewById(R.id.recycler_all);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        showProgress(view.getContext());
        Query query = firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).collection("Notifications").whereEqualTo("allFragment", true);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();
            }
        });
        FirestoreRecyclerOptions<DeliveryModel> deliveryModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<DeliveryModel>()
                .setQuery(query, DeliveryModel.class).build();

        deliveryAllAdapter = new DeliveryAllAdapter(deliveryModelFirestoreRecyclerOptions, getActivity());
        recyclerViewDeliveryAll.setHasFixedSize(true);
        recyclerViewDeliveryAll.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDeliveryAll.setAdapter(deliveryAllAdapter);
        deliveryAllAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        deliveryAllAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        deliveryAllAdapter.stopListening();
    }
}