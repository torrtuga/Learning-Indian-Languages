package com.arqamahmad.languageslearnandtalk;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by B on 8/27/2016.
 * Adapter for the ViewPager
 */
public class CategoryAdapter extends FragmentPagerAdapter {

    //Number of pages in the ViewPager
    private final int NUM_PAGES = 5;
    //Context of the app
    private Context mContext;

    public CategoryAdapter(Context context,FragmentManager fm){
        super(fm);
        mContext = context;
        Toast.makeText(mContext,"Inside Custom Adapter",Toast.LENGTH_SHORT).show();
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ChatFragment();
        } else if (position == 1) {
            return new FamilyFragment();
        } else if (position == 2) {
            return new ColorsFragment();
        } else if (position == 3){
            return new PhrasesFragment();
        }
        else {
            return new NumbersFragment();
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
            return mContext.getString(R.string.category_colors);
        } else if(position == 3) {
            return mContext.getString(R.string.category_phrases);
        }
        else{
            return mContext.getString(R.string.category_chat);
        }
    }
}
