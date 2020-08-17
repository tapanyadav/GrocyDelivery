package com.tapan.grocydelivery.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.adapters.FaqAdapter;
import com.tapan.grocydelivery.models.FaqModel;

public class FaqActivity extends BaseActivity {

    RecyclerView recyclerViewFaq;
    FaqAdapter faqAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_faq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.faq_toolbar);
        recyclerViewFaq = findViewById(R.id.recycler_faq);

        showProgress(this);
        Query query = firebaseFirestore.collection("FAQ").orderBy("arrange", Query.Direction.ASCENDING);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                hideProgressDialog();
                recyclerViewFaq.setAdapter(faqAdapter);
                faqAdapter.notifyDataSetChanged();
            }
        });
        FirestoreRecyclerOptions<FaqModel> faqModelFirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<FaqModel>()
                .setQuery(query, FaqModel.class).build();

        faqAdapter = new FaqAdapter(faqModelFirestoreRecyclerOptions, this);
        recyclerViewFaq.setHasFixedSize(true);
        recyclerViewFaq.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        faqAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        faqAdapter.stopListening();
    }
}