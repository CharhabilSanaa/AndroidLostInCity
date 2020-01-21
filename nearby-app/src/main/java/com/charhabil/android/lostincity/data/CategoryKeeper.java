package com.charhabil.android.lostincity.data;

import com.charhabil.android.lostincity.R;
import com.charhabil.android.lostincity.filter.FilterItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//classe qui contient les categories a chercher:

public class CategoryKeeper {

    private static CategoryKeeper instance = null;
    private final ArrayList<FilterItem> mCategories = new ArrayList<>();

    private CategoryKeeper(){
      mCategories.add(new FilterItem("Bar", R.mipmap.ic_launcher_barx_round, true, R.mipmap.ic_launcher_barx_round));
      mCategories.add(new FilterItem("Coffee Shop", R.mipmap.ic_launcher_cafex_round, true, R.mipmap.ic_launcher_cafex_round));
      mCategories.add(new FilterItem("Food", R.mipmap.ic_launcher_restx_round, true, R.mipmap.ic_launcher_restx_round));
      mCategories.add(new FilterItem("Hotel", R.mipmap.ic_launcher_hotelx_round, true, R.mipmap.ic_launcher_hotelx_round));
      mCategories.add(new FilterItem("Pizza", R.mipmap.ic_launcher_pizzax_round, false, R.mipmap.ic_launcher_pizzax_round));
    }

    public static CategoryKeeper getInstance(){
      if (CategoryKeeper.instance == null){
        CategoryKeeper.instance = new CategoryKeeper();
      }
      return CategoryKeeper.instance;
    }

    public final List<FilterItem> getCategories(){
      return Collections.unmodifiableList(mCategories);
    }

    public final List<String> getSelectedTypes(){
        final List<String> selectedTypes = new ArrayList<>();
        for (final FilterItem item : mCategories){
            if (!item.getSelected()){
                // Because places with food are sub-categorized by
                // food type, add them to the filter list.
                if (item.getTitle().equalsIgnoreCase("Food")){
                    selectedTypes.addAll(CategoryHelper.foodTypes);
                }else {
                    selectedTypes.add(item.getTitle());
                }
            }
        }
        return selectedTypes;
    }
}
