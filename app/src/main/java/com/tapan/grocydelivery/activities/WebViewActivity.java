package com.tapan.grocydelivery.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tapan.grocydelivery.R;

import java.util.Objects;

public class WebViewActivity extends BaseActivity {

    TextView textViewWebToolbarHead;
    WebView webView;
    String link;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.web_view_toolbar);
        textViewWebToolbarHead = findViewById(R.id.loc_web_text_toolbar);
        webView = findViewById(R.id.webView);

        String via = getIntent().getStringExtra("textName");

        assert via != null;
        if (via.equals("Terms")) {
            textViewWebToolbarHead.setText(R.string.web_terms);
            firebaseFirestore.collection("Docs").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if (document.exists()) {
                            if (Objects.equals(document.get("termsStat"), true))
                                link = document.getString("terms");
                        }
                    }
                }
                webView.loadUrl(link);
            });
        } else {
            textViewWebToolbarHead.setText(R.string.web_privacy);
            firebaseFirestore.collection("Docs").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if (document.exists()) {
                            if (Objects.equals(document.get("termsStat"), false))
                                link = document.getString("privacy");

                        }
                    }
                }
                webView.loadUrl(link);
            });
        }
    }
}