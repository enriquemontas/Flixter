package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.MovieDetailsActivity;
import com.example.flixter.R;
import com.example.flixter.databinding.ItemMovieBinding;
import com.example.flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;
    ItemMovieBinding binding;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder();
    }

    // Involves populating data into the item through a holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the movie at passed in position
        Movie movie = movies.get(position);
        // bind the movie data into VH
        holder.bind(movie);

    }

    // returns count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder() {
            super(binding.getRoot());
            tvTitle = binding.tvTitle;
            tvOverview = binding.tvOverview;
            ivPoster = binding.ivPoster;
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // check phone oritentation and route to appropriate image
            boolean portrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            String imageUrl = portrait ? movie.getPosterPath() : movie.getBackdropPath();
            int placeholder = portrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;

            final int radius = 30; // corner radius, higher value = more rounded
            final int margin = 10; // crop margin, set to 0 for corners with no crop


            Glide.with(context).load(imageUrl)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
        }

        @Override
        public void onClick(View view) {
            // get position and ensure its valid
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION){
                // get movie at that position
                Movie movie = movies.get(position);
                // create intent
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // pass movie as an extra serialized
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show activity
                context.startActivity(intent);

            }
        }
    }
}
