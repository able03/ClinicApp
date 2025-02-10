package com.example.clinicapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class Credentials
{
    private SharedPreferences sharedPreferences;

    public Credentials(Activity activity)
    {
        sharedPreferences = activity.getSharedPreferences("user", MODE_PRIVATE);
    }

    public void setCredentials(int id, String name, String specialization, String username, String password, boolean isLoggedIn)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", id);
        editor.putString("name", name);
        editor.putString("specialization", specialization);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public void logout()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
