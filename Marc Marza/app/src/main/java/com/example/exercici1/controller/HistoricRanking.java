package com.example.exercici1.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.exercici1.R;
import com.example.exercici1.controller.LoginActivity;
import com.example.exercici1.controller.ResultActivity;
import com.example.exercici1.model.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

public class HistoricRanking extends AppCompatActivity {

    private Button btnReset,btnRanking;
    private TableLayout tblRankingHistorial;

    private TextView txtViewRanking;

    private ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_ranking);
        synchronizeWidget();
        loadDataSharedPreferences();

        btnReset.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        btnRanking.setOnClickListener(v -> {
            ArrayList<Player> playersIntent = (ArrayList<Player>) getIntent().getSerializableExtra("playerGame");
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("playersIntent",playersIntent);
            startActivity(intent);
        });
    }

    private void synchronizeWidget() {
        this.txtViewRanking = (TextView) findViewById(R.id.txtViewRanking);
        this.tblRankingHistorial = (TableLayout) findViewById(R.id.tblLayoutRankingHistorial);
        this.btnReset = (Button) findViewById(R.id.btnReset);
        this.btnRanking = (Button) findViewById(R.id.btnRanking);
    }
    private void loadDataSharedPreferences(){
        ArrayList<Player> playersAux = (ArrayList<Player>) getIntent().getSerializableExtra("playersIntent");
        SharedPreferences sharedPref = getSharedPreferences("MiSharedPreferences",MODE_PRIVATE);
        String listPlayers = sharedPref.getString("sharedPlayers",null);
        Gson gson = new Gson();
        playersAux = gson.fromJson(listPlayers,new TypeToken<ArrayList<Player>>(){}.getType());
        addDataTable(playersAux);
    }

    private void addDataTable(ArrayList<Player>playersTable) {
        Collections.sort(playersTable);
        for (int i = 0; i < playersTable.size(); i++) {
            TableRow tableRow = new TableRow(this);

            tableRow.setBackground(AppCompatResources.getDrawable(this, R.drawable.border));
            if (i == 0) {
                tableRow.setBackgroundColor(ContextCompat.getColor(this,R.color.gold));
            } else if (i == 1) {
                tableRow.setBackgroundColor(ContextCompat.getColor(this,R.color.silver));
            } else if (i == 2) {
                tableRow.setBackgroundColor(ContextCompat.getColor(this,R.color.bronze));
            } else {
                tableRow.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            }

            tableRow.addView(createTable(String.valueOf((i + 1))));

            tableRow.addView(createTable(playersTable.get(i).getName()));

            tableRow.addView(createTable(String.valueOf(playersTable.get(i).getScore().getResultCorrect())));

            tableRow.addView(createTable(String.valueOf(+playersTable.get(i).getScore().getResultIncorrect())));

            this.tblRankingHistorial.addView(tableRow);
        }
    }

    private TextView createTable(String show) {

        TextView text = new TextView(this);
        text.setText(show);
        text.setTextColor(ContextCompat.getColor(this, R.color.black));
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text.setTextSize(18);
        text.setGravity(Gravity.CENTER);

        return text;
    }
}