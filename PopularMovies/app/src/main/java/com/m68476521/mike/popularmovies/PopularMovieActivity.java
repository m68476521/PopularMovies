package com.m68476521.mike.popularmovies;

import android.support.v4.app.Fragment;

/**
 * This class is an activity for the main PopularMovieApp
 */

public class PopularMovieActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PopularMovieFragment();
    }
}
