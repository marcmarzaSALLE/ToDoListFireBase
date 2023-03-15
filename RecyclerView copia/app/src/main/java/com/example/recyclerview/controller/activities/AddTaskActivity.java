package com.example.recyclerview.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.fragment.DatePickerFragment;
import com.example.recyclerview.controller.fragment.TimePickerFragment;
import com.example.recyclerview.model.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    Button btnSaveTask;
    EditText edtNameTask, edtDescriptionTask, edtDate, edtTime;
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        syncronizedWidget();
        this.btnSaveTask.setOnClickListener(v -> {
            checkData();
        });
        edtDate.setOnClickListener(v -> {
            showDateDialog();
        });
        edtTime.setOnClickListener(v -> {
            showTimeDialog();
        });
    }

    private void syncronizedWidget() {
        this.btnSaveTask = (Button) findViewById(R.id.btnSaveTask);
        this.edtNameTask = (EditText) findViewById(R.id.edtTextTitleTask);
        this.edtDescriptionTask = (EditText) findViewById(R.id.edtDescriptionTask);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
    }

    private void showDateDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                String format = "dd/MM/yyyy";
                selectedDate= new SimpleDateFormat(format).format(cal.getTime());
                edtDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimeDialog() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final String selectedTime = hourOfDay + ":" + minute;
                edtTime.setText(selectedTime);
            }
        });
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    private void checkData() {
        if (nameTaskIsEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.emptyGap), Toast.LENGTH_SHORT).show();
        } else {
            if (descriptionTaskIsEmpty()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.emptyGap), Toast.LENGTH_SHORT).show();
            } else {
                if (edtDate.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorDate), Toast.LENGTH_SHORT).show();

                } else if (edtTime.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorDate), Toast.LENGTH_SHORT).show();
                } else {
                    checkDateTime();
                }
            }

        }


    }

    private void checkDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar dateTimeCal = Calendar.getInstance();
        String dateText = edtDate.getText().toString();
        String timeText = edtTime.getText().toString();
        String finalDate = dateText + " " + timeText;
        try {
            dateTimeCal.setTime(dateFormat.parse(finalDate));
            if (dateTimeCal.compareTo(calendar) > 0) {
                saveDataShared();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorDate), Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean nameTaskIsEmpty() {
        if (this.edtNameTask.getText().toString().matches("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean descriptionTaskIsEmpty() {
        if (this.edtDescriptionTask.getText().toString().matches("")) {
            return true;
        } else {
            return false;
        }
    }

    private void loadDataSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String listTask = sharedPref.getString("sharedTask", null);
        Gson gson = new Gson();
        this.tasks = gson.fromJson(listTask, new TypeToken<ArrayList<Task>>() {
        }.getType());
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
    }

    private void saveDataShared() {
        loadDataSharedPreferences();
        Task task = new Task(edtNameTask.getText().toString(), edtDescriptionTask.getText().toString(), edtDate.getText().toString(), edtTime.getText().toString());
        this.tasks.add(task);
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.tasks);
        prefsEditor.putString("sharedTask", json);
        prefsEditor.apply();
        Intent intent = new Intent(this, MenuActivity.class);
        this.finish();
        startActivity(intent);
    }
}