package com.example.mapsweatherapp.controller;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mapsweatherapp.R;
import com.example.mapsweatherapp.model.Ubication;
import com.example.mapsweatherapp.model.WeatherUbication;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WeatherFragment extends Fragment {
    private Manager manager;
    private ArrayList<Ubication> ubications;

    JSONObject jsonObject;
    private ArrayList<WeatherUbication> weatherUbications;

    private RequestQueue requestQueue;

    Spinner spinner;

    TextView txtName, txtWeather, txtDescription, txtTemp;
    ImageView imgWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        syncronizedData(view);
        getLocations();
        addDataWeatherUbications();
        addDataSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtName.setText(weatherUbications.get(position).getName());
                txtWeather.setText(weatherUbications.get(position).getWeather());
                txtDescription.setText(weatherUbications.get(position).getDescription());
                txtTemp.setText(weatherUbications.get(position).getTemp() + "°C");
                Map<String,Integer> hashMap = manager.getDataMapIcon();
                Uri uri = Uri.parse(String.valueOf(hashMap.get(weatherUbications.get(position).getDescription())));
                Glide.with(requireContext()).load(uri).into(imgWeather);
                Log.wtf("TAG", "onItemSelected: " + hashMap.get(weatherUbications.get(position).getDescription()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtName.setText(weatherUbications.get(0).getName());
                txtWeather.setText(weatherUbications.get(0).getWeather());
                txtDescription.setText(weatherUbications.get(0).getDescription());
                txtTemp.setText(weatherUbications.get(0).getTemp() + "°C");
                Map<String,Integer> hashMap = manager.getDataMapIcon();
                Uri uri = Uri.parse(String.valueOf(R.drawable.clear_sky));
                Glide.with(requireContext()).load(uri).into(imgWeather);

            }
        });
        return view;
    }

    private void syncronizedData(View view) {
        manager = new Manager();
        spinner = view.findViewById(R.id.spinner);
        txtName = view.findViewById(R.id.txtViewLocation);
        txtDescription = view.findViewById(R.id.txtViewDescription);
        txtWeather = view.findViewById(R.id.txtViewWeather);
        txtTemp = view.findViewById(R.id.txtViewTemp);
        imgWeather = view.findViewById(R.id.imgViewIcon);
        weatherUbications = new ArrayList<>();
    }

    private void getLocations() {
        ubications = manager.loadDataSharedPreferences(requireContext());
    }



    private void addDataWeatherUbications() {
        for (int i = 0; i < ubications.size(); i++) {
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + ubications.get(i).getLatitude() + "&lon=" + ubications.get(i).getLongitude() + "&appid=de46ba611be3cefe2486a2d32d4a89f5&units=metric";
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.wtf("RESPONSE", response.toString());
                        String name = response.getString("name");
                        String weather = response.getJSONArray("weather").getJSONObject(0).getString("main");
                        String description = response.getJSONArray("weather").getJSONObject(0).getString("description");
                        double temp = response.getJSONObject("main").getDouble("temp");
                        WeatherFragment.this.addWeatherUbicationObject(new WeatherUbication(name, weather, description, temp));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf("ERROR", error.toString());
                }
            });


            requestQueue.add(stringRequest);
        }

    }

    public void addWeatherUbicationObject(WeatherUbication weatherUbication) {
        Log.wtf("RESPONSE", weatherUbication.getName());
        weatherUbications.add(weatherUbication);
        if (weatherUbications.size() == ubications.size()) {
            addDataSpinner();
        }
    }

    private void addDataSpinner() {
        ArrayList<String> list = new ArrayList<String>();
        for (WeatherUbication weatherUbication : weatherUbications) {
            Log.wtf("LIST", weatherUbication.getName());
            list.add(weatherUbication.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
    }




}