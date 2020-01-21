package com.charhabil.android.lostincity;

import com.charhabil.android.lostincity.data.Place;

public interface PlaceListener {
  void showDetail(Place place);
  void onMapViewChange();
}
