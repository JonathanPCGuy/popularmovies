package com.jonathanpcguy.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jonla on 2/9/2016.
 * Tutorial used: http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/#Custom_ArrayAdapter_class
 */
public class MovieListAdapter extends ArrayAdapter<ReturnedMovie> {

    private Context context;
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    public MovieListAdapter(Context context, int resource, ReturnedMovie[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public MovieListAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public MovieListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
    }

    // viewholder class?
    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        ReturnedMovie returnedMovie = getItem(position);

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_movie, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.list_item_movie_text_view);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_item_poster_image_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // for some reason the movie poster path starts with an extra /. trim it for the builder
        String targetPosterPath = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(returnedMovie.GetPosterPath().substring(1))
                .build().toString();
        Log.d(LOG_TAG, targetPosterPath);

        holder.textView.setText(returnedMovie.GetMovieTitle());
        Picasso.with(context).load(targetPosterPath).into(holder.imageView);

        return convertView;
    }
}
