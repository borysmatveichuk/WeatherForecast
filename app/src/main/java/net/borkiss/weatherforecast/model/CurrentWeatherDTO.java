package net.borkiss.weatherforecast.model;

import java.io.Serializable;

public class CurrentWeatherDTO implements Serializable {
    private static final long serialVersionUID = 4262761557170039078L;

    private int placeId;
    private long time;
    private String document;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
