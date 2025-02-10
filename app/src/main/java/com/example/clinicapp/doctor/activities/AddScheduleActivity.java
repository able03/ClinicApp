package com.example.clinicapp.doctor.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.CalendarAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity implements IDefault
{

    private CalendarView calendarView;

    private RecyclerView recyclerView;
    private TextView tvMonth, tvSelectedDate;
    private Calendar calendar;
    private ArrayList<String> daysList;
    private CalendarAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        initValues();
        setListeners();
    }

    @Override
    public void initValues()
    {
        calendarView = findViewById(R.id.calendar);

        tvMonth = findViewById(R.id.tv_month);
        recyclerView = findViewById(R.id.recyclerView);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        calendar = Calendar.getInstance();
        daysList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 7 columns for days of the week
        loadCalendar();

        adapter = new CalendarAdapter(daysList, day -> tvSelectedDate.setText("Selected Date: " + day + " " + new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.getTime())));

        recyclerView.setAdapter(adapter);
    }

    private void loadCalendar() {
        daysList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonth.setText(sdf.format(calendar.getTime()));

        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysList.add("");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            daysList.add(String.valueOf(i));
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setListeners()
    {
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
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