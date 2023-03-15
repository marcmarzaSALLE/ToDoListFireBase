package com.example.exercici1.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.exercici1.R;
import com.example.exercici1.model.Player;

import java.util.ArrayList;
import java.util.Collections;

public class ResultActivity extends AppCompatActivity {
    private Button btnReset,btnRanking;
    private TableLayout tblRankingGame;

    private TextView txtViewRanking;
    private ArrayList<Player> players;

    private int option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        synchronizeWidget();
        addDataTable();
        btnReset.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        btnRanking.setOnClickListener(v -> {
            ArrayList<Player> playersIntent = (ArrayList<Player>) getIntent().getSerializableExtra("playersIntent");
            Intent intent = new Intent(this, HistoricRanking.class);
            intent.putExtra("playerGame",playersIntent);
            startActivity(intent);
        });

    }

    private void synchronizeWidget() {
        this.txtViewRanking = (TextView) findViewById(R.id.txtViewRanking);
        this.tblRankingGame = (TableLayout) findViewById(R.id.tableLayout);
        this.btnReset = (Button) findViewById(R.id.btnReset);
        this.btnRanking = (Button) findViewById(R.id.btnRanking);
    }

    private void addDataTable() {
        ArrayList<Player> playersIntent = (ArrayList<Player>) getIntent().getSerializableExtra("playersIntent");
        Collections.sort(playersIntent);
        for (int i = 0; i < playersIntent.size(); i++) {
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

            tableRow.addView(createTable(playersIntent.get(i).getName()));

            tableRow.addView(createTable(String.valueOf(playersIntent.get(i).getScore().getResultCorrect())));

            tableRow.addView(createTable(String.valueOf(+playersIntent.get(i).getScore().getResultIncorrect())));

            this.tblRankingGame.addView(tableRow);
        }

    }


    TextView createTable(String show) {

        TextView text = new TextView(this);
        text.setText(show);
        text.setTextColor(ContextCompat.getColor(this, R.color.black));
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        text.setTextSize(18);
        text.setGravity(Gravity.CENTER);

        return text;
    }

}