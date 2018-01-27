package com.cristhianbonilla.moviereview.Model;

import android.net.Uri;

/**
 * Created by cali1 on 9/01/2018.
 */

public class Movie {


    private String titulo;
    private String categoria;
    private String calificación;
    private String tema;
    private String trailer;
    private String imagen;
    private String key;


    public Movie (){

    }

    public String getImagen() {
        return imagen;
    }

    public Movie(String titulo, String categoria, String calificación, String tema, String trailer, String imagen,String key) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.calificación = calificación;
        this.tema = tema;
        this.trailer = trailer;
        this.imagen = imagen;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCalificación() {
        return calificación;
    }

    public void setCalificación(String calificación) {
        this.calificación = calificación;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
