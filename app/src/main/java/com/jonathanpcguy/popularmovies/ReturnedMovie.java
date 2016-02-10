package com.jonathanpcguy.popularmovies;

/**
 * Created by jonla on 2/8/2016.
 */
public class ReturnedMovie {

    private String mMovieTitle;
    private String mPosterPath;
    private int mMovieId;

    public ReturnedMovie(String mMovieTitle, String mPosterPath, int mMovieId) {
        this.mMovieTitle = mMovieTitle;
        this.mPosterPath = mPosterPath;
        this.mMovieId = mMovieId;
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
}
