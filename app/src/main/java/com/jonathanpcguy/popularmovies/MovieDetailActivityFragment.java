package com.jonathanpcguy.popularmovies;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle bundle = getActivity().getIntent().getBundleExtra(ReturnedMovie.BUNDLE_RETURNED_MOVE);
        ReturnedMovie returnedMovie = new ReturnedMovie(bundle);
        // holder?

        TextView movieTitle = (TextView)rootView.findViewById(R.id.movie_detail_title_text_view);
        movieTitle.setText(returnedMovie.GetMovieTitle());
        TextView releaseDate = (TextView)rootView.findViewById(R.id.movie_detail_release_date);
        releaseDate.setText(returnedMovie.GetReleaseDate());
        TextView userRating = (TextView)rootView.findViewById(R.id.movie_detail_rating);
        userRating.setText(String.valueOf(returnedMovie.GetVoteAverage()));
        TextView synopsis = (TextView)rootView.findViewById(R.id.movie_detail_synopsis);
        synopsis.setText(returnedMovie.GetOverview());
        ImageView poster = (ImageView)rootView.findViewById(R.id.movie_detail_poster_image_view);



        // for some reason the movie poster path starts with an extra /. trim it for the builder

        String targetPosterPath = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(returnedMovie.GetPosterPath().substring(1))
                .build().toString();

        Picasso.with(getContext()).load(targetPosterPath).into(poster);

        return rootView;
    }
}
