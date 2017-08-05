package com.m68476521.mike.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class in an adapter for the gridView in PopularMoviesFragment
 */

public class MovieGridAdapter extends ArrayAdapter<MovieItem> {

    private static final String TAG = MovieGridAdapter.class.getSimpleName();

    private final Context mContext;

    public MovieGridAdapter(Activity context, ArrayList<MovieItem> androidFlavors) {
        super(context, 0, androidFlavors);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieItem movieItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }

        ImageView iconView = convertView.findViewById(R.id.movie_item);

        String poster = "http://image.tmdb.org/t/p/w185/" + movieItem.getPoster();
        Log.d(TAG, poster);

        Picasso.with(mContext)
                .load(poster)
                .placeholder(R.mipmap.ic_launcher)
                .into(iconView);

        return convertView;
    }
}
