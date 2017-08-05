package com.m68476521.mike.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is the base for SQLite
 */

public class MovieBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "MovieBase.db";

    public MovieBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MovieDbSchema.MovieTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                MovieDbSchema.MovieTable.Cols.ID + ", " +
                MovieDbSchema.MovieTable.Cols.TITLE + ", " +
                MovieDbSchema.MovieTable.Cols.DESC + ", " +
                MovieDbSchema.MovieTable.Cols.POSTER + ", " +
                MovieDbSchema.MovieTable.Cols.POSTER_BACK + ", " +
                MovieDbSchema.MovieTable.Cols.RELEASE_DATE + ", " +
                MovieDbSchema.MovieTable.Cols.VOTE + ", " +
                MovieDbSchema.MovieTable.Cols.ORI_LANG +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
