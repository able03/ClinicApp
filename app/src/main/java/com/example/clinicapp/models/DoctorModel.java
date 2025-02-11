package com.example.clinicapp.models;

public class DoctorModel
{
    private int doctor_id;
    private String name;
    private String specialization;

    public DoctorModel(int doctor_id, String name, String specialization)
    {
        this.doctor_id = doctor_id;
        this.name = name;
        this.specialization = specialization;
    }

    public int getDoctor_id()
    {
        return doctor_id;
    }

    public String getName()
    {
        return name;
    }

    public String getSpecialization()
    {
        return specialization;
    }
}
