package br.com.denuncia.model;

import java.net.URL;

/**
 * Created by User on 05/07/2014.
 */
public class Comment {

    private String user;
    private String text;
    private URL photo;

    public Comment(String user, String text, URL photo) {
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
