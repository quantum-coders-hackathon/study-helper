package com.coders.quantum.myapplication.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coders.quantum.myapplication.R;
import com.coders.quantum.myapplication.ui.home_fragments.StudyGoalFragment;
import com.coders.quantum.myapplication.ui.home_fragments.StudyMethodFragment;
import com.coders.quantum.myapplication.ui.home_fragments.StudyTimerFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment.
        switch (position) {
            case 0:
                return new StudyGoalFragment(); // First tab
//            case 1:
//                return new HomeFragment(); // Second tab
            case 1:
                return new StudyMethodFragment(); // Third tab
            case 2:
                return new StudyTimerFragment(); // fourth tab
            default:
                return new StudyTimerFragment();
        }
//        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            // Set tab titles
//            switch (position) {
//                case 0:
//                    return "Tab 1";
//                case 1:
//                    return "Tab 2";
//                case 2:
//                    return "Tab 3";
//            }
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}