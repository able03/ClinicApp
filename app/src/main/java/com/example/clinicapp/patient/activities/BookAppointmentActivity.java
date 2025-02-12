package com.example.clinicapp.patient.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.Credentials;
import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.ScheduleAdapter;
import com.example.clinicapp.models.ScheduleModel;
import com.example.clinicapp.staticmodels.DoctorStaticModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookAppointmentActivity extends AppCompatActivity implements IDefault
{

    private DBHelper db;
    private List<ScheduleModel> scheduleModelList;
    private ScheduleAdapter adapter;
    private RecyclerView rv;
    private TextView tv_name, tv_spec, tv_date, tv_purpose_lbl;
    private MaterialButton btn_submit;
    private ChipGroup cg;
    private String date, time, purpose;
    private TextInputEditText et_purpose;

    private int sched_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        initValues();
        setData();
        setListeners();

        SharedPreferences  sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        et_purpose.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.VISIBLE);
        tv_purpose_lbl.setVisibility(View.VISIBLE);

        if(!isLoggedIn)
        {
            et_purpose.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            tv_purpose_lbl.setVisibility(View.GONE);
        }
    }

    @Override
    public void initValues()
    {
        rv = findViewById(R.id.rv);
        tv_name = findViewById(R.id.tvName);
        tv_spec = findViewById(R.id.tvSpec);
        tv_date = findViewById(R.id.tvDate);
        db = new DBHelper(this);
        scheduleModelList = new ArrayList<>();
        adapter = new ScheduleAdapter(true);
        btn_submit = findViewById(R.id.btnSubmit);
        cg = findViewById(R.id.cg);
        tv_purpose_lbl = findViewById(R.id.tvPurposeLbl);

        et_purpose = findViewById(R.id.etPurpose);

        int doctor_id = DoctorStaticModel.getDoctor_id();
        scheduleModelList.addAll(db.getSchedulesList(doctor_id));
        adapter.setScheduleModelList(scheduleModelList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        adapter.setListener(model -> {
            tv_date.setText(model.getDate());
            sched_id = model.getScheduleID();
            addChipsToGroup();
        });

        addChipsToGroup();
    }

    private void setData()
    {
        tv_spec.setText(DoctorStaticModel.getSpecialization());
        tv_name.setText(DoctorStaticModel.getName());
    }

    @Override
    public void setListeners()
    {
        btn_submit.setOnClickListener(submit -> {
            setStr();
            if(et_purpose.getText().toString().isEmpty() || time == null || date == null)
            {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
            else
            {
                bookingProcess();
            }
        });

        cg.setOnCheckedStateChangeListener((group, checkedIds) ->
        {
            for(Integer id : checkedIds)
            {
                Chip selectedChip = findViewById(id);
                time = selectedChip.getText().toString();
            }
        });
    }

    private void addChipsToGroup()
    {
        cg.removeAllViews();
        List<String> timeSlots = new ArrayList<>();
        int doctorId = DoctorStaticModel.getDoctor_id();

        setStr();
        List<String> bookedSlots = db.getBookedTimeSlots(doctorId, sched_id, date);

        for (int hour = 8; hour < 18; hour++) {
            String period = (hour < 12) ? "AM" : "PM";
            int displayHour = (hour > 12) ? (hour - 12) : hour;

            String d = displayHour + ":00 " + period + " - " + displayHour + ":30 " + period;

            if (bookedSlots.contains(d)) continue;

            timeSlots.add(d);
        }


        for (String time : timeSlots) {
            Chip chip = new Chip(this);
            chip.setText(time);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setTextColor(getResources().getColor(R.color.black));

            cg.addView(chip);
        }
    }


    @Override
    public void setStr()
    {
        date = tv_date.getText().toString();
        purpose = Objects.requireNonNull(et_purpose.getText()).toString();
    }

    @Override
    public void clearFields()
    {

    }

    @Override
    public void setFragment(Fragment fragment)
    {

    }

    private void bookingProcess()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        int patient_id = sharedPreferences.getInt("id", -1);
        int doctor_id = DoctorStaticModel.getDoctor_id();
        String status = "pending";

        int schedule_id = sched_id;

        if(db.createAppointment(doctor_id, patient_id, status, date, time, schedule_id, purpose))
            Toast.makeText(this, "Booking success", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();


    }

}