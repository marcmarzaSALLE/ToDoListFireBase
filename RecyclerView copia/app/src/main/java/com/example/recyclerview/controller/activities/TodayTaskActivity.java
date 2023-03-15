package com.example.recyclerview.controller.activities;

import android.os.Bundle;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.activities.TaskActivity;

public class TodayTaskActivity extends TaskActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_task);
        txtTitleView.setText(R.string.forToday);
        setTypeTask(1);
        cleanAdapter();
        loadDataSharedPreferences();
    }
    @Override
    protected void loadDataSharedPreferences() {
        super.loadDataSharedPreferences();
        sortTask();
    }
}