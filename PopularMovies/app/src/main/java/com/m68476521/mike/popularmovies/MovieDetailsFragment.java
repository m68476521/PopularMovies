package com.m68476521.mike.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.m68476521.mike.popularmovies.utilities.MovieVideoAdapter;
import com.m68476521.mike.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * This class is the fragment for the movie details clicked
 */

public class MovieDetailsFragment extends Fragment {
    private final static String TAG = "MovieDetails";
    private static final String ARG_MOVIE_ID = "movie_id";
    private static final String ARG_MOVIE_TITLE = "movie_title";
    private static final String ARG_MOVIE_DESC = "movie_desc";
    private static final String ARG_MOVIE_YEAR = "movie_year";
    private static final String ARG_MOVIE_VOTE = "movie_vote";
    private static final String ARG_MOVIE_ORI = "movie_ori";
    private static final String ARG_MOVIE_POSTER = "movie_poster";
    private static final String ARG_MOVIE_POSTER_BACK = "movie_poster_back";

    private ImageButton mFavorite;
    private RecyclerView mRecyclerView;

    private String mId;
    private String mTitle;
    private String mDesc;
    private String mPosterValue;
    private String mYearValue;
    private Double mVoteAverage;
    private String mOri;
    private String mPosterBack;

    private List<MovieVideo> mMovieVideos = new ArrayList<>();
    private MovieReview mMovieReview;
    private MovieLab mMovieLab;
    private Context mContext;

    private PopupWindow mPopupWindow;
    private TextView mMovieReviewDesc;
    private TextView mMovieReviewAuthor;

    public static MovieDetailsFragment newInstance(String id, String title, String desc, String year,
                                                   Double voteAverage, String poster, String ori, String backPoster) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE_ID, id);
        args.putSerializable(ARG_MOVIE_TITLE, title);
        args.putSerializable(ARG_MOVIE_DESC, desc);
        args.putSerializable(ARG_MOVIE_POSTER, poster);
        args.putSerializable(ARG_MOVIE_YEAR, year);
        args.putSerializable(ARG_MOVIE_VOTE, voteAverage);
        args.putSerializable(ARG_MOVIE_ORI, ori);
        args.putSerializable(ARG_MOVIE_POSTER_BACK, backPoster);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getActivity();

        mId = getArguments().getString(ARG_MOVIE_ID);
        mTitle = getArguments().getString(ARG_MOVIE_TITLE);
        mDesc = getArguments().getString(ARG_MOVIE_DESC);
        mPosterValue = getArguments().getString(ARG_MOVIE_POSTER);
        mYearValue = getArguments().getString(ARG_MOVIE_YEAR);
        mVoteAverage = getArguments().getDouble(ARG_MOVIE_VOTE);
        mOri = getArguments().getString(ARG_MOVIE_ORI);
        mPosterBack = getArguments().getString(ARG_MOVIE_POSTER_BACK);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_movie_details, container, false);

        TextView movieTitle = view.findViewById(R.id.title_movie);
        ImageView poster1 = view.findViewById(R.id.movie_poster);
        TextView description = view.findViewById(R.id.description);
        TextView year1 = view.findViewById(R.id.year);
        TextView duration1 = view.findViewById(R.id.duration);
        TextView rating = view.findViewById(R.id.rating);
        mFavorite = view.findViewById(R.id.button_fav);

        String poster = "http://image.tmdb.org/t/p/w185/" + mPosterBack;
        String year = mYearValue.substring(0, 4);
        Double voteAverage = mVoteAverage;

        movieTitle.setText(mTitle);
        description.setText(mDesc);
        year1.setText(year);
        String average = voteAverage.toString() + "/10";
        rating.setText(average);
        duration1.setText(mOri.toUpperCase());

        getVideos(mId);

        Picasso.with(getActivity())
                .load(poster)
                .placeholder(R.mipmap.ic_launcher)
                .into(poster1);

        mRecyclerView = view.findViewById(R.id.rv_videos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mMovieLab = MovieLab.get(getActivity());

        setFavoriteIcon(mId);

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieItem movie = new MovieItem();
                movie.setId(mId);
                movie.setTitle(mTitle);
                movie.setDescription(mDesc);
                movie.setPoster(mPosterValue);
                movie.setReleaseDate(mYearValue);
                movie.setVoteAverage(mVoteAverage);
                movie.setOriginalLanguage(mOri);
                movie.setBackPoster(mPosterBack);
                setFavoriteMovie(movie);
                setFavoriteIcon(mId);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.action_review) {
            getReview(mId);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupReview() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View customView = inflater.inflate(R.layout.popup_review, null);

        mPopupWindow = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true
        );

        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(20);
        }

        ImageButton closeButton = customView.findViewById(R.id.ib_close);
        mMovieReviewDesc = customView.findViewById(R.id.review);
        mMovieReviewAuthor = customView.findViewById(R.id.author);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
        mPopupWindow.setOutsideTouchable(true);
    }

    private void setFavoriteMovie(MovieItem movie) {
        boolean isFav = mMovieLab.getMovie(movie.getId()) != null;
        if (isFav) {
            mMovieLab.deleteMovie(movie);
        } else {
            mMovieLab.addMovie(movie);
        }
    }

    private void setFavoriteIcon(String id) {
        boolean isFav = mMovieLab.getMovie(id) != null;
        if (!isFav) {
            mFavorite.setBackgroundResource(R.drawable.star_128_no);
        } else {
            mFavorite.setBackgroundResource(R.drawable.star_128);
        }
    }

    private void setupAdapter() {
        MovieVideoAdapter movieVideoAdapter = new MovieVideoAdapter(mMovieVideos);
        mRecyclerView.setAdapter(movieVideoAdapter);
    }

    private void updateReview(MovieReview review) {
        mMovieReviewDesc.setText(review.getReview());
        String author = "... by " + review.getAuthor();
        mMovieReviewAuthor.setText(author);
    }

    private void getVideos(String id) {
        URL searchUrl = NetworkUtils.buildUrl("videos", id);
        new MovieDetailsFragment.TheMoviesGetVideos().execute(searchUrl);
    }

    private void getReview(String id) {
        URL theMoviesUrl = NetworkUtils.buildUrl("review", id);
        new MovieDetailsFragment.Reviews().execute(theMoviesUrl);
    }

    private class TheMoviesGetVideos extends AsyncTask<URL, String, List<MovieVideo>> {
        @Override
        protected List<MovieVideo> doInBackground(URL... params) {
            try {
                NetworkUtils networkUtils = new NetworkUtils();
                mMovieVideos = networkUtils.getPopularVideos(mId);
                return mMovieVideos;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieVideos;
        }

        @Override
        protected void onPostExecute(List<MovieVideo> newMovieItems) {
            if (mMovieVideos != null && mMovieVideos.size() > 0) {
                mMovieVideos = newMovieItems;
                setupAdapter();
            }
        }
    }

    private class Reviews extends AsyncTask<URL, String, MovieReview> {
        @Override
        protected MovieReview doInBackground(URL... params) {
            try {
                NetworkUtils networkUtils = new NetworkUtils();

                mMovieReview = networkUtils.getVideoReview(mId);
                return mMovieReview;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieReview;
        }

        @Override
        protected void onPostExecute(MovieReview newMovieReview) {
            if (mMovieReview != null) {
                mMovieReview = newMovieReview;
                if (mMovieReview.getReview() != null) {
                    showPopupReview();
                    updateReview(newMovieReview);
                } else {
                    Toast toast = Toast.makeText(mContext, "This movie hasn't any review", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }
}
