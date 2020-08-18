package com.tapan.grocydelivery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

public class ProfileFragment extends Fragment {

    ImageView imageViewDelCount, imageViewReviewCount;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageViewDelCount = view.findViewById(R.id.image_sea);
        imageViewReviewCount = view.findViewById(R.id.image_main);

        Glide.with(view.getContext()).load(Constants.linkGradientMain).placeholder(R.drawable.loading_new).into(imageViewReviewCount);
        Glide.with(view.getContext()).load(Constants.linkSubu).placeholder(R.drawable.loading_new).into(imageViewDelCount);
        return view;
    }
}