package com.charhabil.android.lostincity.bean;


import android.support.annotation.NonNull;

import com.charhabil.android.lostincity.data.Place;

import java.io.Serializable;

public class Pharmacy implements Comparable<Pharmacy>, Serializable {

    private String id;
    private String name;
    private double lat;
    private double lng;
    private String icon;
    private String vicinity;
    private double rating;
    private double distance;

    public Pharmacy(String id, String name, double lat, double lng, String icon, String vicinity, double rating, double distance) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
        this.vicinity = vicinity;
        this.rating = rating;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(@NonNull Pharmacy o) {
        Double i1 = new Double(o.getDistance());
        Double i2 = new Double(this.getDistance());
        return i2.compareTo(i1);
    }


}
