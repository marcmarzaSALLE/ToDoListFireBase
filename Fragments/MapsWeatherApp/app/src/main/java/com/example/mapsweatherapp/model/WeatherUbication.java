package com.example.mapsweatherapp.model;

public class WeatherUbication {
    String name;
    String weather;
    String description;
    double temp;

    public WeatherUbication(String name, String weather, String description, double temp) {
        this.name = name;
        this.weather = weather;
        this.description = description;
        this.temp = temp;
    }

    public String getName() {
        return name;
    }

    public String getWeather() {
        return weather;
    }

    public String getDescription() {
        return description;
    }

    public double getTemp() {
        return temp;
    }
}
