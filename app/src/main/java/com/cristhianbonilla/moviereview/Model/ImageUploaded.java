package com.cristhianbonilla.moviereview.Model;

/**
 * Created by cali1 on 10/01/2018.
 */


public class ImageUploaded {


    public String url;
    private String key;
    private String keyGroup;


    public ImageUploaded(){

    }

    public String getKeyGroup() {
        return keyGroup;
    }

    public void setKeyGroup(String keyGroup) {
        this.keyGroup = keyGroup;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;

    }

    public ImageUploaded( String url, String key, String keyGroup) {

        this.url = url;
        this.key = key;
        this.keyGroup = keyGroup;
    }
}
