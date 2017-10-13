package com.furq.popularmovies.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.furq.popularmovies.Fragments.FavouriteMovieFragment;
import com.furq.popularmovies.Fragments.PopularMovieFragment;
import com.furq.popularmovies.Fragments.TopRatedMovieFragment;
import com.furq.popularmovies.R;


/**
 * Created by furqan.khan on 30/09/2017.
 */

public class TabLayoutPager extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private Context context;

    public TabLayoutPager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TopRatedMovieFragment();
        } else if (position == 1) {
            return new PopularMovieFragment();
        } else if (position == 2) {
            return new FavouriteMovieFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab1);
            case 1:
                return context.getString(R.string.tab2);
            case 2:
                return context.getString(R.string.tab3);
            default:
                return null;
        }
    }
}
