package com.m68476521.mike.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * This class is an activity to launch MovieDetailsFragment
 */

public class MovieDetailsActivity extends SingleFragmentActivity {

    private static final String ARG_MOVIE_ID = "movie_id";
    private static final String ARG_MOVIE_TITLE = "movie_title";
    private static final String ARG_MOVIE_DESC = "movie_desc";
    private static final String ARG_MOVIE_YEAR = "movie_year";
    private static final String ARG_MOVIE_VOTE = "movie_vote";
    private static final String ARG_MOVIE_ORI = "movie_ori";
    private static final String ARG_MOVIE_POSTER = "movie_poster";
    private static final String ARG_MOVIE_POSTER_BACK = "movie_poster_back";

    public static Intent newIntent(Context packageContext, String id, String title, String desc, String poster,
                                   String year, Double voteAverage, String ori, String backPoster) {
        Intent intent = new Intent(packageContext, MovieDetailsActivity.class);
        intent.putExtra(ARG_MOVIE_ID, id);
        intent.putExtra(ARG_MOVIE_TITLE, title);
        intent.putExtra(ARG_MOVIE_DESC, desc);
        intent.putExtra(ARG_MOVIE_POSTER, poster);
        intent.putExtra(ARG_MOVIE_YEAR, year);
        intent.putExtra(ARG_MOVIE_VOTE, voteAverage);
        intent.putExtra(ARG_MOVIE_ORI, ori);
        intent.putExtra(ARG_MOVIE_POSTER_BACK, backPoster);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String id = getIntent().getStringExtra(ARG_MOVIE_ID);
        String title = getIntent().getStringExtra(ARG_MOVIE_TITLE);
        String desc = getIntent().getStringExtra(ARG_MOVIE_DESC);
        String year = getIntent().getStringExtra(ARG_MOVIE_YEAR);
        Double vote = getIntent().getDoubleExtra(ARG_MOVIE_VOTE, 0.00);
        String poster = getIntent().getStringExtra(ARG_MOVIE_POSTER);
        String ori = getIntent().getStringExtra(ARG_MOVIE_ORI);
        String backPoster = getIntent().getStringExtra(ARG_MOVIE_POSTER_BACK);
        return MovieDetailsFragment.newInstance(id, title, desc, year, vote, poster, ori, backPoster);
    }
}
