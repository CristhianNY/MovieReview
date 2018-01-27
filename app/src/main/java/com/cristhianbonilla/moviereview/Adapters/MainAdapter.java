package com.cristhianbonilla.moviereview.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.cristhianbonilla.moviereview.Fragments.DetallesMovieFragment;
import com.cristhianbonilla.moviereview.Fragments.HomeFragment;
import com.cristhianbonilla.moviereview.Model.Movie;
import com.cristhianbonilla.moviereview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cali1 on 9/01/2018.
 */

public class MainAdapter extends  RecyclerView.Adapter<MainAdapter.MoviesViewHolder> {

    private List<Movie> movies;
    private Fragment fragment;
    private int contdor = 0;


    public MainAdapter(List<Movie> movies, HomeFragment homeFragment) {

        this.movies = movies;
        this.fragment = homeFragment;

    }
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie,parent,false);
        MoviesViewHolder holder = new MoviesViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        final Movie movie = movies.get(position);
       /** StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setFullSpan(true);
        holder.itemView.setLayoutParams(layoutParams);**/

        movie.getImagen();
        Picasso.with(this.fragment.getContext()).load(movie.getImagen()).resize(320,880).into(holder.imagenMovie);
        holder.calificación.setRating(4);

        holder.imagenMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DetallesMovieFragment detallesMovieFragment  = new DetallesMovieFragment();

                Bundle bundle = new Bundle();
                String key = movie.getKey();

                bundle.putString("key",key);
                detallesMovieFragment.setArguments(bundle);
                detallesMovieFragment.show(fragment.getFragmentManager(),"detalles");
            }
        });
    }

    public void setFilter(ArrayList<Movie> newList) {

        movies = new ArrayList<>();

        movies.addAll(newList);
        notifyDataSetChanged();



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagenMovie;

        private RatingBar calificación;
        public MoviesViewHolder(View itemView) {
            super(itemView);
            imagenMovie = (ImageView) itemView.findViewById(R.id.coverImageView);
            calificación = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }
}
