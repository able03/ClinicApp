package com.example.clinicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.clinicapp.models.DoctorModel;
import com.example.clinicapp.models.ScheduleModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(@Nullable Context context)
    {
        super(context, "clinic.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE doctors(doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, specialization TEXT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE patients(patient_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE appointments(appointment_id INTEGER PRIMARY KEY AUTOINCREMENT, doctor_id INTEGER, status TEXT, patient_id INTEGER, date TEXT, time TEXT)");
        db.execSQL("CREATE TABLE medical_records(record_id INTEGER PRIMARY KEY AUTOINCREMENT, patient_id INTEGER, date TEXT)");
        db.execSQL("CREATE TABLE schedules(schedule_id INTEGER PRIMARY KEY AUTOINCREMENT, doctor_id INTEGER, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS doctors");
        db.execSQL("DROP TABLE IF EXISTS patients");
        db.execSQL("DROP TABLE IF EXISTS appointments");
        db.execSQL("DROP TABLE IF EXISTS medical_records");
        onCreate(db);
    }

    public boolean createDoctor(String name, String specialization, String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("specialization", specialization);
        values.put("username", username);
        values.put("password", password);

        return db.insert("doctors", null, values) != -1;
    }

    public boolean createPatient(String name, String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("username", username);
        values.put("password", password);

        return db.insert("patients", null, values) != -1;
    }

    public boolean isDoctorTableEmpty()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM doctors";
        return db.rawQuery(query, null).getCount() == 0;

    }

    public void embedDoctors()
    {
        if(isDoctorTableEmpty())
        {
            createDoctor("Dr. John Doe", "Cardiologist", "johndoe", "Mypassword@1");
            createDoctor("Dr. Jane Smith", "Dermatologist", "janesmith", "Mypassword@2");
            createDoctor("Dr. Julia Wanton", "Dermatologist", "juliawanton", "Mypassword@4");
        }
    }

    public Cursor getUser(String type, String uname, String pass)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "username = ? AND password = ?";
        String selectionArgs[] = {uname, pass};

        switch (type)
        {
            case "doctor":
                return db.query("doctors", null, selection, selectionArgs,
                        null, null, null);
            case "patient":
                return db.query("patients", null, selection, selectionArgs,
                        null, null, null);
        }

        return null;
    }

    public boolean createSchedule(int doctor_id, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("doctor_id", doctor_id);
        values.put("date", date);

        return db.insert("schedules", null, values) != -1;
    }



    public Cursor getSchedules(int doctor_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "doctor_id = ?";
        String selectionArgs[] = {String.valueOf(doctor_id)};

        return db.query("schedules", null, selection, selectionArgs,
                null, null, null);

    }

    public ArrayList<ScheduleModel> getSchedulesList(int doctor_id)
    {
        Cursor cursor = getSchedules(doctor_id);

        ArrayList<ScheduleModel> tempList = new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                int sched_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                int doc_id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                tempList.add(new ScheduleModel(sched_id, doc_id, date));
            }
        }
        return tempList;
    }

    public ArrayList<DoctorModel> getDoctorsList()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("doctors", null, null, null, null, null, null);

        ArrayList<DoctorModel> tempList = new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));

                tempList.add(new DoctorModel(id, name, spec));
            }
        }

        return tempList;
    }

    public ArrayList<DoctorModel> getDoctorsList(int doctor_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("schedules", null, "doctor_id = ?", new String[]{String.valueOf(doctor_id)}, null, null, null);

        ArrayList<DoctorModel> tempList = new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String spec = cursor.getString(cursor.getColumnIndexOrThrow("specialization"));

                tempList.add(new DoctorModel(id, name, spec));
            }
        }

        return tempList;
    }







}
