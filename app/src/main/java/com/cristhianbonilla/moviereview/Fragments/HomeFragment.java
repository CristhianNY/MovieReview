package com.cristhianbonilla.moviereview.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cristhianbonilla.moviereview.Adapters.MainAdapter;
import com.cristhianbonilla.moviereview.Model.Movie;
import com.cristhianbonilla.moviereview.R;
import com.cristhianbonilla.moviereview.References.Reference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    private List<Movie> movies;
    private String categoria;
    private  int contador =0;
    private String key;
    private MainAdapter muviesAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v =  inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.list_movies);


        categoria = this.getArguments().getString("categoria");

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);

        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
     /**   layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int mod = position % 6;

                if(position == 0 || position == 1)
                    return 2;
                else if(position < 6)
                    return 1;
                else if(mod == 0 || mod == 1)
                    return 2;
                else
                    return 1;
            }
        });**/


        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setHasFixedSize(true);
        movies = new ArrayList<>();

        cargarMovies(categoria);


        return v;


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void cargarMovies(String categoria) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        final DatabaseReference ref = database.getReference(Reference.MOVIES);
        muviesAdapter = new MainAdapter(movies,this);

        recyclerView.setAdapter(muviesAdapter);

        ref.orderByChild("categoria").equalTo(categoria).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                movies.removeAll(movies);

                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()
                        ) {
                    // System.out.print(snapshot.getValue());


                    contador++;
                    Movie movie = snapshot.getValue(Movie.class);
                    if(contador == 1){
                        System.out.print(snapshot.getKey());
                        key = snapshot.getKey();

                    }

                    movies.add(movie);

                }
                muviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        ArrayList<Movie> newList = new ArrayList<>();

        for (Movie movie : movies){
            String titulo = movie.getTitulo().toLowerCase();

            if(titulo.contains(newText)){
                newList.add(movie);
            }

            muviesAdapter.setFilter(newList);

        }



        return true;
    }
}
