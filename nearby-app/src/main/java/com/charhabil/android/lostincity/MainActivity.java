package com.charhabil.android.lostincity;


import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.charhabil.android.lostincity.adapter.PharmacyAdapter;
import com.charhabil.android.lostincity.bean.Pharmacy;
import com.charhabil.android.lostincity.data.Place;
import com.charhabil.android.lostincity.service.PharmacyService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;




public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    RequestQueue requestQueue;
    private ListView liste;
    String url = null;
    PharmacyAdapter adapter;
    MediaPlayer mediaPlayer;
    private double latitude, longitude;
    final List<String> lan = new ArrayList<>();
    PharmacyService ps = new PharmacyService();

    private ProgressBar bar;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liste = findViewById(R.id.liste);
        bar = findViewById(R.id.progressBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_ph_round);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionGranted) {
            getData();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        /////////////////////
        MobileAds.initialize(this, "ca-app-pub-2294526989231885~7876899561");
        prepareAd();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                ScheduledExecutorService scheduler =
                        Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                }
                                prepareAd();
                            }
                        });

                    }
                }, 20, 50, TimeUnit.SECONDS);
            }
        });


/////////////////////

    }

    public void prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2294526989231885/4452536166");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getData();
                }
            }
        }
    }

    public void getData() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=3000&types=pharmacy&key=AIzaSyDtrJnZMxWGlXHg34U-Q9ggz1bFKZWnTsc";
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                bar.setVisibility(View.VISIBLE);


                load(location.getLatitude(), location.getLongitude());


                bar.setVisibility(View.GONE);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bip);
                mediaPlayer.start();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                buildAlertMessageNoGps();
            }
        });
        liste.setOnItemClickListener(this);
    }

    public void show(View view) {
        liste.setAdapter(new PharmacyAdapter(this, ps.findAll()));


    }

    public double distance(Double latitude, Double longitude, double e, double f) {
        double d2r = Math.PI / 180;
        double dlong = (longitude - f) * d2r;
        double dlat = (latitude - e) * d2r;
        double a = Math.pow(Math.sin(dlat / 2.0), 2) + Math.cos(e * d2r)
                * Math.cos(latitude * d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367 * c;
        return d;

    }

    public void load(final double la, final double ln) {
        ps.findAll().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray res = response.getJSONArray("results");
                    for (int i = 0; i < res.length(); i++) {
                        double lng = res.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        double lat = res.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        String icon = res.getJSONObject(i).getString("icon");
                        String id = res.getJSONObject(i).getString("id");
                        String name = res.getJSONObject(i).getString("name");
                        double distance = distance(la, ln, lat, lng);
                        double rating = 0;
                        if (!res.getJSONObject(i).isNull("rating"))
                            rating = res.getJSONObject(i).getDouble("rating");
                        String vicinity = res.getJSONObject(i).getString("vicinity");
                        ps.create(new Pharmacy(id, name, lat, lng, icon, vicinity, rating, distance));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final TextView idd = view.findViewById(R.id.id);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.mipmap.position);
        alertDialogBuilder.setTitle("Locate the Pharmacy");
        //alertDialogBuilder.setMessage("");
        alertDialogBuilder.setPositiveButton("Location", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("Pharmacy", ps.findBy(idd.getText().toString()));
                intent.putExtra("lat", latitude + "");
                intent.putExtra("lon", longitude + "");
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Path", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double l1 = ps.findBy(idd.getText().toString()).getLat();
                double l2 = ps.findBy(idd.getText().toString()).getLng();

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&saddr=" + latitude + "," + longitude + "&daddr=" + l1 + "," + l2 + ""));
                intent.setComponent(new ComponentName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"));
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
