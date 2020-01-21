package com.charhabil.android.lostincity.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



public class ActivityUtils {
  /**
   * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
   * performed by the {@code fragmentManager}.
   *
   */
  public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
      @NonNull Fragment fragment, int frameId, @NonNull String fragmentName) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(frameId, fragment);
    transaction.addToBackStack(fragmentName);
    transaction.commitAllowingStateLoss();
  }
}
