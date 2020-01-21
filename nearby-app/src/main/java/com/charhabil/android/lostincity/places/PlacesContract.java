
package com.charhabil.android.lostincity.places;

import android.location.Location;
import com.charhabil.android.lostincity.BasePresenter;
import com.charhabil.android.lostincity.BaseView;
import com.charhabil.android.lostincity.data.Place;
import com.esri.arcgisruntime.geometry.Envelope;

import java.util.List;


public interface PlacesContract {

  interface View extends BaseView<Presenter> {

    void showNearbyPlaces(List<Place> places);

    void showProgressIndicator(String message);

    void showMessage(String message);


  }

  interface Presenter extends BasePresenter {

    void setPlacesNearby(List<Place> places);

    void setLocation(Location location);

    void getPlacesNearby();

    Envelope getExtentForNearbyPlaces();

  }
}
