package com.example.recyclerview.controller.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.adapter.ListMenuAdapter;
import com.example.recyclerview.model.MenuTask;
import com.example.recyclerview.model.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    List<MenuTask> typeTasks;
    private ArrayList<Task> tasks;
    private int numTaskToday, numTaskPlanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        loadDataSharedPreferences();
        getNumberTaskToday();
        addData();
    }

    private void addData() {
        typeTasks = new ArrayList<>();
        typeTasks.add(new MenuTask(1, R.drawable.icon_today, "PARA HOY", this.numTaskToday + " tareas"));
        typeTasks.add(new MenuTask(2, R.drawable.icon_planned, "PROGRAMADAS", this.numTaskPlanned + " tareas"));
        typeTasks.add(new MenuTask(3, R.drawable.icon_add, "NUEVA TAREA", "Crea una nueva tarea"));
        ListMenuAdapter menuAdapter = new ListMenuAdapter(typeTasks, this, new ListMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuTask menuTask) {
                startIntent(menuTask);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(menuAdapter);

    }

    private void startIntent(MenuTask menuTask) {
        switch (menuTask.getId()) {
            case 1:
                if (this.numTaskToday == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noTaskRegister), Toast.LENGTH_SHORT).show();
                } else {
                    startNewIntentToday();
                }

                break;
            case 2:
                if (this.numTaskPlanned == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noTaskRegister), Toast.LENGTH_SHORT).show();
                } else {
                    startNewIntentPlanned();

                }
                break;
            case 3:
                Intent intent = new Intent(this, AddTaskActivity.class);
                startActivity(intent);
                break;
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
            this.numTaskPlanned = 0;
            this.numTaskToday = 0;
        }
    }

    private void getNumberTaskToday() {
        int counterToday = 0, counterPlanned = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Task t : this.tasks) {
            Calendar dateTimeCal = Calendar.getInstance();
            String dateText = t.getDate();
            String timeText = t.getTime();
            String finalDate = dateText + " " + timeText;
            try {
                dateTimeCal.setTime(dateFormat.parse(finalDate));
                if (today.get(Calendar.YEAR) == dateTimeCal.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == dateTimeCal.get(Calendar.DAY_OF_YEAR)) {
                    counterToday++;
                } else if (today.get(Calendar.YEAR) == dateTimeCal.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) < dateTimeCal.get(Calendar.DAY_OF_YEAR)) {
                    counterPlanned++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.numTaskToday = counterToday;
        this.numTaskPlanned = counterPlanned;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }

    public void startNewIntentToday() {
        Intent intent = new Intent(this, TodayTaskActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    public void startNewIntentPlanned() {
        Intent intent = new Intent(this, PlannedTaskActivity.class);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String id = data.getStringExtra("idTask");
                        Log.d("TAREAAAA", id);
                        deleteTask(id);
                    }else if (result.getResultCode()==Activity.RESULT_FIRST_USER){
                        Intent data = result.getData();
                        assert data != null;
                        Task taskUpdate = (Task) data.getSerializableExtra("taskUpdateShared");
                        updateTaskSharedPreferences(taskUpdate);
                    }
                }
            });
    private void deleteTask(String id){
        ArrayList<Task>auxTask = (ArrayList<Task>) this.tasks.clone();
        for(int i=0; i<this.tasks.size(); i++){
            if(this.tasks.get(i).getId().equals(id)){
                auxTask.remove(i);
                break;
            }
        }
        this.tasks.clear();
        this.tasks.addAll(auxTask);

        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.tasks);
        prefsEditor.putString("sharedTask", json);
        prefsEditor.apply();
        getNumberTaskToday();
        addData();

    }

    private void updateTaskSharedPreferences(Task taskUpdate){
        for (Task t: this.tasks){
            if(t.getId().equals(taskUpdate.getId())){
                t.setNameTask(taskUpdate.getNameTask());
                t.setDescriptionTask(taskUpdate.getDescriptionTask());
                t.setDate(taskUpdate.getDate());
                t.setTime(taskUpdate.getTime());
            }
        }
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.tasks);
        prefsEditor.putString("sharedTask", json);
        prefsEditor.apply();
        getNumberTaskToday();
        addData();

    }
}