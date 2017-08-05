package com.m68476521.mike.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.m68476521.mike.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PopularMovieFragment extends Fragment {
    private final static String TAG = "PopularMovieFragment";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private ArrayList<MovieItem> mMovieItems = new ArrayList<>();
    private ArrayList<MovieItem> mMovieItemsTop = new ArrayList<>();
    private MovieGridAdapter mMovieGridAdapter;
    private GridView mGridView;
    private boolean mSubtitleVisible;
    private boolean showFavModel = false;
    private boolean showTopModel = false;
    private boolean showPopularModel = true;
    private boolean firstToolBarSelection = false;
    private String masterCurrentPage = "";

    private MovieLab mMovieLab;

    private boolean shouldSetAdapterFromSaveInstance = false;
    private ProgressBar progressBar;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mMovieLab = MovieLab.get(getActivity());
        mContext = getContext();

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.d(TAG, "instanced null go to API");
            getTopMovies();
            getPopularMovies();
        } else if (savedInstanceState.containsKey("currentPage")) {
            String currentPage = savedInstanceState.getString("currentPage");
            mMovieItems = savedInstanceState.getParcelableArrayList("movies");
            mMovieItemsTop = savedInstanceState.getParcelableArrayList("moviesTop");
            shouldSetAdapterFromSaveInstance = true;
            switch (currentPage) {
                case "top":
                    masterCurrentPage = "top";
                    showPopularModel = false;
                    showFavModel = false;
                    showTopModel = true;
                    break;
                case "favorites":
                    masterCurrentPage = "favorites";
                    showPopularModel = false;
                    showTopModel = false;
                    showFavModel = true;
                    break;
                case "popular":
                    masterCurrentPage = "popular";
                    showPopularModel = true;
                    showFavModel = false;
                    showTopModel = false;
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_view, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        mGridView = view.findViewById(R.id.flavors_grid);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItem mMovieItem;
                if (showPopularModel) {
                    mMovieItem = mMovieItems.get(position);
                } else if (showTopModel) {
                    mMovieItem = mMovieItemsTop.get(position);
                } else {
                    List<MovieItem> movies = mMovieLab.getMovies();
                    mMovieItem = movies.get(position);
                }

                Log.d(TAG, mMovieItem.getTitle());

                Intent intent = MovieDetailsActivity.newIntent(getContext(), mMovieItem.getId(),
                        mMovieItem.getTitle(), mMovieItem.getDescription(), mMovieItem.getPoster(),
                        mMovieItem.getReleaseDate(), mMovieItem.getVoteAverage(), mMovieItem.getOriginalLanguage(), mMovieItem.getBackPoster());

                Context context = view.getContext();
                context.startActivity(intent);
            }
        });


        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        if (shouldSetAdapterFromSaveInstance) {
            switch (masterCurrentPage) {
                case "top":
                    setupAdapterTop();
                    break;
                case "popular":
                    setupAdapter();
                    break;
                case "favorites":
                    setupAdapterFavorites();
                    break;
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mMovieItems);
        outState.putParcelableArrayList("moviesTop", mMovieItemsTop);

        String currentModel;
        if (showPopularModel) {
            currentModel = "popular";
        } else if (showFavModel) {
            currentModel = "favorites";
        } else {
            currentModel = "top";
        }

        outState.putString("currentPage", currentModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.action_main);
        if (showPopularModel) {
            subtitleItem.setTitle(R.string.movies_popular);
        } else {
            subtitleItem.setTitle(R.string.movies_top);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        mSubtitleVisible = !mSubtitleVisible;

        if (menuItemSelected == R.id.action_fav) {
            setupAdapterFavorites();
            showFavModel = true;
            showPopularModel = false;
            showTopModel = false;
        } else if (menuItemSelected == R.id.action_main) {
            if (item.getTitle().equals("Popular")) {
                item.setTitle(R.string.movies_top);
                if (mMovieItemsTop.size() == 0) {
                    getTopMovies();
                } else {
                    setupAdapterTop();
                }
                showFavModel = false;
                showPopularModel = false;
                showTopModel = true;
            } else {
                item.setTitle(R.string.movies_popular);
                if (mMovieItems.size() == 0) {
                    getPopularMovies();
                } else {
                    setupAdapter();
                }
                showPopularModel = true;
                showFavModel = false;
                showTopModel = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupAdapter() {
        mMovieGridAdapter = new MovieGridAdapter(getActivity(), mMovieItems);
        mGridView.setAdapter(mMovieGridAdapter);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void setupAdapterFavorites() {
        MovieLab movieLab = MovieLab.get(getActivity());
        ArrayList<MovieItem> movies = movieLab.getMovies();
        mMovieGridAdapter = new MovieGridAdapter(getActivity(), movies);
        mGridView.setAdapter(mMovieGridAdapter);
        if (movies.size() == 0) {
            Toast toast = Toast.makeText(mContext, "You haven't added any movie to favorites", Toast.LENGTH_SHORT);
            toast.show();
        }
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void setupAdapterTop() {
        mMovieGridAdapter = new MovieGridAdapter(getActivity(), mMovieItemsTop);
        mGridView.setAdapter(mMovieGridAdapter);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void getPopularMovies() {
        URL searchUrl = NetworkUtils.buildUrl("popular", " ");
        new GetQueryTask().execute(searchUrl);
    }

    private void getTopMovies() {
        URL searchUrl = NetworkUtils.buildUrl("top", " ");
        new GetTopQueryTask().execute(searchUrl);
    }

    private class GetQueryTask extends AsyncTask<URL, Void, ArrayList<MovieItem>> {

        @Override
        protected ArrayList<MovieItem> doInBackground(URL... params) {
            try {
                NetworkUtils networkUtils = new NetworkUtils();
                mMovieItems = networkUtils.getPopularMovies();
                return mMovieItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieItems;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> newMovieItems) {
            if (mMovieItems != null && mMovieItems.size() > 0) {
                mMovieItems = newMovieItems;
                Log.d(TAG, " postExecute");
                setupAdapter();
            } else {
                Toast toast = Toast.makeText(mContext, "Something went wrong, please check your internet connection and try again! ", Toast.LENGTH_SHORT);
                toast.show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class GetTopQueryTask extends AsyncTask<URL, Void, ArrayList<MovieItem>> {
        @Override
        protected ArrayList<MovieItem> doInBackground(URL... params) {
            try {
                NetworkUtils networkUtils = new NetworkUtils();
                mMovieItemsTop = networkUtils.getTopMovies();
                return mMovieItemsTop;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mMovieItemsTop;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> newMovieItems) {
            if (mMovieItemsTop != null && mMovieItemsTop.size() > 0) {
                mMovieItemsTop = newMovieItems;
                Log.d(TAG, " postExecute");
                setupAdapterTop();
            } else {
                Toast toast = Toast.makeText(mContext, "Something went wrong, please check your internet connection and try again! ", Toast.LENGTH_SHORT);
                toast.show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
