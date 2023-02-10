package com.example.exercici1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Declaracions del TextView i del Button
    private TextView textView;
    private Button trueButton, falseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Amb el findViewById, és vincula el button del xml amb la variable creada, per així poder treballar amb ella en el mainActivity
        trueButton = (Button) findViewById(R.id.button1);
        falseButton = (Button) findViewById(R.id.button2);

        //setOnClickListener, es la funció que s'executa un cop presionas un botó.
        trueButton.setOnClickListener(v ->{
            //1. Forma de declarar un Toast
            //En el primer valor li dius que es un context de l'aplicació, en el segon es el missatge que mostrará, i en el tercer el temps que mostrarà el missatge
            Toast toast=Toast.makeText(getApplicationContext(),getResources().getString(R.string.trueMessage),Toast.LENGTH_SHORT);
            //toast.setMargin(50,50);
            toast.show();
        });

        falseButton.setOnClickListener(v->{
            //2. Segona forma de declarar un Toast, és igual que la primera però sense guardar en una variable el toast.
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.falseMessage),Toast.LENGTH_SHORT).show();
        });
    }
}