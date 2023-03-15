package com.example.recyclerview.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.fragment.DatePickerFragment;
import com.example.recyclerview.controller.fragment.TimePickerFragment;
import com.example.recyclerview.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskInfoActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText edtName, edtDescription, edtDate, edtTime;
    Button btnEdit;
    Task taskInfo;
    private boolean enableEdit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        syncronizedWidget();
        setTextWidget();
        this.enableEdit=true;
        getInfoTask();
        disableWidget();
        this.btnEdit.setOnClickListener(v -> {

            if (this.enableEdit) {
                enableWidget();
                setTextWidget();
                this.enableEdit = false;
            } else {
                if(checkData()){
                    setTextWidget();
                    disableWidget();
                    this.enableEdit=true;
                    saveShared();
                    Intent intent = this.getIntent();
                    intent.putExtra("taskUpdate",taskInfo);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                    //recoger datos, volver al Task Activity
                }
            }
        });
        edtDate.setOnClickListener(v -> {
            showDateDialog();
        });
        edtTime.setOnClickListener(v -> {
            showTimeDialog();
        });
    }

    private void syncronizedWidget() {
        this.txtTitle = (TextView) findViewById(R.id.txtTitleActivity);
        this.edtName = (EditText) findViewById(R.id.edtTextTitleTask);
        this.edtDescription = (EditText) findViewById(R.id.edtDescriptionTask);
        this.edtDate = (EditText) findViewById(R.id.edtDate);
        this.edtTime = (EditText) findViewById(R.id.edtTime);
        this.btnEdit = (Button) findViewById(R.id.btnSaveTask);
    }

    private void disableWidget() {
        this.edtName.setEnabled(false);
        this.edtDescription.setEnabled(false);
        this.edtDate.setEnabled(false);
        this.edtTime.setEnabled(false);
    }

    private void enableWidget() {
        this.edtName.setEnabled(true);
        this.edtDescription.setEnabled(true);
        this.edtDate.setEnabled(true);
        this.edtTime.setEnabled(true);
    }

    private void setTextWidget() {
        this.txtTitle.setText(getResources().getString(R.string.infoTask));
        if(this.enableEdit){
            this.btnEdit.setText(getResources().getString(R.string.saveEditTask));
        }else{
            this.btnEdit.setText(getResources().getString(R.string.editTask));
        }

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

    private void getInfoTask() {
        taskInfo = (Task) getIntent().getSerializableExtra("taskInfo");
        this.edtName.setText(taskInfo.getNameTask());
        this.edtDescription.setText(taskInfo.getDescriptionTask());
        this.edtDate.setText(taskInfo.getDate());
        this.edtTime.setText(taskInfo.getTime());
    }

    private boolean checkData() {
        boolean correct=false;
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
                   correct=checkDateTime();
                }
            }

        }
        return correct;
    }
    private boolean checkDateTime() {
        boolean correct=false;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar dateTimeCal = Calendar.getInstance();
        String dateText = edtDate.getText().toString();
        String timeText = edtTime.getText().toString();
        String finalDate = dateText + " " + timeText;
        try {
            dateTimeCal.setTime(dateFormat.parse(finalDate));
            if (dateTimeCal.compareTo(calendar) > 0) {
                correct=true;
                saveShared();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorDate), Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return correct;
    }
    private boolean nameTaskIsEmpty() {
        if (this.edtName.getText().toString().matches("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean descriptionTaskIsEmpty() {
        if (this.edtDescription.getText().toString().matches("")) {
            return true;
        } else {
            return false;
        }
    }
    private void saveShared(){
        taskInfo.setNameTask(edtName.getText().toString());
        taskInfo.setDescriptionTask(edtDescription.getText().toString());
        taskInfo.setDate(edtDate.getText().toString());
        taskInfo.setTime(edtTime.getText().toString());
    }

}