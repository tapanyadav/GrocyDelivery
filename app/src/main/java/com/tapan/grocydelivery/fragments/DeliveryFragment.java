package com.tapan.grocydelivery.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeliveryFragment extends BaseFragment {
    TabLayout tabLayout;

    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        AllFragment allFragment = new AllFragment();
        UnderDeliveryFragment underDeliveryFragment = new UnderDeliveryFragment();
        DeliveredFragment deliveredFragment = new DeliveredFragment();

        tabLayout.setupWithViewPager(viewPager, true);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);

        viewPagerAdapter.addFragment(allFragment, "All");
        viewPagerAdapter.addFragment(underDeliveryFragment, "Under delivery");
        viewPagerAdapter.addFragment(deliveredFragment, "Delivered");

        viewPager.setAdapter(viewPagerAdapter);

        getBadgeCount();

        return view;
    }

    void getBadgeCount() {

        firebaseFirestore.collection(Constants.mainDelCollection).document(getCurrentUserId()).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                assert value != null;
                int allCount = Integer.parseInt("" + value.get("badgeCountAll"));
                int underCount = Integer.parseInt("" + value.get("badgeCountUnder"));
                int delCount = Integer.parseInt("" + value.get("totalDeliveries"));


                BadgeDrawable badgeDrawableAll = Objects.requireNonNull(tabLayout.getTabAt(0)).getOrCreateBadge();
                badgeDrawableAll.setBadgeTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getContext()).getTheme()));
                badgeDrawableAll.setHorizontalOffset(-12);
                if (allCount != 0) {
                    badgeDrawableAll.setNumber(allCount);
                    badgeDrawableAll.setVisible(true);
                } else {
                    badgeDrawableAll.setNumber(0);
                }


                BadgeDrawable badgeDrawableUnder = Objects.requireNonNull(tabLayout.getTabAt(1)).getOrCreateBadge();
                badgeDrawableUnder.setBadgeTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getContext()).getTheme()));
                badgeDrawableUnder.setHorizontalOffset(-22);
                if (underCount != 0) {
                    badgeDrawableUnder.setNumber(underCount);
                    badgeDrawableUnder.setVisible(true);
                } else {
                    badgeDrawableUnder.setNumber(0);
                }

                BadgeDrawable badgeDrawableDelivered = Objects.requireNonNull(tabLayout.getTabAt(2)).getOrCreateBadge();
                badgeDrawableDelivered.setBadgeTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getContext()).getTheme()));
                badgeDrawableDelivered.setHorizontalOffset(-16);
                if (delCount != 0) {
                    badgeDrawableDelivered.setNumber(delCount);
                    badgeDrawableDelivered.setVisible(true);
                } else {
                    badgeDrawableDelivered.setNumber(0);
                }
            }
        });
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}