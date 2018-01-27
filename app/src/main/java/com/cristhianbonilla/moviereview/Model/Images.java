package com.cristhianbonilla.moviereview.Model;

import android.net.Uri;

/**
 * Created by cali1 on 11/01/2018.
 */

public class Images {


    //  private Bitmap imagen;
    private Uri imgUri;
    private String key;
    private String keyGroup;

    public Images(){

        super();
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public Images(Uri imgUri, String key, String keyGroup) {
        this.imgUri = imgUri;
        this.key = key;
        this.keyGroup = keyGroup;
    }

    public String getKeyGroup() {
        return keyGroup;
    }

    public void setKeyGroup(String keyGroup) {
        this.keyGroup = keyGroup;
    }
    /**public Bitmap getImagen() {
     return imagen;
     }

     public void setImagen(Bitmap imagen) {
     this.imagen = imagen;
     }**/
}
