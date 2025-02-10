package com.example.clinicapp.patient.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clinicapp.Credentials;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class PatientDashboardActivity extends AppCompatActivity implements IDefault
{

    private MaterialButton btn_logout;
    private Credentials credentials;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        initValues();
        setListeners();

        btn_logout.setVisibility(View.VISIBLE);
        TextView tv = findViewById(R.id.tv);
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("isLoggedIn", false))
        {
            btn_logout.setVisibility(View.GONE);
            tv.setText("Welcome Guest!");
        }
    }

    @Override
    public void initValues()
    {
        btn_logout = findViewById(R.id.btnLogout);
        credentials = new Credentials(this);
    }

    @Override
    public void setListeners()
    {
        btn_logout.setOnClickListener(logout -> {
            credentials.logout();
            startActivity(new Intent(PatientDashboardActivity.this, LoginActivity.class));
            finish();
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