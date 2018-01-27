package com.cristhianbonilla.moviereview.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cristhianbonilla.moviereview.Fragments.DetallesMovieFragment;
import com.cristhianbonilla.moviereview.Model.Comentario;
import com.cristhianbonilla.moviereview.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cali1 on 14/01/2018.
 */

public class ComentariosAdapter  extends RecyclerView.Adapter<ComentariosAdapter.ViewHolder>{


    private List<Comentario> comentarios;
    private Fragment fragment;

    public ComentariosAdapter(List<Comentario> comentarios, DetallesMovieFragment detailsFragment) {

        this.comentarios = comentarios;
        this.fragment = detailsFragment;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.row_comentarios, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comentario coment = comentarios.get(position);


        holder.nombre.setText(coment.getUsername());
        //calificacion =  calificacion + Float.parseFloat(comentario.getRating());
        holder.calificacion.setRating(Float.parseFloat(coment.getRating()));

        Picasso.with(fragment.getContext()).load(coment.getImg()).resize(200,200).into( holder.img);

        // holder.comentario.setBackgroundResource(fragment.getResources().getColor());
        holder.comentario.setText(coment.getContentComentarios());

    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView nombre, comentario;
        public RatingBar calificacion;
        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            nombre = (TextView) itemView.findViewById(R.id.nombre_friend_fb);
            comentario = (TextView) itemView.findViewById(R.id.contenido_review);
            calificacion = (RatingBar) itemView.findViewById(R.id.estrellas);

        }
    }
}
