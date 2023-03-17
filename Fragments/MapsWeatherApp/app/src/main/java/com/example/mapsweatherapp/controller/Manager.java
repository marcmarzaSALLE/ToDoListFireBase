package com.example.mapsweatherapp.controller;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.mapsweatherapp.R;
import com.example.mapsweatherapp.model.Ubication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Manager {
    private ArrayList<Ubication> ubications;


    public Manager() {
        this.ubications = new ArrayList<>();
    }

    public ArrayList<Ubication> loadDataSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("sharedUbications", MODE_PRIVATE);
        String listTask = sharedPref.getString("sharedUbication", null);
        Gson gson = new Gson();
        this.ubications = gson.fromJson(listTask, new TypeToken<ArrayList<Ubication>>() {
        }.getType());
        if (this.ubications == null) {
            this.ubications = new ArrayList<>();
        }
        return this.ubications;
    }



    public void saveDataSharedPreferences(Context context, Ubication ubication) {
        loadDataSharedPreferences(context);
        boolean exist = false;
        if (!this.ubications.isEmpty()) {
            for (Ubication u : this.ubications) {
                if (u.getName().equals(ubication.getName())) {
                    exist = true;
                    break;
                }
            }
        }
        if (!exist) {
            this.ubications.add(ubication);
        }
        SharedPreferences sharedPref = context.getSharedPreferences("sharedUbications", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.ubications);
        editor.putString("sharedUbication", json);
        editor.apply();
    }


    public Map<String, Integer> getDataMapIcon() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("clear sky", R.drawable.clear_sky);
        map.put("thunderstorm", R.drawable.thunderstorm);
        map.put("few clouds", R.drawable.clouds);
        map.put("snow", R.drawable.snow);
        map.put("scattered clouds", R.drawable.clouds);
        map.put("mist", R.drawable.mist);
        map.put("broken clouds", R.drawable.clouds);
        map.put("shower rain", R.drawable.rain);
        map.put("rain", R.drawable.rain);
        return map;
    }
}
