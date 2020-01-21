package com.charhabil.android.lostincity.data;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import com.charhabil.android.lostincity.R;
import java.util.Arrays;
import java.util.List;

//la liste de type qu'on peut trouver:

public class CategoryHelper {
  static final List<String> foodTypes = Arrays.asList(
        "African Food",
        "American Food",
        "Argentinean Food",
        "Australian Food",
        "Austrian Food",
        "Bakery",
        "BBQ and Southern Food",
        "Belgian Food",
        "Bistro",
        "Brazilian Food",
        "Breakfast",
        "Brewpub",
        "British Isles Food",
        "Burgers",
        "Cajun and Creole Food",
        "Californian Food",
        "Caribbean Food",
        "Chicken Restaurant",
        "Chilean Food",
        "Chinese Food",
        "Continental Food",
        "Creperie",
        "East European Food",
        "Fast Food",
        "Filipino Food",
        "Fondue",
        "French Food",
        "Fusion Food",
        "German Food",
        "Greek Food",
        "Grill",
        "Hawaiian Food",
        "Ice Cream Shop",
        "Indian Food",
        "Indonesian Food",
        "International Food",
        "Irish Food",
        "Italian Food",
        "Japanese Food",
        "Korean Food",
        "Kosher Food",
        "Latin American Food",
        "Malaysian Food",
        "Mexican Food",
        "Middle Eastern Food",
        "Moroccan Food",
        "Other Restaurant",
        "Pastries",
        "Polish Food",
        "Portuguese Food",
        "Russian Food",
        "Sandwich Shop",
        "Scandinavian Food",
        "Seafood",
        "Snacks",
        "South American Food",
        "Southeast Asian Food",
        "Southwestern Food",
        "Spanish Food",
        "Steak House",
        "Sushi",
        "Swiss Food",
        "Tapas",
        "Thai Food",
        "Turkish Food",
        "Vegetarian Food",
        "Vietnamese Food",
        "Winery");

  //connaitre la categorie du food:

  private static String getCategoryForFoodType(final String foodType){
    String category = foodType;
    if (foodTypes.contains(foodType)){
      category = "Food";
    }
    return category;
  }
  //tracer la location dans la map:

  public static Integer getResourceIdForPlacePin(final com.charhabil.android.lostincity.data.Place p){
    final String category = CategoryHelper.getCategoryForFoodType(p.getType());
    final int d;
    switch (category){
      case "Pizza":
        d = R.drawable.pizza_pin;
        break;
      case "Hotel":
        d = R.drawable.hotel_pin;
        break;
      case "Food":
        d = R.drawable.restaurant_pin;
        break;
      case "Bar or Pub":
        d = R.drawable.bar_pin;
        break;
      case "Coffee Shop":
        d = R.drawable.cafe_pin;
        break;
      default:
        d = R.drawable.empty_pin;
    }
    return d;
  }

  //creation de la liste trouvee:
  public static Drawable getDrawableForPlace(final com.charhabil.android.lostincity.data.Place p, final Activity a){

    final String placeType = p.getType();
    final String category = CategoryHelper.getCategoryForFoodType(placeType);
    final Drawable d;
    switch (category){
      case "Pizza":

        d = ResourcesCompat.getDrawable(a.getResources(), R.mipmap.ic_launcher_pizzax_round,null);
        break;
      case "Hotel":
        d = ResourcesCompat.getDrawable(a.getResources(), R.mipmap.ic_launcher_hotelx_round,null);
        break;
      case "Food":
        d = ResourcesCompat.getDrawable(a.getResources(), R.mipmap.ic_launcher_restx_round,null);
        break;
      case "Bar or Pub":
        d = ResourcesCompat.getDrawable(a.getResources(), R.mipmap.ic_launcher_barx_round,null);
        break;
      case "Coffee Shop":
        d = ResourcesCompat.getDrawable(a.getResources(), R.mipmap.ic_launcher_cafex_round,null);
        break;
      default:
        d = ResourcesCompat.getDrawable(a.getResources(), R.mipmap.ic_launcher_pos_round,null);
    }
    return d;
  }
}
