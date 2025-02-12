package com.example.clinicapp.models;

public class AppointmentModel
{
    private int appointment_id;
    private int doctor_id;
    private String status;
    private int patient_id;

    public void setStatus(String status)
    {
        this.status = status;
    }

    private String date;
    private String time;
    private int schedule_id;
    private String purpose;

    public AppointmentModel(int appointment_id, int doctor_id, String status, int patient_id, String date, String time, int schedule_id, String purpose)
    {
        this.appointment_id = appointment_id;
        this.doctor_id = doctor_id;
        this.status = status;
        this.patient_id = patient_id;
        this.date = date;
        this.time = time;
        this.schedule_id = schedule_id;
        this.purpose = purpose;
    }

    public int getAppointment_id()
    {
        return appointment_id;
    }

    public int getDoctor_id()
    {
        return doctor_id;
    }

    public String getStatus()
    {
        return status;
    }

    public int getPatient_id()
    {
        return patient_id;
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public int getSchedule_id()
    {
        return schedule_id;
    }

    public String getPurpose()
    {
        return purpose;
    }
}
