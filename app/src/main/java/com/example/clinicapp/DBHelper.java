package com.example.clinicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.clinicapp.models.AppointmentModel;
import com.example.clinicapp.models.DoctorModel;
import com.example.clinicapp.models.ScheduleModel;
import com.example.clinicapp.notification.NotificationScheduler;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private Context context;
    public DBHelper(@Nullable Context context)
    {
        super(context, "clinic.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE doctors(doctor_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, specialization TEXT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE patients(patient_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE appointments(appointment_id INTEGER PRIMARY KEY AUTOINCREMENT, doctor_id INTEGER, status TEXT, patient_id INTEGER, date TEXT, time TEXT, schedule_id INTEGER, purpose TEXT)");
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


    public boolean createAppointment(int doctor_id, int patient_id, String status, String date,
                                      String time, int schedule_id, String purpose)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("doctor_id", doctor_id);
        values.put("patient_id", patient_id);
        values.put("status", "pending");
        values.put("date", date);
        values.put("time", time);
        values.put("schedule_id", schedule_id);
        values.put("purpose", purpose);

        return db.insert("appointments", null, values) != -1;
    }

    public ArrayList<String> getBookedTimeSlots(int doctor_id, int schedule_id, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> bookedSlots = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                new String[]{"time"},
                "doctor_id = ? AND schedule_id = ? AND date = ?",
                new String[]{String.valueOf(doctor_id), String.valueOf(schedule_id), date},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                bookedSlots.add(cursor.getString(cursor.getColumnIndexOrThrow("time")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookedSlots;
    }

    public ArrayList<ScheduleModel> getBookedSchedulesList(int doctor_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT s.schedule_id, s.doctor_id, s.date " +
                "FROM schedules s " +
                "INNER JOIN appointments a ON s.schedule_id = a.schedule_id " +
                "WHERE s.doctor_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(doctor_id)});

        ArrayList<ScheduleModel> tempList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int sched_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                int doc_id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                tempList.add(new ScheduleModel(sched_id, doc_id, date));
            }
        }
        cursor.close();
        return tempList;
    }

    public ArrayList<AppointmentModel> getConfirmedAppointmentsByDoctor(int doctor_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppointmentModel> appointmentList = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                null,
                "doctor_id = ? AND status = ?",
                new String[]{String.valueOf(doctor_id), "confirmed"},
                null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int appointment_id = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                int patient_id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                int schedule_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));

                appointmentList.add(new AppointmentModel(appointment_id, doctor_id, status, patient_id, date, time, schedule_id, purpose));
            }
        }
        cursor.close();
        return appointmentList;
    }

    public ArrayList<AppointmentModel> getAppointmentsByDoctorAndSchedule(int doctor_id, int schedule_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppointmentModel> appointmentList = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                null,
                "doctor_id = ? AND schedule_id = ? AND status = ?",
                new String[]{String.valueOf(doctor_id), String.valueOf(schedule_id), "pending"},
                null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int appointment_id = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                int patient_id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));

                appointmentList.add(new AppointmentModel(appointment_id, doctor_id, status, patient_id, date, time, schedule_id, purpose));
            }
        }
        cursor.close();
        return appointmentList;
    }

    public boolean updateAppointmentStatus(int appointment_id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);

        int rowsAffected = db.update("appointments", values, "appointment_id = ?", new String[]{String.valueOf(appointment_id)});
        if (rowsAffected > 0) {
            String notificationMessage = newStatus.equals("confirmed") ?
                    "Your appointment has been confirmed!" : "Your appointment has been canceled.";

            Cursor cursor = db.rawQuery("SELECT patient_id FROM appointments WHERE appointment_id = ?", new String[]{String.valueOf(appointment_id)});
            if (cursor.moveToFirst()) {
                int patientId = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));

                String patientName = getPatientName(patientId);

                NotificationScheduler.scheduleNotification(context, "Appointment Update", patientName + ", " + notificationMessage, 5);
            }
            cursor.close();
        }

        return rowsAffected > 0;
    }

    public String getPatientName(int patientId) {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM patients WHERE patient_id = ?", new String[]{String.valueOf(patientId)});

        if (cursor.moveToFirst()) {
            sb.append(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }

        cursor.close();
        return sb.toString();
    }

    public String getDoctorName(int doctorId) {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM doctors WHERE doctor_id = ?", new String[]{String.valueOf(doctorId)});

        if (cursor.moveToFirst()) {
            sb.append(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }

        cursor.close();
        return sb.toString();
    }




    public ArrayList<AppointmentModel> getAppointmentsByStatus(int doctor_id, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppointmentModel> appointmentList = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                null,
                "doctor_id = ? AND status = ?",
                new String[]{String.valueOf(doctor_id), status},
                null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int appointment_id = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                int patient_id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                int schedule_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));

                appointmentList.add(new AppointmentModel(appointment_id, doctor_id, status, patient_id, date, time, schedule_id, purpose));
            }
        }
        cursor.close();
        return appointmentList;
    }

    public ArrayList<AppointmentModel> getPendingAppointmentsByDoctor(int doctor_id) {
        return getAppointmentsByStatus(doctor_id, "pending");
    }

    public ArrayList<AppointmentModel> getConfirmedAppointments(int doctor_id) {
        return getAppointmentsByStatus(doctor_id, "confirmed");
    }

    public ArrayList<AppointmentModel> getCancelledAppointments(int doctor_id) {
        return getAppointmentsByStatus(doctor_id, "cancelled");
    }

    public ArrayList<AppointmentModel> getAppointementsList(int doc_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppointmentModel> appointmentList = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                null,
                "doctor_id = ?",
                new String[]{String.valueOf(doc_id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int appointment_id = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                int patient_id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                int doctor_id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                int schedule_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));

                appointmentList.add(new AppointmentModel(appointment_id, doctor_id, status, patient_id, date, time, schedule_id, purpose));
            }
        }
        cursor.close();
        return appointmentList;
    }


    public ArrayList<AppointmentModel> getAppointementsListPatient(int pat_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppointmentModel> appointmentList = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                null,
                "patient_id = ?",
                new String[]{String.valueOf(pat_id)},
                null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int appointment_id = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                int patient_id = cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                int doctor_id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                int schedule_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));

                appointmentList.add(new AppointmentModel(appointment_id, doctor_id, status, patient_id, date, time, schedule_id, purpose));
            }
        }
        cursor.close();
        return appointmentList;
    }




    public ArrayList<AppointmentModel> getAppointmentsByStatusPatient(int pat_id, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AppointmentModel> appointmentList = new ArrayList<>();

        Cursor cursor = db.query("appointments",
                null,
                "patient_id = ? AND status = ?",
                new String[]{String.valueOf(pat_id), status},
                null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int appointment_id = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                int doctor_id = cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                int schedule_id = cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id"));
                String purpose = cursor.getString(cursor.getColumnIndexOrThrow("purpose"));

                appointmentList.add(new AppointmentModel(appointment_id, doctor_id, status, pat_id, date, time, schedule_id, purpose));
            }
        }
        cursor.close();
        return appointmentList;
    }


    public ArrayList<AppointmentModel> getPendingAppointmentsByDoctorPatient(int patient_id) {
        return getAppointmentsByStatusPatient(patient_id, "pending");
    }

    public ArrayList<AppointmentModel> getConfirmedAppointmentsPatient(int patient_id) {
        return getAppointmentsByStatusPatient(patient_id, "confirmed");
    }

    public ArrayList<AppointmentModel> getCancelledAppointmentsPatient(int patient_id) {
        return getAppointmentsByStatusPatient(patient_id, "cancelled");
    }


}
