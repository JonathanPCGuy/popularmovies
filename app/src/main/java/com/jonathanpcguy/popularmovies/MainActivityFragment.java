package com.jonathanpcguy.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    private class FetchMoviesTask  extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String API_KEY = getString(R.string.the_movie_db_api_key);
        private final String API_BASE_ENDPOINT = getString(R.string.the_movie_api_endpoint);


        // i think also this class will eventually be moved to a worker if i recall correctly

        /**
         * Processes the data from the movie api. For now, only tested against a fixed api query; will be updated later
         * @param jsonString
         * @return parsed movie data for display
         */
        private String[] parseMovieData(String jsonString)
            throws JSONException {

            ArrayList<String> returnData = new ArrayList<String>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray movieArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < movieArray.length(); i++) {
                returnData.add(movieArray.getJSONObject(i).getString("title"));
            }

            return returnData.toArray(new String[returnData.size()]);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String returnData = null;
            String[] returnedMovies = null;

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
        protected void onPostExecute(String[] strings) {
            if(strings != null) {
                mArrayAdapter.clear();
                ArrayList<String> returnData = new ArrayList<String>(Arrays.asList(strings));

                for(String s :returnData) {
                    mArrayAdapter.add(s);
                }
            }
            super.onPostExecute(strings);
        }
    }

    ArrayAdapter<String> mArrayAdapter;

    private void GetMovieData() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        mArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_movie, R.id.list_item_movie_item);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(mArrayAdapter);
        GetMovieData();
        return rootView;
    }
}
