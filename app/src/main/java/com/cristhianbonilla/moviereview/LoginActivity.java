package com.cristhianbonilla.moviereview;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.cristhianbonilla.moviereview.Model.Usuario;
import com.cristhianbonilla.moviereview.References.Reference;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private FirebaseUser usuario;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;
    private Button loginButton2;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        crearHask();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton2 = (Button) findViewById(R.id.btn_login);

        fbLogin();

        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
                //fbLogin();
            }
        });

    }

    private void crearHask() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cristhianbonilla.askloy", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goMainScreen() {

        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }


    private void insertarUsuario(final String idUsuarioFu, final  String email, final String deviceToken){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = database.getReference(Reference.USUARIO);
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
                                            Usuario usuarioFb  = new Usuario(
                                                    me.optString("name"),email,profileImageUrl,idUsuarioFu,"regular",deviceToken);

                                            //   contectReview.setText("");
                                            ref.child(idUsuarioFu).setValue(usuarioFb);
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


    private void fbLogin() {




        mAuth = FirebaseAuth.getInstance();

        fiAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    Log.d("Login",user.getUid());


                    insertarUsuario(user.getUid(), user.getEmail(), deviceToken);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }else{
                    Log.d("Login","Signed Out");
                    Toast.makeText(getApplicationContext(), "Estamos AFUERA ",Toast.LENGTH_LONG).show();

                }
            }
        };
        loginButton.setReadPermissions("email","user_friends");

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handledFacebookAccessToken(loginResult.getAccessToken());



            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.i("error ",error.toString());

            }
        });

    }
    public void handledFacebookAccessToken(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), R.string.fiebase_error_login,Toast.LENGTH_LONG).show();
                }else {

                    GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                            accessToken,
                            //AccessToken.getCurrentAccessToken(),
                            "/me/friends",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    //   Intent intent = new Intent(MainActivity.this,friend.class);
                                    try {
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        usuario = FirebaseAuth.getInstance().getCurrentUser();
                                        //  final DatabaseReference ref = database.getReference(References.FRIENDS+"/"+ usuario.getUid());
                                        // final DatabaseReference refAsk = database.getReference(References.FRIENDS);
                                        final JSONArray rawName = response.getJSONObject().getJSONArray("data");

                                        //     ArrayList<Friend> castList= new ArrayList<Friend>();
                                        final DatabaseReference refUser = database.getReference("usuarios"+"/"+usuario.getUid()+"/"+"username");
                                        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String value = (String) dataSnapshot.getValue();
                                                System.out.println(value);
                                                //  Toast.makeText(getContext(), username.getUsername(),Toast.LENGTH_LONG).show();

                                                for (int i=0; i < rawName.length(); i++) {

                                                    try {
                                                        JSONObject jpersonObj = rawName.getJSONObject(i);

                                                        //  Friend amigos = new Friend(jpersonObj.getString("id"),jpersonObj.getString("name"));
                                                        //ref.setValue(amigos);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        /**   ItemFragment itemFragment = new ItemFragment();
                                         Bundle bundle = new Bundle();
                                         String nameFriends = rawName.toString();
                                         bundle.putString("nameFriends",nameFriends);

                                         itemFragment.setArguments(bundle);
                                         **/
                                        System.out.print(rawName.toString()+"algo por aca");
                                        //  intent.putExtra("jsondata", rawName.toString());
                                        //     startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).executeAsync();
                    goMainScreen();
                }

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fiAuthStateListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fiAuthStateListener);
    }
}
