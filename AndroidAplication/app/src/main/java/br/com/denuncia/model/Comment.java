package br.com.denuncia.model;

import java.net.URL;

/**
 * Created by User on 05/07/2014.
 */
public class Comment {

    private String text;
    private URL photo;

    public Comment(String text, URL photo) {
        this.text = text;
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public URL getPhoto() {
        return photo;
    }

    public void setPhoto(URL photo) {
        this.photo = photo;
    }
}
