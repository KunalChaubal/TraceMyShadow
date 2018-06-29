package com.kunalc.trackerapp.shadow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kunalc.trackerapp.shadow.fragment.CurrentLocationFragment;
import com.kunalc.trackerapp.shadow.fragment.HistoryFragment;

/**
 * Created by KunalC on 6/9/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            case 0:
                // First Fragment of First Tab
                result = HistoryFragment.newInstance(null,null);
                break;
            case 1:
                // First Fragment of Second Tab
                result = CurrentLocationFragment.newInstance(1);
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "HISTORY";
            case 1:
                return "LOCATION";
        }
        return null;
    }
}
