package com.arqamahmad.languageslearnandtalk;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by B on 8/27/2016.
 * Adapter for the ViewPager
 */
public class CategoryAdapter extends FragmentPagerAdapter {

    //Number of pages in the ViewPager
    private final int NUM_PAGES = 4;
    //Context of the app
    private Context mContext;

    public CategoryAdapter(Context context,FragmentManager fm){
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new FamilyFragment();
        } else if (position == 2) {
            return new PronounsFragment();
        } else {
            return new PhrasesFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_numbers);
        } else if (position == 1) {
            return mContext.getString(R.string.category_family);
        } else if (position == 2) {
            return mContext.getString(R.string.category_pronouns);
        } else {
            return mContext.getString(R.string.category_phrases);
        }
    }
}
