package com.example.clinicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity implements IDefault
{
    private CheckBox cb_terms;
    private TextView tv_login;
    private MaterialButton btn_register;
    private TextInputEditText et_name, et_uname, et_pass, et_cpass;
    private TextInputLayout lo_name, lo_uname, lo_pass, lo_cpass;
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

        lo_name = findViewById(R.id.loName);
        lo_uname = findViewById(R.id.loUsername);
        lo_pass = findViewById(R.id.loPassword);
        lo_cpass = findViewById(R.id.loCPassword);

        btn_register.setEnabled(false);
        db = new DBHelper(this);
    }

    @Override
    public void setListeners()
    {
        cb_terms.setOnCheckedChangeListener((buttonView, isChecked) ->
                btn_register.setEnabled(isChecked)
        );

        tv_login.setOnClickListener(login -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btn_register.setOnClickListener(register -> {
            setStr();
            if (validateFields()) {
                registerProcess();
            }
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

    private boolean validateFields()
    {
        boolean isValid = true;
        if (name.isEmpty() || name.length() < 3) {
            lo_name.setError("Full name must be at least 3 characters");
            et_name.addTextChangedListener(getTextWatcher(lo_name));
            isValid = false;
        }
        if (uname.isEmpty() || uname.length() < 4) {
            lo_uname.setError("Username must be at least 4 characters");
            et_uname.addTextChangedListener(getTextWatcher(lo_uname));
            isValid = false;
        }
        if (pass.isEmpty() || pass.length() < 6) {
            lo_pass.setError("Password must be at least 6 characters");
            et_pass.addTextChangedListener(getTextWatcher(lo_pass));
            isValid = false;
        }
        if (!pass.equals(cpass)) {
            lo_cpass.setError("Passwords do not match");
            et_cpass.addTextChangedListener(getTextWatcher(lo_cpass));
            isValid = false;
        }
        if (!cb_terms.isChecked()) {
            Toast.makeText(this, "You must accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    private TextWatcher getTextWatcher(TextInputLayout layout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                layout.setErrorEnabled(false);
            }
        };
    }


    private void registerProcess()
    {
        executor.execute(() -> {
            if (db.createPatient(name, uname, pass))
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
}
