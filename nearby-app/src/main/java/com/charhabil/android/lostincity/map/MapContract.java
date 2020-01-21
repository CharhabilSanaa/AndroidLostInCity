package com.charhabil.android.lostincity.map;

import com.charhabil.android.lostincity.BasePresenter;
import com.charhabil.android.lostincity.BaseView;
import com.charhabil.android.lostincity.data.LocationService;
import com.charhabil.android.lostincity.data.Place;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;

import java.util.List;

public interface MapContract {

  interface View extends BaseView<Presenter>{

    void showNearbyPlaces(List<Place> placeList);

    MapView getMapView();

    void centerOnPlace(Place p);

    void setRoute(RouteResult routeResult, Point start, Point end);

    void showMessage(String message);

    void showProgressIndicator(String message);

    void showRouteDetail(int position);

    void restoreMapView();

    void getRoute(LocationService service);

  }

  interface Presenter extends BasePresenter {

    void findPlacesNearby();

    void centerOnPlace(Place p);

    Place findPlaceForPoint(Point p);

    void getRoute();

    void setCurrentExtent(Envelope envelope);
  }
}
