package com.charhabil.android.lostincity.places;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.charhabil.android.lostincity.R;
import com.charhabil.android.lostincity.filter.FilterContract;
import com.charhabil.android.lostincity.filter.FilterDialogFragment;
import com.charhabil.android.lostincity.filter.FilterPresenter;
import com.charhabil.android.lostincity.map.MapActivity;
import com.charhabil.android.lostincity.util.ActivityUtils;
import com.esri.arcgisruntime.geometry.Envelope;


public class PlacesActivity extends AppCompatActivity implements FilterContract.FilterView,
    ActivityCompat.OnRequestPermissionsResultCallback, com.charhabil.android.lostincity.places.PlacesFragment.FragmentListener {

  private static final int PERMISSION_REQUEST_LOCATION = 0;
  private static final int REQUEST_LOCATION_SETTINGS = 1;
  private static final int REQUEST_WIFI_SETTINGS = 2;
  private com.charhabil.android.lostincity.places.PlacesFragment mPlacesFragment = null;
  private CoordinatorLayout mMainLayout = null;
  private com.charhabil.android.lostincity.places.PlacesPresenter mPresenter = null;

  private static boolean mUserDeniedPermission = false;

  @Override
  public final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);

    mMainLayout = findViewById(R.id.list_coordinator_layout);

    // Check for gps and wifi
    checkSettings();
  }

  private void completeSetUp(){
    // request location permission
    requestLocationPermission();
  }

  @Override
  public final boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu items for use in the action bar
    final MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Set up toolbar
   */
   private void setUpToolbar(){
     final Toolbar toolbar = findViewById(R.id.placeList_toolbar);
     setSupportActionBar(toolbar);

     assert toolbar != null;
     toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
       @Override public boolean onMenuItemClick(final MenuItem item) {
         if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.map_view))){
           // Hide the list, show the map
          showMap();
         }
         if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.filter))){
           final FilterDialogFragment dialogFragment = new FilterDialogFragment();
           final FilterPresenter filterPresenter = new FilterPresenter();
           dialogFragment.setPresenter(filterPresenter);
           dialogFragment.show(getFragmentManager(),"dialog_fragment");

         }
         return false;
       }
     });
   }
  @Override public final void onFilterDialogClose(final boolean applyFilter) {
    if (applyFilter){
      mPresenter.start();
    }
  }
  public static Intent createMapIntent(final Activity a, final Envelope envelope){
    final Intent intent = new Intent(a, MapActivity.class);
    // Get the extent of search results so map
    // can set viewpoint

    if (envelope != null){
      intent.putExtra("MIN_X", envelope.getXMin());
      intent.putExtra("MIN_Y", envelope.getYMin());
      intent.putExtra("MAX_X", envelope.getXMax());
      intent.putExtra("MAX_Y", envelope.getYMax());
      intent.putExtra("SR", envelope.getSpatialReference().getWKText());
    }
    return  intent;
  }
  private void showMap(){
    final Envelope envelope = mPresenter.getExtentForNearbyPlaces();
    final Intent intent = createMapIntent(this, envelope);
    startActivity(intent);
  }

  /**
   * Set up fragments
   */
  private void setUpFragments(){

    mPlacesFragment = (com.charhabil.android.lostincity.places.PlacesFragment) getSupportFragmentManager().findFragmentById(R.id.recycleView) ;

    if (mPlacesFragment == null){
      // Create the fragment
      mPlacesFragment = com.charhabil.android.lostincity.places.PlacesFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mPlacesFragment, R.id.list_fragment_container, "list fragment");
    }
  }

  /**
   * Requests the {@link Manifest.permission#ACCESS_FINE_LOCATION}
   * permission.
   */

  private void requestLocationPermission() {
    if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || mUserDeniedPermission) {
      // Permission has been granted (or denied and ignored), set up the toolbar and fragments.
      setUpToolbar();
      setUpFragments();
    } else {
      ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
              PERMISSION_REQUEST_LOCATION);
    }
  }
  /**
   * Once the app has prompted for permission to access location, the response
   * from the user is handled here. If permission exists to access location
   * check if GPS is available and device is not in airplane mode.
   *
   * @param requestCode
   *            int: The request code passed into requestPermissions
   * @param permissions
   *            String: The requested permission(s).
   * @param grantResults
   *            int: The grant results for the permission(s). This will be
   *            either PERMISSION_GRANTED or PERMISSION_DENIED
   */
  @Override
  public final void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
      @NonNull final int[] grantResults) {

    if (requestCode == PERMISSION_REQUEST_LOCATION) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
        if (!mUserDeniedPermission) {
          // Display Snackbar with extra rationale if permission was denied but will only show the
          // snackbar once.
          mUserDeniedPermission = true;
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(mMainLayout, "Location access is required to search for places nearby.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                      @Override
                      public void onClick(final View view) {
                        // Request the permission
                        ActivityCompat.requestPermissions(PlacesActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                      }
                    }).show();
          }
        } else {
          // Permission has been denied twice, go ahead and launch without location
          setUpToolbar();
          setUpFragments();
        }
      } else {
        // Permission has been granted, launch setup
        setUpToolbar();
        setUpFragments();
      }
    }
  }

  @Override public void onCreationComplete() {
    mPresenter = new com.charhabil.android.lostincity.places.PlacesPresenter(mPlacesFragment);
  }

  private boolean locationTrackingEnabled() {
    final LocationManager locationManager = (LocationManager) getApplicationContext()
        .getSystemService(Context.LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  /**
   * Determine wifi connectivity.
   * @return boolean indicating wifi connectivity. True for connected.
   */
  private boolean internetConnectivity(){
    final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    final NetworkInfo wifi = connManager.getActiveNetworkInfo();
    return wifi != null && wifi.isConnected();
  }


  /*
   * Prompt user to turn on location tracking and wireless if needed
   */
  private void checkSettings() {
    // Is GPS enabled?
    final boolean gpsEnabled = locationTrackingEnabled();
    // Is there internet connectivity?
    final boolean internetConnected = internetConnectivity();

    if (gpsEnabled && internetConnected) {
      completeSetUp();
    } else if (!gpsEnabled) {
      final Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
      showDialog(gpsIntent, REQUEST_LOCATION_SETTINGS, getString(R.string.location_tracking_off));
    } else {
      final Intent internetIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
      showDialog(internetIntent, REQUEST_WIFI_SETTINGS, getString(R.string.wireless_off));
    }
  }
  /**
   * When returning from activities concerning wifi mode and location
   * settings, check them again.
   *
   * @param requestCode - an integer representing the type of request
   * @param resultCode - an integer representing the result of the returning activity
   * @param data - the Intent returned
   */
  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    if (requestCode == REQUEST_WIFI_SETTINGS || requestCode == REQUEST_LOCATION_SETTINGS) {
      checkSettings();
    }

  }
  /**
   * Prompt user to change settings required for app
   * @param intent - the Intent containing any data
   * @param requestCode -an integer representing the type of request
   * @param message - a string representing the message to display in the dialog.
   */
  private void showDialog(final Intent intent, final int requestCode, final String message) {

    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setMessage(message);
    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        //
        startActivityForResult(intent, requestCode);
      }
    });
    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        finish();
      }
    });
    alertDialog.create().show();
  }

  @Override
  public void onBackPressed() {
    int count = getSupportFragmentManager().getBackStackEntryCount();
    if (count == 0) {
      super.onBackPressed();
    } else {
      finish();
    }
  }
}
