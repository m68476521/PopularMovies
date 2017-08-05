package com.m68476521.mike.popularmovies;

/**
 * This class is a bean for review, it is related to the movie
 */

public class MovieReview {
    private String mAuthor;
    private String mReview;
    private String mId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        this.mReview = review;
    }
}
