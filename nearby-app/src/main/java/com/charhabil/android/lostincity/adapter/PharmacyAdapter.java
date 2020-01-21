package com.charhabil.android.lostincity.adapter;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.charhabil.android.lostincity.R;
import com.charhabil.android.lostincity.bean.Pharmacy;
import java.util.Collections;
import java.util.List;



public class PharmacyAdapter extends BaseAdapter{

    private List<Pharmacy> pharmacies;
    private LayoutInflater layoutInflater;

    public PharmacyAdapter(Activity activity, List<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
        Collections.sort(this.pharmacies);
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pharmacies.size();
    }

    @Override
    public Object getItem(int position) {
        return pharmacies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.place_item, null);
        TextView name = convertView.findViewById(R.id.name);
        TextView id = convertView.findViewById(R.id.id);

        TextView vicinity = convertView.findViewById(R.id.vicinity);
        ImageView icon = convertView.findViewById(R.id.icon);
        RatingBar rating = convertView.findViewById(R.id.rating);

        Drawable drawable = rating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP);


        TextView distance  = convertView.findViewById(R.id.distance);
        name.setText(pharmacies.get(position).getName().toUpperCase());
        vicinity.setText(pharmacies.get(position).getVicinity());
        if(pharmacies.get(position).getDistance() < 1)
            distance.setText(((double) Math.round(pharmacies.get(position).getDistance()*100)/100)* 1000 +" m");
        else
            distance.setText((double) Math.round(pharmacies.get(position).getDistance() * 100) / 100 +" km");
        icon.setImageResource(R.mipmap.ic_launcher_ph_round);
        id.setText(pharmacies.get(position).getId());
        rating.setRating((float) pharmacies.get(position).getRating());
        return convertView;
    }
}
