package com.example.recyclerview.controller.activities;

import android.os.Bundle;

import com.example.recyclerview.R;

public class PlannedTaskActivity extends TaskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_task);
        txtTitleView.setText(R.string.planned);
        setTypeTask(0);
        cleanAdapter();
        loadDataSharedPreferences();
    }
    @Override
    protected void loadDataSharedPreferences() {
        super.loadDataSharedPreferences();
        sortTask();
    }


}
