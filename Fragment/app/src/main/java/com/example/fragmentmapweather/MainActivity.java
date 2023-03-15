package com.example.fragmentmapweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;

    MapFragment mapFragment;
    WeatherFragment weatherFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.maps);
        weatherFragment =(WeatherFragment)getSupportFragmentManager().findFragmentById(R.id.weather);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId())
                {
                    case R.id.maps:
                        Log.d("MAPA","MAPAS");

                        ft.replace(R.id.flFragment,MapFragment.class,null);
                        ft.commit();
                        return true;
                    case R.id.weather:
                        ft.replace(R.id.flFragment,WeatherFragment.class,null);
                        ft.commit();
                        Log.d("WEATHER","WEATHER");
                }
                return false;
            }

        });

    }
}