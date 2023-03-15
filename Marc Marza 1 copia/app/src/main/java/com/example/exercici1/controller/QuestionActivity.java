package com.example.exercici1.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercici1.R;
import com.example.exercici1.model.Player;
import com.example.exercici1.model.Question;
import com.example.exercici1.model.Score;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionActivity extends AppCompatActivity {
    //Declaracions del TextView i del Button
    private TextView textView, txtViewTitle, txtViewNamePlayer;
    private ArrayList<Question> questions;
    private ArrayList<Player> players;

    private ArrayList<Player>playersAux;
    int positionQuestion = 0, counterAnswersCorrect = 0, counterAnswersIncorrect = 0, counterQuestion = 1, numberPlayer = 1;
    private Button btnNo, btnYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        addData();
        initializeData();
        setPlayerTxtView();

        nextQuestion();


        this.btnNo.setOnClickListener(v -> {
            if (!this.questions.get(this.positionQuestion).isAnswer()) {
                this.counterAnswersCorrect++;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.trueMessage), Toast.LENGTH_SHORT).show();

            } else {
                this.counterAnswersIncorrect++;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.falseMessage), Toast.LENGTH_SHORT).show();

            }
            this.positionQuestion++;
            this.counterQuestion++;

            if (this.positionQuestion < this.questions.size()) {
                this.txtViewTitle.setText(getResources().getString(R.string.titleQuestion) + " " + this.counterQuestion);
                this.textView.setText(this.questions.get(this.positionQuestion).getQuestion());
            }

            if (this.positionQuestion == this.questions.size()) {
                Score score = new Score(getResources().getString(R.string.messageAnswerCorrect), getResources().getString(R.string.messageAnswerIncorrect), this.counterAnswersCorrect, this.counterAnswersIncorrect);
                this.players.get(this.numberPlayer - 1).setScore(score);

                if (this.numberPlayer == this.players.size()) {
                    makeIntent();

                } else {
                    resetCounters();
                    Collections.shuffle(this.questions);
                    nextQuestion();
                }

            }
        });

        this.btnYes.setOnClickListener(v -> {
            if (this.questions.get(this.positionQuestion).isAnswer()) {
                this.counterAnswersCorrect++;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.trueMessage), Toast.LENGTH_SHORT).show();

            } else {
                this.counterAnswersIncorrect++;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.falseMessage), Toast.LENGTH_SHORT).show();

            }
            this.positionQuestion++;
            this.counterQuestion++;

            if (this.positionQuestion < this.questions.size()) {
                this.txtViewTitle.setText(getResources().getString(R.string.titleQuestion) + " " + this.counterQuestion);
                this.textView.setText(this.questions.get(this.positionQuestion).getQuestion());

            }

            if (this.positionQuestion == this.questions.size()) {
                Score score = new Score(getResources().getString(R.string.messageAnswerCorrect), getResources().getString(R.string.messageAnswerIncorrect), this.counterAnswersCorrect, this.counterAnswersIncorrect);
                this.players.get(numberPlayer - 1).setScore(score);

                if (this.numberPlayer == this.players.size()) {
                    makeIntent();

                } else {
                    resetCounters();
                    Collections.shuffle(this.questions);
                    nextQuestion();
                }

            }
        });


    }
    private void loadDataSharedPreferences(){

        SharedPreferences sharedPref = getSharedPreferences("MiSharedPreferences",MODE_PRIVATE);
        String listPlayers = sharedPref.getString("sharedPlayers",null);
        Gson gson = new Gson();
        this.playersAux = gson.fromJson(listPlayers,new TypeToken<ArrayList<Player>>(){}.getType());
        if(this.playersAux != null){
            this.playersAux.addAll(this.players);
        }else{
            this.playersAux = new ArrayList<>();
            this.playersAux.addAll(this.players);
        }


    }
    private void makeIntent(){
        loadDataSharedPreferences();
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.playersAux);
        prefsEditor.putString("sharedPlayers", json);
        prefsEditor.apply();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("playersIntent",this.players);
        this.finish();
        startActivity(intent);
    }

    private void nextQuestion() {
        this.textView.setText(this.questions.get(this.positionQuestion).getQuestion());
        this.txtViewTitle.setText(getResources().getString(R.string.titleQuestion) + " " + this.counterQuestion);
    }

    private void addData() {
        this.questions = new ArrayList<>();
        String[] arrayQuestion = getResources().getStringArray(R.array.question);
        String[] arrayAnswers = getResources().getStringArray(R.array.answers);
        Question question;
        for (int i = 0; i < arrayQuestion.length; i++) {
            if (arrayAnswers[i].equalsIgnoreCase("si")) {
                question = new Question(arrayQuestion[i], true);
            } else {
                question = new Question(arrayQuestion[i], false);
            }
            this.questions.add(question);
        }
        Collections.shuffle(this.questions);
    }

    private void initializeData() {
        this.textView = (TextView) findViewById(R.id.textView);
        this.txtViewTitle = (TextView) findViewById(R.id.textViewTitle);
        this.txtViewNamePlayer = (TextView) findViewById(R.id.textViewTitleUser);

        this.btnNo = (Button) findViewById(R.id.button1);
        this.btnYes = (Button) findViewById(R.id.button2);

    }

    private void setPlayerTxtView() {
        players = (ArrayList<Player>) getIntent().getSerializableExtra("players");
        this.txtViewNamePlayer.setText(getResources().getString(R.string.txtUserAnswer) + " " + this.players.get(0).getName());

    }

    private void resetCounters() {
        this.counterQuestion = 1;
        this.positionQuestion = 0;
        this.txtViewNamePlayer.setText(getResources().getString(R.string.txtUserAnswer) + " " + this.players.get(this.numberPlayer).getName());
        this.numberPlayer++;
        this.counterAnswersCorrect = 0;
        this.counterAnswersIncorrect = 0;

    }
}