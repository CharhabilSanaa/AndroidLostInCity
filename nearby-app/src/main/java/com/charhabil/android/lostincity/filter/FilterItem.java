package com.charhabil.android.lostincity.filter;

/**
 * Represents a filter setting used to control what types of places are
 * viewed in the map or list.
 */
public class FilterItem {

  private final String mTitle;
  private final int mIconId;
  private boolean mSelected;
  private final int mSelectedIconId;

  public FilterItem (final String title, final int icon, final boolean s, final int selectedIcon){
    mTitle = title;
    mIconId = icon;
    mSelected = s;
    mSelectedIconId = selectedIcon;
  }

  public final int getIconId() {
    return mIconId;
  }

  public final String getTitle() {
    return mTitle;
  }

  public final boolean getSelected() { return mSelected ;}

  public final void setSelected(final boolean selected){ mSelected = selected; }

  public final int getSelectedIconId() {
    return mSelectedIconId;
  }

  @Override public String toString() {
    return "FilterItem{" +
        "mTitle='" + mTitle + '\'' +
        ", mIconId=" + mIconId +
        ", mSelected=" + mSelected +
        ", mSelectedIconId=" + mSelectedIconId +
        '}';
  }
}
