package com.charhabil.android.lostincity.filter;

import com.charhabil.android.lostincity.data.CategoryKeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterPresenter implements FilterContract.Presenter{
  private List<FilterItem> mFilters = new ArrayList<>();

  @Override
  public final List<FilterItem> getFilteredCategories() {
    return Collections.unmodifiableList(mFilters);
  }

  @Override public final void start() {
    final CategoryKeeper keeper = CategoryKeeper.getInstance();
    mFilters = keeper.getCategories();
  }

}
