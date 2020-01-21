
package com.charhabil.android.lostincity.map;

import android.support.annotation.NonNull;
import com.charhabil.android.lostincity.data.LocationService;
import com.charhabil.android.lostincity.data.Place;
import com.charhabil.android.lostincity.data.PlacesServiceApi;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;


public class MapPresenter implements MapContract.Presenter {

  private final static String TAG = MapPresenter.class.getSimpleName();

  private final MapContract.View mMapView;
  private LocationService mLocationService;
  private final static int MAX_RESULT_COUNT = 10;
  private final static String GEOCODE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";
  private Place mCenteredPlace;

  public MapPresenter(@NonNull final MapContract.View mapView ){
    mMapView = mapView;
    mMapView.setPresenter(this);
  }

  /**
   * Use the location service to geocode places of interest
   * based on the map's visible area extent.
   */
  @Override public final void findPlacesNearby() {
    mMapView.showProgressIndicator("Finding nearby places...");
    final Point g =  mMapView.getMapView().getVisibleArea().getExtent().getCenter();

    if ( g !=null ){
      final GeocodeParameters parameters = new GeocodeParameters();
      parameters.setMaxResults(MAX_RESULT_COUNT);
      parameters.setPreferredSearchLocation(g);
      mLocationService.getPlacesFromService(parameters, new PlacesServiceApi.PlacesServiceCallback() {
        @Override public void onLoaded(final Object places) {
          final List<Place> data = (List) places;



          // Create graphics for displaying locations in map
          mMapView.showNearbyPlaces(data);
        }
      });
    }
  }

  @Override public final void centerOnPlace(final Place p) {
    mCenteredPlace = p;
    mMapView.centerOnPlace(mCenteredPlace);
  }

  @Override public final Place findPlaceForPoint(final Point p) {
    Place foundPlace = null;
    final List<Place> foundPlaces =mLocationService.getPlacesFromRepo();
    for (final Place place : foundPlaces){
      if (p.equals(place.getLocation())){
        foundPlace = place;
        break;
      }
    }
    return foundPlace;
  }

  @Override public final void getRoute() {
    if ((mCenteredPlace != null) && (mLocationService.getCurrentLocation() != null)){
      mMapView.showProgressIndicator("Retrieving route...");
      mMapView.getRoute(mLocationService);
    }
  }

  /**
   * Set the envelope based on map's current
   * view.
   * @param envelope - Envelope representing visible area of map.
   */
  @Override public void setCurrentExtent(final Envelope envelope) {
    mLocationService.setCurrentEnvelope(envelope);
  }

  /**
   * The entry point for this class starts by loading the geocoding service.
   */
  @Override public final void start() {
    mLocationService = LocationService.getInstance();
    final List<Place> existingPlaces = mLocationService.getPlacesFromRepo();
    if (existingPlaces != null){
      mMapView.showNearbyPlaces(existingPlaces);
    }else{
      LocationService.configureService(GEOCODE_URL,
          // On locator task load success
          new Runnable() {
            @Override public void run() {
              findPlacesNearby();
            }
          },
          // On locator task load error
          new Runnable() {
            @Override public void run() {
              mMapView.showMessage("The locator task was unable to load");
            }
          });
    }
  }

}
