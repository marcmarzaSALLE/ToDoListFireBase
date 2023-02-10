package com.example.exercici1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button trueButton, falseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = (Button) findViewById(R.id.button1);
        falseButton = (Button) findViewById(R.id.button2);

        trueButton.setOnClickListener(v ->{
            Toast toast=Toast.makeText(getApplicationContext(),getResources().getString(R.string.trueMessage),Toast.LENGTH_SHORT);
            //toast.setMargin(50,50);
            toast.show();
        });

        falseButton.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.falseMessage),Toast.LENGTH_SHORT).show();
        });
    }
}