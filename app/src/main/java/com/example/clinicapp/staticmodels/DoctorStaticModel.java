package com.example.clinicapp.staticmodels;

public class DoctorStaticModel
{
    private static int doctor_id;
    private static String name;
    private static String specialization;

    public DoctorStaticModel(int doctor_id, String name, String specialization)
    {
        this.doctor_id = doctor_id;
        this.name = name;
        this.specialization = specialization;
    }

    public static int getDoctor_id()
    {
        return doctor_id;
    }

    public static String getName()
    {
        return name;
    }

    public static String getSpecialization()
    {
        return specialization;
    }
}
