package com.cristhianbonilla.moviereview.Fragments;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cristhianbonilla.moviereview.Model.ImageUploaded;
import com.cristhianbonilla.moviereview.Model.Images;
import com.cristhianbonilla.moviereview.Model.Movie;
import com.cristhianbonilla.moviereview.R;
import com.cristhianbonilla.moviereview.References.Reference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMovie extends Fragment {


    private EditText nombre,linkYoutube;
    private EditText descricion;
    private DatePicker fecha;
    private Spinner categoria;
    private ImageButton btnCamera;
    private DatabaseReference mDatabaseRef;
    private Button enviar;
    private ImageView imagenMovie;
    private TextView errorCategoria;
    public static final  int REQUEST_CODE = 1234;
    private ImageView imageView;
    private Uri imgUri;
    private FirebaseUser usuario;
    private List<Movie> movies ;
    private List<Images> imagenes ;
    private DatabaseReference refMovie;
    public static final  String PELICULA = "pelicula";
    private StorageReference mStorageRef;
    public static final  String FB_STORAGE_PATH = "image/";
    public static final  String FB_DATABASE_PATH = "image";
    private int contador=0;
    private List<Images> imagenesAgregadas ;
    private List<ImageUploaded> imgs;
    Movie movie ;


    public AddMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_add_movie, container, false);


       imagenMovie = (ImageView) v.findViewById(R.id.foto_movie);
       nombre = (EditText) v.findViewById(R.id.input_name);
       linkYoutube = (EditText) v.findViewById(R.id.input_youtube);
       descricion = (EditText) v.findViewById(R.id.input_decripcion);
       enviar = (Button) v.findViewById(R.id.enviar);
       btnCamera = (ImageButton) v.findViewById(R.id.btn_camera);
       categoria = (Spinner) v.findViewById(R.id.list_category);

       errorCategoria = (TextView) v.findViewById(R.id.error_categoria);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        descricion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;

            }
        });


        movies = new ArrayList<>();

        imagenes = new ArrayList<>();
        imagenesAgregadas = new ArrayList<>();

        imgs = new ArrayList<>();
        imageView = (ImageView) v.findViewById(R.id.foto_movie);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    btnUpload_Click(view);
                }

            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBrowse_click(v);
            }
        });

        validate();

       return v;
    }

    private void btnBrowse_click(View v) {

        Intent intent = new Intent();
        intent.setType("image/*");


        intent.setAction(intent.ACTION_GET_CONTENT);

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uriWhereToStore);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent.createChooser(intent,"select image"),REQUEST_CODE);
        } catch (ActivityNotFoundException e) {}



    }

    private boolean validate(){
        boolean valid = true;
        if(nombre.getText().length()== 0){


            valid = false;
            nombre.setError("Por favor ingresa un nombre");
        }

        if(linkYoutube.getText().length()== 0){

            valid = false;
            linkYoutube.setError("Por favor ingresa un nombre");
        }

        if(nombre.getText().length()== 0){


            valid = false;
            nombre.setError("Por favor ingresa un nombre");
        }
        if(categoria.getSelectedItem().toString() == "Seleccione la categoria"){


            errorCategoria.setError("Por Favor Seleccione una Categoria");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imgUri = data.getData();


        }
        Picasso.with(getActivity()).load(imgUri).resize(300,300).into(imagenMovie);

        Images img = new Images(imgUri,"123","431");
        imagenesAgregadas.add(img);
        imagenes.add(img);


    }

    public void btnUpload_Click(View v) {

        if (imagenes.size() != 0) {




            if(!validate()){
                Toast.makeText(getActivity().getApplicationContext(),"Por favor conrrige los siguientes campos ", Toast.LENGTH_LONG).show();

            }else{


                    subirTodasLasFotos();



            }



            //get the storage reference


        }
        else{

            Toast.makeText(getContext(),"Por favor Seleccione una imagen", Toast.LENGTH_LONG).show();

        }

    }

    public String getImageExt(Uri uri){

        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void subirTodasLasFotos() {
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        final ProgressDialog dialog = new ProgressDialog(getContext());

        dialog.setTitle("Subiendo Pelicula");
        dialog.show();

        refMovie = FirebaseDatabase.getInstance().getReference(PELICULA);

        final String ketMovie =  refMovie.push().getKey();

        for (Images img : imagenesAgregadas) {


            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(img.getImgUri()));

            final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(Reference.CLAVEMOVIE + "/" + usuario.getUid());


            ref.putFile(img.getImgUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH+"/"+ketMovie);


                    contador = contador+1;
                    if(contador == 1){


                     movie = new Movie(nombre.getText().toString(),categoria.getSelectedItem().toString(),"0",descricion.getText().toString(),linkYoutube.getText().toString(),taskSnapshot.getDownloadUrl().toString(),ketMovie);


                        refMovie.child(ketMovie).setValue(movie);
                        ref2.setValue(ketMovie);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    dialog.setMessage("uploaded" + (int) progress + "%");
                    if(progress == 100){
                        dialog.dismiss();


                    }
                }
            });
        }
    }

}
