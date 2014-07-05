package br.com.denuncia.model;

import java.net.URL;

/**
 * Created by User on 05/07/2014.
 */
public class Report {

    private URL photo;

    public Report(URL photo) {
        this.photo = photo;
    }

    public URL getPhoto() {
        return photo;
    }

    public void setPhoto(URL photo) {
        this.photo = photo;
    }
}
