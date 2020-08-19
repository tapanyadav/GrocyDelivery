package com.tapan.grocydelivery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.activities.ContactUsActivity;
import com.tapan.grocydelivery.activities.FaqActivity;
import com.tapan.grocydelivery.activities.IncentivesRulesActivity;

public class SupportFragment extends BaseFragment {

    LinearLayout linearLayoutContactUs, linearLayoutFaq, linearLayoutIncentives;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_support, container, false);

        linearLayoutContactUs = view.findViewById(R.id.linear_contact_us);
        linearLayoutFaq = view.findViewById(R.id.linear_faq);
        linearLayoutIncentives = view.findViewById(R.id.linear_incentive_rules);

        linearLayoutFaq.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FaqActivity.class);
            startActivity(intent);
        });

        linearLayoutContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContactUsActivity.class);
            startActivity(intent);
        });

        linearLayoutIncentives.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), IncentivesRulesActivity.class);
            startActivity(intent);
        });
        return view;
    }
}