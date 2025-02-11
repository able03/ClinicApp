package com.example.clinicapp.patient.activities;

import android.os.Bundle;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity implements IDefault
{

    private DBHelper db;
    private List<ScheduleModel> scheduleModelList;
    private ScheduleAdapter adapter;
    private RecyclerView rv;
    private Credentials credentials;
    private TextView tv_name, tv_spec, tv_date;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        initValues();
        setData();
    }

    @Override
    public void initValues()
    {
        rv = findViewById(R.id.rv);
        tv_name = findViewById(R.id.tvName);
        tv_spec = findViewById(R.id.tvSpec);
        tv_date = findViewById(R.id.tvDate);
        db = new DBHelper(this);
        credentials = new Credentials(this);
        scheduleModelList = new ArrayList<>();
        adapter = new ScheduleAdapter();


        int doctor_id = DoctorStaticModel.getDoctor_id();
        scheduleModelList.addAll(db.getSchedulesList(doctor_id));
        adapter.setScheduleModelList(scheduleModelList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        adapter.setListener(model -> {
            tv_date.setText(model.getDate());
        });
    }

    private void setData()
    {
        tv_spec.setText(DoctorStaticModel.getSpecialization());
        tv_name.setText(DoctorStaticModel.getName());
    }

    @Override
    public void setListeners()
    {

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