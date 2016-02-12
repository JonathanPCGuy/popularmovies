package com.jonathanpcguy.popularmovies;

import android.os.Bundle;

/**
 * Created by jonla on 2/8/2016.
 */
public class ReturnedMovie {

    public static final String BUNDLE_RETURNED_MOVE = "EXTRA_MOVIE_BUNDLE";

    private String mMovieTitle;
    private String mPosterPath;
    private int mMovieId;
    private double mVoteAverage;
    private String mReleaseDate;
    private String mOverview;

    public ReturnedMovie(String mMovieTitle, String mPosterPath, int mMovieId, String overview, double voteAverage, String releaseDate) {
        this.mMovieTitle = mMovieTitle;
        this.mPosterPath = mPosterPath;
        this.mMovieId = mMovieId;
        this.mVoteAverage = voteAverage;
        this.mReleaseDate =  releaseDate;
        this.mOverview = overview;
    }

    public ReturnedMovie(Bundle bundle) {
        this.mMovieTitle = bundle.getString("title");
        this.mPosterPath = bundle.getString("poster_path");
        this.mMovieId = bundle.getInt("id");
        this.mVoteAverage = bundle.getDouble("vote_average");
        this.mReleaseDate =  bundle.getString("release_date");
        this.mOverview = bundle.getString("overview");
    }

    public String GetMovieTitle() {
        return mMovieTitle;
    }

    public String GetPosterPath() {

        return mPosterPath;
    }

    public int GetMovieId() {
        return mMovieId;
    }

    public double GetVoteAverage() {
        return mVoteAverage;
    }

    public String GetReleaseDate() {
        return mReleaseDate;
    }

    public String GetOverview() {
        return mOverview;
    }

    public Bundle ToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", GetMovieTitle());
        bundle.putString("poster_path", GetPosterPath());
        bundle.putInt("id", GetMovieId());
        bundle.putDouble("vote_average", GetVoteAverage());
        bundle.putString("release_date", GetReleaseDate());
        bundle.putString("overview", GetOverview());

        return bundle;
    }

}
