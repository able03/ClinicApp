package com.example.clinicapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.ScheduleAdapter;
import com.example.clinicapp.models.ScheduleModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DoctorMyScheduleFragment extends Fragment implements IDefault
{

    private DBHelper dbHelper;
    private RecyclerView rv;
    private List<ScheduleModel> scheduleModelList;
    private ScheduleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_doctor_my_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        setRV();
    }


    private void setRV()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        scheduleModelList.addAll(dbHelper.getBookedSchedulesList(id));
        adapter.setScheduleModelList(scheduleModelList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void initValues()
    {
        dbHelper = new DBHelper(getContext());
        rv = getView().findViewById(R.id.rv);
        adapter = new ScheduleAdapter(false);
        scheduleModelList = new ArrayList<>();
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