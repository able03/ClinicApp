package com.example.clinicapp.patient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.Credentials;
import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.activities.LoginActivity;
import com.example.clinicapp.adapters.DoctorAdapter;
import com.example.clinicapp.models.DoctorModel;
import com.example.clinicapp.staticmodels.DoctorStaticModel;

import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity implements IDefault
{

    private Credentials credentials;
    private Toolbar toolbar;
    private RecyclerView rv;
    private List<DoctorModel> doctorModelList;
    private DoctorAdapter adapter;
    private DBHelper db;
    private TextView tv_name;

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");

            Toast.makeText(context, title + ": " + message, Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        initValues();
        setListeners();
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);

        tv_name.setText(name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            registerReceiver(notificationReceiver, new IntentFilter("com.example.clinicapp.NOTIFICATION"), Context.RECEIVER_NOT_EXPORTED);
        }
    }


    @Override
    public void initValues()
    {
        credentials = new Credentials(this);
        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        doctorModelList = new ArrayList<>();
        adapter = new DoctorAdapter();
        db = new DBHelper(this);
        tv_name = findViewById(R.id.tvName);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_patient_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.menu_logout)
        {
            credentials.logout();
            startActivity(new Intent(PatientDashboardActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

}