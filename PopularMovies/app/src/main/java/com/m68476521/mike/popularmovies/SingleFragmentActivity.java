package com.m68476521.mike.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the container for the fragments
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private final List<MovieItem> myFavoritesMovies = new ArrayList<>();

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.fragment_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public void addToFav(MovieItem myFavMovie) {
        myFavoritesMovies.add(myFavMovie);
    }

    public List<MovieItem> getFavMovies() {
        return myFavoritesMovies;
    }

}
