package com.example.recyclerview.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.adapter.ListTaskAdapter;
import com.example.recyclerview.model.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {
    protected ArrayList<Task> allTask;
    protected ArrayList<Task> plannedTask;

    protected ArrayList<Task> taskToday;
    protected TextView txtTitleView;

    private ListTaskAdapter menuAdapter;

    protected int typeTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_task);
        this.allTask = new ArrayList<>();
         txtTitleView = findViewById(R.id.txtTitle);
        loadDataSharedPreferences();

    }

    protected void cleanAdapter(){
        if(typeTask == 0){
            if(!this.taskToday.isEmpty()){
                this.taskToday.clear();
                menuAdapter.notifyDataSetChanged();
            }
        }else if(typeTask == 1){
            if(!this.plannedTask.isEmpty()){
                this.plannedTask.clear();
                menuAdapter.notifyDataSetChanged();
            }
        }
    }
    protected void addDataAdapter(ArrayList<Task>showTasks) {
        menuAdapter = new ListTaskAdapter(typeTask,showTasks, this,this ,new ListTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                startNewIntent(task);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTask);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(menuAdapter);
    }





    protected void sortTask() {
        this.allTask.sort(new Comparator<Task>() {
            final DateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            @Override
            public int compare(Task t1, Task t2) {
                try {
                    Date dateTime1 = f.parse(t1.getDate() + " " + t1.getTime());
                    Date dateTime2 = f.parse(t2.getDate() + " " + t2.getTime());
                    t1.setDateTime(dateTime1);
                    t2.setDateTime(dateTime2);
                    assert dateTime1 != null;
                    return dateTime1.compareTo(dateTime2);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(today);
        this.taskToday = new ArrayList<>();
        this.plannedTask = new ArrayList<>();
        for (Task t : this.allTask) {
            try {
                Date dateTask =sdf.parse(t.getDate());
                if(typeTask==0){
                    if (dateTask.after(today)) {
                        this.plannedTask.add(t);
                    }
                }else if(typeTask==1){
                    if (t.getDate().equals(currentDate)) {
                        this.taskToday.add(t);
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        if(typeTask==0){
            addDataAdapter(this.plannedTask);
        }else if(typeTask==1){
            addDataAdapter(this.taskToday);
        }
    }

    protected void loadDataSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String listTask = sharedPref.getString("sharedTask", null);
        Gson gson = new Gson();
        this.allTask = gson.fromJson(listTask, new TypeToken<ArrayList<Task>>() {
        }.getType());
    }

    public void setTypeTask(int typeTask) {
        this.typeTask = typeTask;
    }

    private void startNewIntent(Task task){
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("taskInfo",task);
        getIntentTaskUpdate.launch(intent);
    }

    ActivityResultLauncher<Intent> getIntentTaskUpdate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Task taskInfo = (Task) result.getData().getSerializableExtra("taskUpdate");
                        Intent intentUpdate = getIntent();
                        intentUpdate.putExtra("taskUpdateShared",taskInfo);
                        Log.d("TASK",taskInfo.getNameTask());
                        setResult(Activity.RESULT_FIRST_USER,intentUpdate);
                        finish();
                    }
                }
            });

}
