package com.m68476521.mike.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * This class is a bean for the movies
 */

public class MovieItem implements Parcelable {

    public final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }

    };
    private String mVoteCount;
    private String mId;
    private UUID myId;
    private boolean mVideo;
    private double mVoteAverage;
    private String mTitle;
    private String mDescription;
    private String mPoster;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private String mBackPoster;
    private boolean mAdult;
    private String mReleaseDate;

    public MovieItem(String id, String title, String description) {
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
    }

    private MovieItem(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
    }

    public MovieItem() {
        this(UUID.randomUUID());
    }

    private MovieItem(UUID id) {
        myId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public UUID getMyId() {
        return myId;
    }

    public void setMyId(UUID myId) {
        this.myId = myId;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean video) {
        mVideo = video;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getBackPoster() {
        return mBackPoster;
    }

    public void setBackPoster(String backPoster) {
        mBackPoster = backPoster;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean adult) {
        mAdult = adult;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getmVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(String voteCount) {
        this.mVoteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mDescription);
        parcel.writeString(mPoster);
        parcel.writeString(mBackPoster);
        parcel.writeString(mOriginalLanguage);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mReleaseDate);
    }
}

