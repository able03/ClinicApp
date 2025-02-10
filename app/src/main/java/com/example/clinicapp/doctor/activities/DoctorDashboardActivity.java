package com.example.clinicapp.doctor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.Credentials;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.OnItemClickListener;
import com.example.clinicapp.R;
import com.example.clinicapp.activities.LoginActivity;
import com.example.clinicapp.adapters.CalendarAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DoctorDashboardActivity extends AppCompatActivity implements IDefault
{

    private Credentials credentials;
    private Toolbar toolbar;
    private FloatingActionButton fab_add_sched;
    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        initValues();
        setListeners();

        setSupportActionBar(toolbar);

    }

    @Override
    public void initValues()
    {
        credentials = new Credentials(this);
        toolbar = findViewById(R.id.toolbar);
        fab_add_sched = findViewById(R.id.fabAddSchedule);
        calendarView = findViewById(R.id.calendar);
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());


    }



    @Override
    public void setListeners()
    {
        fab_add_sched.setOnClickListener(add_sched -> {
            startActivity(new Intent(DoctorDashboardActivity.this, AddScheduleActivity.class));
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_doctor_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.menu_logout)
        {
            credentials.logout();
            startActivity(new Intent(DoctorDashboardActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}