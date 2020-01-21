package com.charhabil.android.lostincity.filter;

import com.charhabil.android.lostincity.BasePresenter;
import com.charhabil.android.lostincity.BaseView;

import java.util.List;


public interface FilterContract {
  interface View extends BaseView<Presenter> {

  }
  interface Presenter extends BasePresenter {
    List<com.charhabil.android.lostincity.filter.FilterItem> getFilteredCategories();
  }

  interface FilterView{
    void onFilterDialogClose(boolean applyFilter);
  }
}
