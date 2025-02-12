package com.example.clinicapp.doctor.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.ScheduleAdapter;
import com.example.clinicapp.models.ScheduleModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity implements IDefault
{

    private CalendarView calendarView;
    private DBHelper dbHelper;
    private String selectedDate;
    private MaterialButton btn_save;
    private RecyclerView rv;
    private List<ScheduleModel> scheduleModelList;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        initValues();
        setListeners();
        setRV();

        adapter.setListener(model ->
        {
            Toast.makeText(this, model.getDate(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void initValues()
    {
        calendarView = findViewById(R.id.calendar);
        dbHelper = new DBHelper(this);
        btn_save = findViewById(R.id.btnConfirm);
        rv = findViewById(R.id.rv);
        adapter = new ScheduleAdapter(true);
        scheduleModelList = new ArrayList<>();

    }

    private void setRV()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        scheduleModelList.addAll(dbHelper.getSchedulesList(id));
        adapter.setScheduleModelList(scheduleModelList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }


    @Override
    public void setListeners()
    {
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());

        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> selectedDate = formatDate(i, i1, i2));

        btn_save.setOnClickListener(save -> saveSchedule());
    }

    private void saveSchedule()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        boolean isSuccess = dbHelper.createSchedule(id, selectedDate);
        if(isSuccess) Toast.makeText(this, "Schedule set", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Schedule failed", Toast.LENGTH_SHORT).show();
    }


    private String formatDate(int year, int month, int day) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
    }

    @Override
    public void setStr()
    {

    }

    @Override
    public void clearFields()
    {

    }

    @Override
    public void setFragment(Fragment fragment)
    {

    }
}