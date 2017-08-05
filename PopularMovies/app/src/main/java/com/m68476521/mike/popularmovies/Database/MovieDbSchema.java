package com.m68476521.mike.popularmovies.database;

/**
 * =This class define the schema for the Database
 */

public class MovieDbSchema {
    public static final class MovieTable {
        public static final String NAME = "movies";

        public static final class Cols {
            public static final String ID = "uuid";
            public static final String TITLE = "title";
            public static final String DESC = "desc";
            public static final String POPULARITY = "popularity";
            public static final String POSTER = "poster";
            public static final String ORI_LANG = "ori_lang";
            public static final String ORI_TITLE = "ori_title";
            public static final String POSTER_BACK = "poster_back";
            public static final String VOTE = "vote";
            public static final String ADULT = "adult";
            public static final String RELEASE_DATE = "release_date";
        }
    }
}
