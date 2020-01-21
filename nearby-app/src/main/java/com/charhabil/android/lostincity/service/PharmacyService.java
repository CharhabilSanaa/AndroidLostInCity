package com.charhabil.android.lostincity.service;

import com.charhabil.android.lostincity.bean.Pharmacy;
import com.charhabil.android.lostincity.data.Place;

import java.util.ArrayList;
import java.util.List;

public class PharmacyService {
    private List<Pharmacy> pharmacies;

    public PharmacyService() {
        this.pharmacies = new ArrayList<>();
    }

    public void create(Pharmacy p){
        pharmacies.add(p);
    }

    public List<Pharmacy> findAll(){
        return pharmacies;
    }

    public Pharmacy findBy(String id){
        for(Pharmacy p : pharmacies){
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }
}