package com.charhabil.android.lostincity.map;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.charhabil.android.lostincity.R;
import com.charhabil.android.lostincity.filter.FilterContract;
import com.charhabil.android.lostincity.route.RouteDirectionsFragment;
import com.charhabil.android.lostincity.util.ActivityUtils;
import com.esri.arcgisruntime.security.AuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.AuthenticationManager;
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;

import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements FilterContract.FilterView {

  private com.charhabil.android.lostincity.map.MapPresenter mMapPresenter = null;

  @Override
  protected final void onCreate(final Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    //ajout:
    String languageToLoad = "EN";
    Locale locale = new Locale(languageToLoad);
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext().getResources().updateConfiguration(config,
            getBaseContext().getResources().getDisplayMetrics());

    setContentView(R.layout.map_layout);



    setUpMapFragment();

    setUpAuth();

  }

  /**
   * Authentication method used when requesting a route.
   */
  private void setUpAuth(){
    try{
      final AuthenticationChallengeHandler authenticationChallengeHandler = new DefaultAuthenticationChallengeHandler(this);
      AuthenticationManager.setAuthenticationChallengeHandler(authenticationChallengeHandler);
    }catch(final Exception e){
      Log.e("MapActivity", "Authentication handling issue: " + e.getMessage());
    }
  }
  /**
   * When user presses 'Apply' button in filter dialog,
   * re-filter results.
   * @param applyFilter - boolean
   */
  @Override public final void onFilterDialogClose(final boolean applyFilter) {
    if (applyFilter){
      mMapPresenter.start();
    }
  }


  /**
   * Set up map fragment
   */
  private void setUpMapFragment(){

    final FragmentManager fm = getSupportFragmentManager();
    com.charhabil.android.lostincity.map.MapFragment mapFragment = (com.charhabil.android.lostincity.map.MapFragment) fm.findFragmentById(R.id.map_fragment_container);

    if (mapFragment == null) {
      mapFragment = com.charhabil.android.lostincity.map.MapFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mapFragment, R.id.map_fragment_container, getString(R.string.map_fragment));
    }
    mMapPresenter = new com.charhabil.android.lostincity.map.MapPresenter(mapFragment);

  }

  /**
   * Show the list of directions
   * @param directions List of DirectionManeuver items containing navigation directions
   */
  public final void showDirections(final List<DirectionManeuver> directions){
    final FragmentManager fm = getSupportFragmentManager();
    RouteDirectionsFragment routeDirectionsFragment = (RouteDirectionsFragment) fm.findFragmentById(R.id.route_directions_container);
    if (routeDirectionsFragment == null){
      routeDirectionsFragment = RouteDirectionsFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), routeDirectionsFragment, R.id.route_directions_container, getString(R.string.route_fragment));
    }
    // Show the fragment
    final LinearLayout layout = findViewById(R.id.route_directions_container);
    layout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
    layout.requestLayout();

    // Hide the map
    final FrameLayout mapLayout = findViewById(R.id.map_fragment_container);
    final CoordinatorLayout.LayoutParams  layoutParams  =  new CoordinatorLayout.LayoutParams(0,0);
    layoutParams.setMargins(0, 0,0,0);
    mapLayout.setLayoutParams(layoutParams);
    mapLayout.requestLayout();

    routeDirectionsFragment.setRoutingDirections(directions);
  }

  /**
   * Display the specific directions for the segment of the route
   * the user has clicked on.
   * @param position An integer representing the index in the
   *                 list of segment directions.
   */
  public final void showRouteDetail(final int position){
    // Hide the list of directions
    final LinearLayout layout = findViewById(R.id.route_directions_container);
    layout.setLayoutParams(new CoordinatorLayout.LayoutParams(0,0));
    layout.requestLayout();

    // Restore the map, removing any route segment detail
    final com.charhabil.android.lostincity.map.MapFragment mapFragment = restoreMapAndRemoveRouteDetail();
    // Add specific route segment detail
    mapFragment.showRouteDetail(position);
  }

  /**
   * Restore map to original size and remove
   * views associated with displaying route segments.
   * @return MapFragment - The fragment containing the map
   */
  public final com.charhabil.android.lostincity.map.MapFragment restoreMapAndRemoveRouteDetail(){
    // Remove the route directions
    final LinearLayout layout = findViewById(R.id.route_directions_container);
    layout.setLayoutParams(new CoordinatorLayout.LayoutParams(0, 0));
    layout.requestLayout();

    // Show the map
    final FrameLayout mapLayout = findViewById(R.id.map_fragment_container);

    final CoordinatorLayout.LayoutParams  layoutParams  =  new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    mapLayout.setLayoutParams(layoutParams);
    mapLayout.requestLayout();

    final FragmentManager fm = getSupportFragmentManager();
    final com.charhabil.android.lostincity.map.MapFragment mapFragment = (com.charhabil.android.lostincity.map.MapFragment) fm.findFragmentById(R.id.map_fragment_container);
    mapFragment.removeRouteDetail();
    return mapFragment;
  }

  /**
   * Adjust the map view and show route
   */
  public final void restoreRouteView(){
    com.charhabil.android.lostincity.map.MapFragment mapFragment = restoreMapAndRemoveRouteDetail();
    mapFragment.displayRoute();
  }

  @Override
  public void onBackPressed() {
    int count = getSupportFragmentManager().getBackStackEntryCount();
    if (count == 0) {
      super.onBackPressed();
    } else {
      getSupportFragmentManager().popBackStack();
      FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(count-1);
      String fragmentName = entry.getName();
      if (fragmentName.equalsIgnoreCase(getString(R.string.route_fragment))){
        restoreRouteView();
      }else{
        finish();
      }
    }
  }
}
