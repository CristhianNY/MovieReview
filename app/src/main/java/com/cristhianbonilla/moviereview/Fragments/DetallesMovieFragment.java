package com.cristhianbonilla.moviereview.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cristhianbonilla.moviereview.Adapters.ComentariosAdapter;
import com.cristhianbonilla.moviereview.Model.Comentario;
import com.cristhianbonilla.moviereview.Model.Movie;
import com.cristhianbonilla.moviereview.R;
import com.cristhianbonilla.moviereview.References.Reference;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetallesMovieFragment extends DialogFragment {


    private String key,nombre,descripcion;
    private Button btnCalificar,insertComentario;
    private RecyclerView recyclerViewComentarios;
    private List<Comentario> comentarios;
    private RatingBar rb, ratingBarEdit,ratingBarEdit2;
    private EditText contectReview,contenidoreview;
    private Float calificacion = Float.parseFloat("0");
    private ComentariosAdapter adapterComentarios;
    private ImageView imagenMovie;
    private FirebaseUser usuario;
    private TextView contentDescription;
    private ImageView btnYoutube;
    private int contador2 = 0;
    public DetallesMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v = inflater.inflate(R.layout.fragment_detalles_movie, container, false);


         btnCalificar = (Button) v.findViewById(R.id.btn_calificar);
         btnYoutube = (ImageView) v.findViewById(R.id.btn_youtube);
         contentDescription = (TextView) v.findViewById(R.id.content_description);
         recyclerViewComentarios = (RecyclerView) v.findViewById(R.id.recycler_comentarios);
         imagenMovie = (ImageView) v.findViewById(R.id.imagenMovie);
           rb = (RatingBar) v.findViewById(R.id.rating);

        contenidoreview = (EditText) v.findViewById(R.id.contenido_review);

        recyclerViewComentarios.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        recyclerViewComentarios.setLayoutManager(mManager);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

         key = this.getArguments().getString("key");
        cargarReviews(key);
         comentarios = new ArrayList<>();
         btnCalificar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {


              final Dialog dialog = new Dialog(getContext());

              dialog.setContentView(R.layout.new_comentario);
              contenidoreview = (EditText) dialog.findViewById(R.id.content_review);
              insertComentario = (Button) dialog.findViewById(R.id.insertComentario);
              ratingBarEdit2 = (RatingBar) dialog.findViewById(R.id.idRating);
              insertComentario.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if(contenidoreview.getText().length() != 0 && ratingBarEdit2.getRating()!=0){
                          insertarReview(key, contenidoreview.getText().toString(),ratingBarEdit2.getRating());

                          dialog.dismiss();

                      }else{

                          if(contenidoreview.getText().length() <= 0){
                              contenidoreview.setError("Por favor escribe un comentario");

                          }

                          if(ratingBarEdit2.getRating() <= 0){

                              TextView errorEstrellas = (TextView) dialog.findViewById(R.id.error_estrellas);
                              errorEstrellas.setError("Selecciona una calificacion por favor ");

                          }
                      }

                  }
              });
              dialog.show();

          }

      });

         cargarInformacion(key);

      return v;
    }

    private void cargarReviews(String key) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(Reference.COMENTARIOS);





        ref.child(key).orderByKey().limitToLast(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                comentarios.removeAll(comentarios);

                for (DataSnapshot snapshop:
                        dataSnapshot.getChildren()
                        ) {

                    contador2 = contador2 +1;
                    Comentario comentario = snapshop.getValue(Comentario.class);

                    //        comentario.getRating();
                    calificacion =  calificacion + Float.parseFloat(comentario.getRating());

                    comentarios.add(comentario);
                    adapterComentarios = new ComentariosAdapter(comentarios,DetallesMovieFragment.this);
                    recyclerViewComentarios.setAdapter(adapterComentarios);

                    //   adapter.notifyDataSetChanged();


                    rb.setRating(calificacion/contador2);

                    recyclerViewComentarios.smoothScrollToPosition(adapterComentarios.getItemCount());
                    adapterComentarios.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void insertarReview(final String key, final String contenido, final Float calificacion) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = database.getReference(Reference.COMENTARIOS + "/" + key);
        adapterComentarios = new ComentariosAdapter(comentarios,this);
        Toast.makeText(getContext(),key ,Toast.LENGTH_LONG).show();

        recyclerViewComentarios.setAdapter(adapterComentarios);
        if (AccessToken.getCurrentAccessToken() != null) {

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject me, GraphResponse response) {

                            if (AccessToken.getCurrentAccessToken() != null) {

                                if (me != null) {

                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String profileImageUrl = ImageRequest.getProfilePictureUri(me.optString("id"), 500, 500).toString();
                                            // Log.i(LOG_TAG, profileImageUrl);
                                            String comentariokey = ref.push().getKey();

                                            String valorCalificacion = Float.toString(calificacion);

                                            Comentario comment = new Comentario(profileImageUrl,
                                                    me.optString("name"), new Date().toString(),comentariokey,usuario.getUid(),contenido,key,valorCalificacion);

                                            //   contectReview.setText("");
                                            ref.child(usuario.getUid()).setValue(comment);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });





                                }
                            }
                        }
                    });
            GraphRequest.executeBatchAsync(request);
        }

    }
    private void cargarInformacion(String key) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(Reference.MOVIES);
        ref.orderByChild("key").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()
                        ) {

                    final Movie infoMovie = snapshot.getValue(Movie.class);
                    Picasso.with(getActivity()).load(R.drawable.youtube_boton).resize(50,50).into(btnYoutube);
                   btnYoutube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Boolean validaUrl =    URLUtil.isValidUrl(infoMovie.getTrailer());

                            if(validaUrl == true){
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(infoMovie.getTrailer())));
                            }else{

                                Toast.makeText(getActivity(),"Error en la url del video",Toast.LENGTH_LONG).show();
                            }



                        }
                    });

                infoMovie.getCalificación();
                infoMovie.getCategoria();
                infoMovie.getTema();
                infoMovie.getImagen();
                infoMovie.getTrailer();
                contentDescription.setText(infoMovie.getTema());

                    calificacion =  calificacion + Float.parseFloat(infoMovie.getCalificación());

                    Picasso.with(getActivity()).load(infoMovie.getImagen()).resize(720,1080).into(imagenMovie);






                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=ya8SuUknLhM")));
    }

}
