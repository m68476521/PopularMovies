package com.m68476521.mike.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.m68476521.mike.popularmovies.database.MovieBaseHelper;
import com.m68476521.mike.popularmovies.database.MovieCursorWrapper;
import com.m68476521.mike.popularmovies.database.MovieDbSchema;

import java.util.ArrayList;

/**
 * This class is for handling Database transactions
 */

public class MovieLab {

    private static MovieLab mCrimeLab;

    private final SQLiteDatabase mDatabase;

    private MovieLab(Context context) {
        Context context1 = context.getApplicationContext();
        mDatabase = new MovieBaseHelper(context1)
                .getWritableDatabase();
    }

    public static MovieLab get(Context context) {
        if (mCrimeLab == null) {
            mCrimeLab = new MovieLab(context);
        }
        return mCrimeLab;
    }

    private static ContentValues getContentValues(MovieItem movie) {
        ContentValues values = new ContentValues();
        values.put(MovieDbSchema.MovieTable.Cols.ID, movie.getId());
        values.put(MovieDbSchema.MovieTable.Cols.TITLE, movie.getTitle());
        values.put(MovieDbSchema.MovieTable.Cols.DESC, movie.getDescription());
        values.put(MovieDbSchema.MovieTable.Cols.POSTER, movie.getPoster());
        values.put(MovieDbSchema.MovieTable.Cols.VOTE, movie.getVoteAverage());
        values.put(MovieDbSchema.MovieTable.Cols.RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieDbSchema.MovieTable.Cols.ORI_LANG, movie.getOriginalLanguage());
        values.put(MovieDbSchema.MovieTable.Cols.POSTER_BACK, movie.getBackPoster());
        return values;
    }

    public ArrayList<MovieItem> getMovies() {
        ArrayList<MovieItem> crimes = new ArrayList<>();

        MovieCursorWrapper cursor = queryMovies(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getMovie());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public MovieItem getMovie(String id) {
        MovieCursorWrapper cursor = queryMovies(
                MovieDbSchema.MovieTable.Cols.ID + " = ?",
                new String[]{id}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getMovie();
        } finally {
            cursor.close();
        }
    }

    private MovieCursorWrapper queryMovies(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MovieDbSchema.MovieTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // group by
                null, // having
                null  // orderBy
        );
        return new MovieCursorWrapper(cursor);
    }

    public void addMovie(MovieItem c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(MovieDbSchema.MovieTable.NAME, null, values);
    }

    public void updateMovie(MovieItem movie) {
        String uuidString = movie.getId();
        ContentValues values = getContentValues(movie);

        mDatabase.update(MovieDbSchema.MovieTable.NAME, values,
                MovieDbSchema.MovieTable.Cols.ID + " = ?",
                new String[]{uuidString});
    }

    public void deleteMovie(MovieItem movie) {
        String uuidString = movie.getId();
        mDatabase.delete(MovieDbSchema.MovieTable.NAME, MovieDbSchema.MovieTable.Cols.ID + " = ?",
                new String[]{uuidString});
    }
}