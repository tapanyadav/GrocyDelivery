package com.tapan.grocydelivery.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.adapters.AboutTeamAdapter;
import com.tapan.grocydelivery.adapters.VisionAdapter;
import com.tapan.grocydelivery.models.AboutTeamModel;
import com.tapan.grocydelivery.models.VisionModel;
import com.tapan.grocydelivery.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends BaseActivity {

    private ViewPager2 viewPager2About, viewPager2Vision;
    private Handler sliderHandler = new Handler();
    private Handler sliderVisionHandler = new Handler();
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2About.setCurrentItem(viewPager2About.getCurrentItem() + 1);

        }
    };
    private Runnable sliderVisionRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2Vision.setCurrentItem(viewPager2Vision.getCurrentItem() + 1);
        }
    };

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar(R.id.about_toolbar);
        viewPager2About = findViewById(R.id.pagerSliderTeam);
        viewPager2Vision = findViewById(R.id.pagerVisionTeam);

        List<AboutTeamModel> aboutTeamModelList = new ArrayList<>();
        aboutTeamModelList.add(new AboutTeamModel(Constants.linkTapanImage, "Tapan Yadav", "Not yet decided", Constants.linkGradientMain));
        aboutTeamModelList.add(new AboutTeamModel(Constants.linkChotuImage, "Prashant Bhardwaj", "Not yet decided", Constants.linkGradient));
        aboutTeamModelList.add(new AboutTeamModel(Constants.linkGabbyImage, "Utkarsh Gupta", "Not yet decided", Constants.linkSubu));
        aboutTeamModelList.add(new AboutTeamModel(Constants.linkUtkarshImage, "Utkarsh", "Not yet decided", Constants.linkGradientOne));

        List<VisionModel> visionModelList = new ArrayList<>();
        visionModelList.add(new VisionModel(Constants.linkVision, "Our vision", getString(R.string.vision)));
        visionModelList.add(new VisionModel(Constants.linkMission, "Our Mission", getString(R.string.mission)));
        visionModelList.add(new VisionModel(Constants.linkValues, "Our values", getString(R.string.values)));

        viewPager2Vision.setAdapter(new VisionAdapter(this, visionModelList, viewPager2Vision));
        viewPager2About.setAdapter(new AboutTeamAdapter(this, aboutTeamModelList, viewPager2About));

        viewPager2About.setClipToPadding(false);
        viewPager2About.setClipChildren(false);
        viewPager2About.setOffscreenPageLimit(3);
        viewPager2About.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        viewPager2Vision.setClipToPadding(false);
        viewPager2Vision.setClipChildren(false);
        viewPager2Vision.setOffscreenPageLimit(4);
        viewPager2Vision.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager2About.setPageTransformer(compositePageTransformer);
        viewPager2Vision.setPageTransformer(compositePageTransformer);

        viewPager2About.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }

        });

        viewPager2Vision.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderVisionHandler.removeCallbacks(sliderVisionRunnable);
                sliderVisionHandler.postDelayed(sliderVisionRunnable, 4000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        sliderVisionHandler.postDelayed(sliderVisionRunnable, 4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        sliderVisionHandler.removeCallbacks(sliderVisionRunnable);
    }
}