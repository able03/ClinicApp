package com.example.clinicapp.doctor.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.AppointmentAdapter;
import com.example.clinicapp.adapters.ScheduleAdapter;
import com.example.clinicapp.models.AppointmentModel;
import com.example.clinicapp.models.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorMyAppointmentsActivity extends AppCompatActivity implements IDefault
{

    private DBHelper dbHelper;
    private RecyclerView rv, rv_appointements;
    private List<ScheduleModel> scheduleModelList;
    private List<AppointmentModel> appointmentModelList;
    private AppointmentAdapter appointmentAdapter;
    private ScheduleAdapter adapter;
    private int doc_id, sched_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_appointments);
        initValues();
        setStr();
        setRV();

        adapter.setListener(model ->
        {

            setAppointments(doc_id, model.getScheduleID());

        });
    }

    private void setRV()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        scheduleModelList.addAll(dbHelper.getBookedSchedulesList(id));
        adapter.setScheduleModelList(scheduleModelList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void initValues()
    {
        dbHelper = new DBHelper(this);
        rv = findViewById(R.id.rv);
        adapter = new ScheduleAdapter(true);
        scheduleModelList = new ArrayList<>();
        rv_appointements = findViewById(R.id.rvAppointements);

        appointmentModelList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter();

    }

    private void setAppointments(int doc_id, int sched_id)
    {
        appointmentModelList.clear();

        appointmentModelList.addAll(dbHelper.getAppointmentsByDoctorAndSchedule(doc_id, sched_id));
        appointmentAdapter.setAppointmentModelList(appointmentModelList);
        rv_appointements.setAdapter(appointmentAdapter);
        rv_appointements.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void setListeners()
    {

    }

    @Override
    public void setStr()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        doc_id = sharedPreferences.getInt("id", -1);
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