package com.m68476521.mike.popularmovies.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m68476521.mike.popularmovies.MovieVideo;
import com.m68476521.mike.popularmovies.R;

import java.util.List;

/**
 * This class is an adapter for the movie
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.VideoViewHolder> {

    private static final String TAG = MovieVideoAdapter.class.getSimpleName();

    private final List<MovieVideo> mMovieVideos;

    private Context mContext;

    public MovieVideoAdapter(List<MovieVideo> items) {
        mMovieVideos = items;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachParentImmediately);
        VideoViewHolder viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovieVideos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mIconPlay;
        MovieVideo mMovieVideo;
        final TextView mTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mIconPlay = itemView.findViewById(R.id.video_item);
            mTitle = itemView.findViewById(R.id.trailer_id);
        }

        void bind(int listIndex) {
            mMovieVideo = mMovieVideos.get(listIndex);
            mTitle.setText("Trailer " + listIndex + " " + mMovieVideo.getName());
            mIconPlay.setImageResource(R.drawable.media_play);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, mMovieVideo.getName());

            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + mMovieVideo.getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + mMovieVideo.getKey()));
            try {
                mContext.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                mContext.startActivity(webIntent);
            }
        }
    }
}
