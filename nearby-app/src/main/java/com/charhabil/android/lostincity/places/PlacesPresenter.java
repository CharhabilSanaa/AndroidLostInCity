
package com.charhabil.android.lostincity.places;

import android.location.Location;
import android.support.annotation.NonNull;
import com.charhabil.android.lostincity.data.LocationService;
import com.charhabil.android.lostincity.data.Place;
import com.charhabil.android.lostincity.data.PlacesServiceApi;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;

import java.util.List;


public class PlacesPresenter implements com.charhabil.android.lostincity.places.PlacesContract.Presenter {


  private final com.charhabil.android.lostincity.places.PlacesContract.View mPlacesView;
  private Point mDeviceLocation = null;

  private LocationService mLocationService;
  private final static int MAX_RESULT_COUNT = 10;
  private final static String GEOCODE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";

  public PlacesPresenter( @NonNull com.charhabil.android.lostincity.places.PlacesContract.View listView){
    mPlacesView = listView;
    mPlacesView.setPresenter(this);
  }

  /**
   * Place presenter starts by using the device
   * location as the initial parameter in the
   * geocode search.
   */
  @Override public final void start() {
    mPlacesView.showProgressIndicator("Finding places...");
    mLocationService = LocationService.getInstance();
    List<Place> existingPlaces = mLocationService.getPlacesFromRepo();
    if (existingPlaces != null ){
      setPlacesNearby(existingPlaces);
    }else{
      LocationService.configureService(GEOCODE_URL,
          // On locator task load success
          new Runnable() {
            @Override public void run() {
              getPlacesNearby();
            }
          },
          // On locator task load error
          new Runnable() {
            @Override public void run() {
              mPlacesView.showMessage("The locator task was unable to load");
            }
          });
    }
  }


  /**
   * Delegates the display of places to the view
   * @param places List<Place> items
   */
  @Override public final void setPlacesNearby(List<Place> places) {
    mPlacesView.showNearbyPlaces(places);
  }

  @Override public final void setLocation(Location location) {
    mDeviceLocation = new Point(location.getLongitude(), location.getLatitude());
  }

  @Override public final void getPlacesNearby() {
    if (mDeviceLocation != null) {
      GeocodeParameters parameters = new GeocodeParameters();
      parameters.setMaxResults(MAX_RESULT_COUNT);
      parameters.setPreferredSearchLocation(mDeviceLocation);
      mLocationService.getPlacesFromService(parameters, new PlacesServiceApi.PlacesServiceCallback() {
        @Override public void onLoaded(Object places) {
          List<Place> data = (List) places;

          // Show list of places
          setPlacesNearby(data);
        }
      });
    }
  }

  @Override public final Envelope getExtentForNearbyPlaces() {
    return mLocationService != null ? mLocationService.getResultEnvelope(): null;
  }

}
