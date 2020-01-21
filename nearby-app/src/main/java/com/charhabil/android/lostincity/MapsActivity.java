package com.charhabil.android.lostincity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.charhabil.android.lostincity.bean.Pharmacy;
import com.charhabil.android.lostincity.data.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        Serializable s = intent.getSerializableExtra("place");
        Pharmacy p = (Pharmacy) s;
        double lat = Double.parseDouble(intent.getStringExtra("lat"));
        double lon = Double.parseDouble(intent.getStringExtra("lon"));
        // Add a marker in Sydney and move the camera
        LatLng place = new LatLng(p.getLat(), p.getLng());
        mMap.addMarker(new MarkerOptions().position(place).title(p.getName()));
        LatLng me = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(me).title("me").icon(BitmapDescriptorFactory.fromResource(R.mipmap.boy)));
        float zoomLevel = 15.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, zoomLevel));
    }
}

