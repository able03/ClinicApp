package com.example.clinicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.R;
import com.example.clinicapp.models.DoctorModel;
import com.example.clinicapp.patient.activities.BookAppointmentActivity;
import com.example.clinicapp.staticmodels.DoctorStaticModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>
{

    private List<DoctorModel> doctorModels;
    private Context context;

    public void setDoctorModels(List<DoctorModel> doctorModels)
    {
        this.doctorModels = doctorModels;
    }

    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_avail_doc_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position)
    {
        DoctorModel mDoctor = doctorModels.get(position);

        holder.tv_name.setText(mDoctor.getName());
        holder.tv_spec.setText(mDoctor.getSpecialization());

        holder.btn_book.setOnClickListener(book -> {
            new DoctorStaticModel(mDoctor.getDoctor_id(), mDoctor.getName(), mDoctor.getSpecialization());
            Intent intent = new Intent(context, BookAppointmentActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount()
    {
        return doctorModels.size() == 0 ? 0 : doctorModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_name, tv_spec;
        private MaterialButton btn_book;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tvName);
            tv_spec = itemView.findViewById(R.id.tvSpec);
            btn_book = itemView.findViewById(R.id.btnBook);
        }
    }
}
