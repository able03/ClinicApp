package com.example.clinicapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.DoctorAdapter;
import com.example.clinicapp.models.DoctorModel;

import java.util.ArrayList;
import java.util.List;

public class GuestActivity extends AppCompatActivity implements IDefault
{

    private RecyclerView rv;
    private List<DoctorModel> doctorModelList;
    private DoctorAdapter adapter;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        initValues();
        setListeners();
    }

    @Override
    public void initValues()
    {
        rv = findViewById(R.id.rv);
        doctorModelList = new ArrayList<>();
        adapter = new DoctorAdapter();
        db = new DBHelper(this);
    }

    @Override
    public void setListeners()
    {
        doctorModelList.addAll(db.getDoctorsList());
        adapter.setDoctorModels(doctorModelList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
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