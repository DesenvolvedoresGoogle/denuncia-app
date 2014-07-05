package br.com.denuncia.model;

import java.net.URL;

/**
 * Created by User on 05/07/2014.
 */
public class Report {

    private URL photo;
    private String title;
    private String description;
    private float latitude;
    private float longitude;


    public Report(URL photo, String title, String description, float latitude, float longitude) {
        this.photo = photo;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public URL getPhoto() {
        return photo;
    }

    public void setPhoto(URL photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
