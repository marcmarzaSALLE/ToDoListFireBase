package com.example.exercici1.controller;

import static java.sql.Types.NULL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercici1.R;
import com.example.exercici1.model.Player;

import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private Button btnStart, btnSendNumberUser;
    private EditText edtTextNumberUser, edtTextName;
    private TextView txtViewMsgNumberUser;
    private int counterUserRegister = 1, numberUser;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        synchronizeWidget();
        disableNameUser();

        this.btnSendNumberUser.setOnClickListener(v -> {
            getNumberUsers();
        });

    }

    private void getNumberUsers() {
        if (this.edtTextNumberUser.getText().toString().matches("")) {
            this.edtTextNumberUser.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastGapRequerit), Toast.LENGTH_SHORT).show();


        } else {
            String numberUserString = this.edtTextNumberUser.getText().toString();
            this.numberUser = Integer.parseInt(numberUserString);

            if (this.numberUser <= 0) {
                this.edtTextNumberUser.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastMinimumOneUser), Toast.LENGTH_SHORT).show();
            } else if (this.numberUser >10) {
                this.edtTextNumberUser.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastMaximumTenUser), Toast.LENGTH_SHORT).show();
            } else {
                enableNameUser();
                registerUser(this.numberUser);
            }

        }
    }

    private void registerUser(int number) {


        this.txtViewMsgNumberUser.setText(getResources().getString(R.string.txtMsgUserRegister) + " " + counterUserRegister);
        if (number == 1) {
            btnStart.setText(getResources().getString(R.string.btnStart));
        }
        onClickKeyBoard();
        btnStart.setOnClickListener(v -> {

            String name = this.edtTextName.getText().toString();
            if (name.matches("")) {
                this.edtTextName.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastGapRequerit), Toast.LENGTH_SHORT).show();
            } else {
                if (this.counterUserRegister == number) {
                    disableVisibilityKeyBoard();
                    disableOnlyTextName();
                    this.player = new Player(this.edtTextName.getText().toString());
                    this.players.add(this.player);
                    Intent intent = new Intent(this, QuestionActivity.class);
                    intent.putExtra("players", this.players);
                    this.finish();
                    startActivity(intent);
                } else {
                    disableVisibilityKeyBoard();
                    this.player = new Player(this.edtTextName.getText().toString());
                    this.players.add(this.player);
                    this.edtTextName.getText().clear();
                    this.counterUserRegister++;
                    this.txtViewMsgNumberUser.setText(getResources().getString(R.string.txtMsgUserRegister) + " " + counterUserRegister);
                    if (this.counterUserRegister == number) {
                        btnStart.setText(getResources().getString(R.string.btnStart));
                    }
                    enableVisibilityKeyBoard();
                }
            }
        });
    }

    private void onClickKeyBoard() {
        this.edtTextName.setOnClickListener(view -> {
            disableVisibilityKeyBoard();
        });
    }

    private void disableVisibilityKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(this.edtTextName.getWindowToken(), 0);
    }

    private void enableVisibilityKeyBoard() {
        this.edtTextName.requestFocus(); //Asegurar que editText tiene focus
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.edtTextName, InputMethodManager.SHOW_IMPLICIT);
    }


    private void disableOnlyTextName() {
        this.edtTextName.setEnabled(false);
    }

    private void synchronizeWidget() {
        this.btnStart = (Button) findViewById(R.id.buttonLogin);
        this.btnSendNumberUser = (Button) findViewById(R.id.btnSendNumberUser);
        this.edtTextNumberUser = (EditText) findViewById(R.id.numberUsers);
        this.edtTextName = (EditText) findViewById(R.id.editTxtNameUser);
        this.txtViewMsgNumberUser = (TextView) findViewById(R.id.txtViewMsgUserNumber);
        this.txtViewMsgNumberUser.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
    }

    private void disableNameUser() {
        this.txtViewMsgNumberUser.setVisibility(View.GONE);
        this.edtTextName.setVisibility(View.GONE);
        this.btnStart.setVisibility(View.GONE);
        this.edtTextName.setEnabled(false);
        this.btnStart.setEnabled(false);
    }

    private void enableNameUser() {
        this.txtViewMsgNumberUser.setVisibility(View.VISIBLE);
        this.edtTextName.setVisibility(View.VISIBLE);
        this.btnStart.setVisibility(View.VISIBLE);
        this.edtTextName.setEnabled(true);
        this.btnStart.setEnabled(true);
        this.edtTextNumberUser.setEnabled(false);
        this.btnSendNumberUser.setEnabled(false);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.edtTextNumberUser.isEnabled()) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                getNumberUsers();
                return true;
            }

        }
        return false;
    }


}