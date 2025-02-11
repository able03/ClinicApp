package com.example.clinicapp.models;

public class ScheduleModel
{
    private int scheduleID;
    private int doctorID;
    private String date;

    public ScheduleModel(int scheduleID, int doctorID, String date)
    {
        this.scheduleID = scheduleID;
        this.doctorID = doctorID;
        this.date = date;
    }

    public int getScheduleID()
    {
        return scheduleID;
    }

    public int getDoctorID()
    {
        return doctorID;
    }

    public String getDate()
    {
        return date;
    }
}
