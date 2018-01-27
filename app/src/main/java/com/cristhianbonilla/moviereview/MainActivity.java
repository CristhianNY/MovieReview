package com.cristhianbonilla.moviereview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.cristhianbonilla.moviereview.Fragments.AddMovie;
import com.cristhianbonilla.moviereview.Fragments.DetallesMovieFragment;
import com.cristhianbonilla.moviereview.Fragments.HomeFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imagenDePerfil;
    private FirebaseUser usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        setActionBarTitle("");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMovie addMovie = new AddMovie();

                getSupportFragmentManager().beginTransaction().replace(R.id.container, addMovie)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
            }
        });

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        if(usuario.getUid().equals("YWwdbZNDQASy88ymTGWxNcICYJs1")){

            fab.show();
        }else{
            fab.hide();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       drawer.addDrawerListener(toggle);
       toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();

        String categoria = "popular";
        bundle.putString("categoria",categoria);

        View hView =  navigationView.getHeaderView(0);
        imagenDePerfil = (ImageView) hView.findViewById(R.id.imagen_perfil);
        if (AccessToken.getCurrentAccessToken() != null) {

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject me, GraphResponse response) {

                            if (AccessToken.getCurrentAccessToken() != null) {

                                if (me != null) {


                                    String profileImageUrl = ImageRequest.getProfilePictureUri(me.optString("id"), 100, 100).toString();

                                    Picasso.with(getApplicationContext()).load(profileImageUrl).resize(200,200).into(imagenDePerfil);


                                }
                            }
                        }
                    });
            GraphRequest.executeBatchAsync(request);
        }

        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.accion) {
            Bundle bundle = new Bundle();
            setActionBarTitle("");
            String categoria = "Acción";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
            // Handle the camera action
        } else if (id == R.id.aventura) {

            Bundle bundle = new Bundle();

            String categoria = "Aventura";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        } else if (id == R.id.comedia) {
            Bundle bundle = new Bundle();

            String categoria = "comedia";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
        } else if (id == R.id.accion) {

        }else if (id == R.id.accion) {
            Bundle bundle = new Bundle();

            String categoria = "accion";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        } else if (id == R.id.biografia) {
            Bundle bundle = new Bundle();

            String categoria = "biografia";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        } else if (id == R.id.drama) {
            Bundle bundle = new Bundle();

            String categoria = "drama";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        } else if (id == R.id.terror) {
            Bundle bundle = new Bundle();

            String categoria = "terror";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        } else if (id == R.id.animadas) {
            Bundle bundle = new Bundle();

            String categoria = "animadas";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        } else if (id == R.id.documental) {
            Bundle bundle = new Bundle();

            String categoria = "documental";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();


        }else if (id == R.id.popular) {
            Bundle bundle = new Bundle();

            String categoria = "popular";
            bundle.putString("categoria",categoria);
            HomeFragment homeFragment = new HomeFragment();

            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();



        }else if (id == R.id.salir) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Estas seguro que quieres salir?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "Saliendo ",Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();



        }else if (id == R.id.compartir) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola Te invito a instalar esta aplicación para conocer gente cerca https://play.google.com/store/apps/details?id=com.optimusfly.cali1.mapa&hl=en.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);



        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
