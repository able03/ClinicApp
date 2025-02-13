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
import android.widget.Toast;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.R;
import com.example.clinicapp.adapters.TransactionAdapter;
import com.example.clinicapp.models.AppointmentModel;
import com.example.clinicapp.staticmodels.DoctorStaticModel;

import java.util.ArrayList;
import java.util.List;


public class TransactionFragment extends Fragment
{
    private RecyclerView rv;
    private TransactionAdapter adapter;
    private List<AppointmentModel> appointmentModelList;
    private DBHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv);
        db = new DBHelper(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);

        appointmentModelList = new ArrayList<>();
        appointmentModelList.addAll(db.getConfirmedAppointments(id));

        adapter = new TransactionAdapter();
        adapter.setTransactionModelList(appointmentModelList, getContext());

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}