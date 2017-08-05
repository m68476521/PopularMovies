package com.m68476521.mike.popularmovies.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.m68476521.mike.popularmovies.MovieItem;

/**
 * This class is a wrapper for MovieItem
 */

public class MovieCursorWrapper extends CursorWrapper {
    public MovieCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public MovieItem getMovie() {
        String id = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.ID));
        String title = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.TITLE));
        String date = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.RELEASE_DATE));
        String origin = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.ORI_LANG));
        String poster = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.POSTER));
        String desc = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.DESC));
        String vote = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.VOTE));
        String posterBack = getString(getColumnIndex(MovieDbSchema.MovieTable.Cols.POSTER_BACK));

        MovieItem movieItem = new MovieItem();
        movieItem.setTitle(title);
        movieItem.setDescription(desc);
        movieItem.setPoster(poster);
        movieItem.setId(id);
        movieItem.setReleaseDate(date);
        movieItem.setVoteCount(vote);
        movieItem.setOriginalLanguage(origin);
        movieItem.setBackPoster(posterBack);
        return movieItem;
    }
}
