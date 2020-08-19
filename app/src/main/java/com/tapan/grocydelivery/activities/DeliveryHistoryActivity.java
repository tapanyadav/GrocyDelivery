package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.adapters.DeliveryHistoryAdapter;
import com.tapan.grocydelivery.models.DeliveryModel;
import com.tapan.grocydelivery.utils.Constants;

public class DeliveryHistoryActivity extends BaseActivity {

    RecyclerView recyclerViewDelHistory;
    DeliveryHistoryAdapter deliveryHistoryAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delivery_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerViewDelHistory = findViewById(R.id.recycler_delivery_history);

        showProgress(this);
        Query query = firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId())
                .collection("Notifications").whereEqualTo("deliveryHistory", true);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recyclerViewDelHistory.setAdapter(deliveryHistoryAdapter);
                deliveryHistoryAdapter.notifyDataSetChanged();
                hideProgressDialog();
            }
        });
        FirestoreRecyclerOptions<DeliveryModel> deliveryModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<DeliveryModel>()
                .setQuery(query, DeliveryModel.class).build();

        deliveryHistoryAdapter = new DeliveryHistoryAdapter(deliveryModelFirestoreRecyclerOptions, this);
        recyclerViewDelHistory.setHasFixedSize(true);
        recyclerViewDelHistory.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStop() {
        super.onStop();
        deliveryHistoryAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        deliveryHistoryAdapter.startListening();
    }
}