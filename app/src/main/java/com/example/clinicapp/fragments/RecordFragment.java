package com.example.clinicapp.fragments;

import android.content.Context;
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
import com.example.clinicapp.adapters.AppointmentAdapter;
import com.example.clinicapp.models.AppointmentModel;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment implements IDefault
{

    private ChipGroup cg;
    private RecyclerView rv;
    private DBHelper db;
    private AppointmentAdapter adapter;
    private List<AppointmentModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        setListeners();
    }

    @Override
    public void initValues()
    {

        cg = getView().findViewById(R.id.cg);
        rv = getView().findViewById(R.id.rv);
        db = new DBHelper(getContext());
        list = new ArrayList<>();
        adapter = new AppointmentAdapter();

    }

    private void setRV(ArrayList<AppointmentModel> model)
    {
        list.clear();
        list.addAll(model);
        adapter.setAppointmentModelList(list);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();

    }


    @Override
    public void setListeners()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        int doctor_id = sharedPreferences.getInt("id", 0);
        setRV(db.getAppointementsList(doctor_id));

        cg.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int selectedId = checkedIds.get(0);
            if (selectedId == R.id.all) setRV(db.getAppointementsList(doctor_id));
            else if (selectedId == R.id.confirmed) setRV(db.getConfirmedAppointments(doctor_id));
            else if (selectedId == R.id.cancelled) setRV(db.getCancelledAppointments(doctor_id));
            else if (selectedId == R.id.pending) setRV(db.getPendingAppointmentsByDoctor(doctor_id));
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
}