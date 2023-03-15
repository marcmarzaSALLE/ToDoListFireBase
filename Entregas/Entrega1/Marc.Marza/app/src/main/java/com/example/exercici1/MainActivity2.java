package com.example.exercici1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private TextView txtViewNumberQuestion,txtViewAnswersCorrect,txtViewAnswerIncorrect;
    private ImageView imageView;
    private Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txtViewNumberQuestion =findViewById(R.id.textView2);
        txtViewAnswersCorrect=findViewById(R.id.txtViewAnswersCorrect);
        txtViewAnswerIncorrect=findViewById(R.id.txtViewAnswersIncorrect);
        imageView=findViewById(R.id.imageView);

        String numberQuestion = getIntent().getStringExtra("numberQuestion");
        result = (Result) getIntent().getSerializableExtra("result");
        txtViewNumberQuestion.setText(numberQuestion);
        txtViewAnswersCorrect.setText(result.messageResultCorrect());
        txtViewAnswerIncorrect.setText(result.messageResultIncorrect());

        if(result.getResultCorrect()<5){
            imageView.setImageResource(R.drawable.emojisad);

        }else if(result.getResultCorrect()>5){
            imageView.setImageResource(R.drawable.emojihappy);
        }else if(result.getResultCorrect()==5){
            imageView.setImageResource(R.drawable.emojineutral);

        }
    }
}