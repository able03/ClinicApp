package com.example.clinicapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clinicapp.Credentials;
import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.example.clinicapp.doctor.activities.DoctorDashboardActivity;
import com.example.clinicapp.patient.activities.PatientDashboardActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity implements IDefault
{

    private DBHelper db;
    private TextView tv_signup;
    private String uname, pass;
    private TextInputEditText et_uname, et_pass;
    private MaterialButton btn_login, btn_guest;
    private Credentials credentials;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initValues();
        executor.execute(() -> db.embedDoctors());
        checkAutoLogin();
        setListeners();

    }

    @Override
    public void initValues()
    {
        db = new DBHelper(this);
        tv_signup = findViewById(R.id.tvSignup);
        et_uname = findViewById(R.id.etUsername);
        et_pass = findViewById(R.id.etPassword);
        btn_login = findViewById(R.id.btnLogin);
        credentials = new Credentials(this);
        btn_guest = findViewById(R.id.btnGuest);
    }

    @Override
    public void setListeners()
    {
        tv_signup.setOnClickListener(signup -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btn_login.setOnClickListener(login -> {
            setStr();
            loginProcess();
        });

        btn_guest.setOnClickListener(guest -> startActivity(new Intent(LoginActivity.this, PatientDashboardActivity.class)));
    }

    @Override
    public void setStr()
    {
        uname = Objects.requireNonNull(et_uname.getText()).toString().trim();
        pass = Objects.requireNonNull(et_pass.getText()).toString().trim();
    }

    @Override
    public void clearFields()
    {
        et_uname.setText("");
        et_pass.setText("");
    }

    @Override
    public void setFragment(Fragment fragment)
    {

    }

    private void loginProcess()
    {
        if(!uname.isEmpty() && !pass.isEmpty())
        {
            Cursor cursor = db.getUser("doctor", uname, pass);

            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));
                String uname = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                credentials.setCredentials(id, name, spec, uname, pass, true);
                Intent intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
                startActivity(intent);
            }
            else
            {
                Cursor patient = db.getUser("patient", uname, pass);
                if(patient.getCount() > 0)
                {
                    patient.moveToFirst();
                    int id = patient.getInt(patient.getColumnIndexOrThrow("patient_id"));
                    String name = patient.getString(patient.getColumnIndexOrThrow("name"));
                    String uname = patient.getString(patient.getColumnIndexOrThrow("username"));
                    String pass = patient.getString(patient.getColumnIndexOrThrow("password"));
                    credentials.setCredentials(id, name, null, uname, pass, true);
                    Intent intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }

                patient.close();
            }
            cursor.close();

        }
        else
        {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAutoLogin()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
        String user = sharedPreferences.getString("username", null);
        String pass = sharedPreferences.getString("password", null);

        if(user != null && pass != null)
        {
            Cursor cursor = db.getUser("doctor", user, pass);
            if(cursor.moveToFirst())
            {
                Intent intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
                startActivity(intent);
            }
            else
            {
                Cursor patient = db.getUser("patient", user, pass);
                if(patient.moveToFirst())
                {
                    Intent intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
                    startActivity(intent);
                }
                patient.close();
            }

            cursor.close();
        }
    }



}