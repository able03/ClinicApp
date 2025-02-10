package com.example.clinicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.IDefault;
import com.example.clinicapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity implements IDefault
{

    private CheckBox cb_terms;
    private TextView tv_login;
    private MaterialButton btn_register;
    private TextInputEditText et_name, et_uname, et_pass, et_cpass;
    private String name, uname, pass, cpass;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initValues();
        setListeners();


    }

    @Override
    public void initValues()
    {
        cb_terms = findViewById(R.id.cbTerms);

        tv_login = findViewById(R.id.tvLogin);
        btn_register = findViewById(R.id.btnRegister);

        et_name = findViewById(R.id.etName);
        et_uname = findViewById(R.id.etUsername);
        et_pass = findViewById(R.id.etPassword);
        et_cpass = findViewById(R.id.etCPassword);

        btn_register.setEnabled(false);

        db = new DBHelper(this);
    }

    @Override
    public void setListeners()
    {
        cb_terms.setOnCheckedChangeListener((buttonView, isChecked) ->
                btn_register.setEnabled(isChecked));

        tv_login.setOnClickListener(login -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btn_register.setOnClickListener(register -> {
            setStr();
            registerProcess();
        });
    }

    @Override
    public void setStr()
    {
        name = Objects.requireNonNull(et_name.getText()).toString().trim();
        uname = Objects.requireNonNull(et_uname.getText()).toString().trim();
        pass = Objects.requireNonNull(et_pass.getText()).toString().trim();
        cpass = Objects.requireNonNull(et_cpass.getText()).toString().trim();
    }

    @Override
    public void clearFields()
    {
        et_name.setText("");
        et_uname.setText("");
        et_pass.setText("");
        et_cpass.setText("");
    }

    @Override
    public void setFragment(Fragment fragment)
    {

    }

    private void registerProcess()
    {
        if( !name.isEmpty() && !uname.isEmpty() && !pass.isEmpty() && !cpass.isEmpty())
        {
            if(pass.equals(cpass))
            {
                executor.execute(() -> {
                    if(db.createPatient(name, uname, pass))
                    {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                            clearFields();
                        });
                    }
                    else
                    {
                        runOnUiThread(() ->
                                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show());
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}