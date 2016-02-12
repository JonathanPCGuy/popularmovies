package com.jonathanpcguy.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    private class FetchMoviesTask  extends AsyncTask<Void, Void, ReturnedMovie[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String API_KEY = getString(R.string.the_movie_db_api_key);
        private final String API_BASE_ENDPOINT = getString(R.string.the_movie_api_endpoint);


        // i think also this class will eventually be moved to a worker if i recall correctly

        /**
         * Processes the data from the movie api. For now, only tested against a fixed api query; will be updated later
         * @param jsonString
         * @return parsed movie data for display
         */
        private ReturnedMovie[] parseMovieData(String jsonString)
            throws JSONException {

            ArrayList<ReturnedMovie> returnData = new ArrayList<ReturnedMovie>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray movieArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject currentObject = movieArray.getJSONObject(i);
                // is there a more elegant way to do this?
                returnData.add(new ReturnedMovie(currentObject.getString("title"),
                        currentObject.getString("poster_path"),
                        currentObject.getInt("id"),
                        currentObject.getString("overview"),
                        currentObject.getDouble("vote_average"),
                        currentObject.getString("release_date")));
            }

            return returnData.toArray(new ReturnedMovie[returnData.size()]);
        }

        @Override
        protected ReturnedMovie[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String returnData = null;
            ReturnedMovie[] returnedMovies = null;

            try {
                // todo: make modular when query and sorting options changes
                URL url = new URL(Uri.parse(API_BASE_ENDPOINT).buildUpon()
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("sort_by","popularity.desc")
                        .appendQueryParameter("api_key", API_KEY)
                        .build()
                        .toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    returnData = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    returnData = null;
                }
                returnData = buffer.toString();
                returnedMovies = parseMovieData(returnData);


            }
            catch(IOException | JSONException e) {
                Log.e(LOG_TAG, "Error", e);
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return returnedMovies;
        }

        @Override
        protected void onPostExecute(ReturnedMovie[] returnedMovies) {
            if(returnedMovies != null) {
                mMovieListAdapter.clear();
                ArrayList<ReturnedMovie> returnData = new ArrayList<ReturnedMovie>(Arrays.asList(returnedMovies));

                for(ReturnedMovie s :returnData) {
                    mMovieListAdapter.add(s);
                }
            }
            super.onPostExecute(returnedMovies);
        }
    }

    // todo: figure out how to update multiple fields with the adapter (i've done this before but i've forgotten)

    MovieListAdapter mMovieListAdapter;

    private void GetMovieData() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        mMovieListAdapter = new MovieListAdapter(getActivity(), R.layout.list_item_movie, R.id.list_item_movie_text_view);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(mMovieListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReturnedMovie returnedMovie = mMovieListAdapter.getItem(position);

                // todo: study how to shove a map
                // later on we would just hit the local db;
                Intent detailedIntent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra(ReturnedMovie.BUNDLE_RETURNED_MOVE, returnedMovie.ToBundle());
                startActivity(detailedIntent);

            }
        });

        GetMovieData();
        return rootView;
    }
}
