package com.example.clinicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.R;
import com.example.clinicapp.models.AppointmentModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>
{
    private List<AppointmentModel> appointmentModelList;
    private Context context;
    private String type;

    public void setAppointmentModelList(List<AppointmentModel> appointmentModelList, String type)
    {
        this.type = type;
        this.appointmentModelList = appointmentModelList;
    }

    @NonNull
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_appointments_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.ViewHolder holder, int position)
    {
        AppointmentModel model = appointmentModelList.get(position);

        DBHelper dbHelper = new DBHelper(context);

        holder.tv_purpose.setText(model.getPurpose());

        holder.tv_spec.setText(model.getTime());








        switch (type)
        {
            case "doctor":

                String name = dbHelper.getPatientName(model.getPatient_id());
                holder.tv_name.setText(name);

                String status = model.getStatus();
                updateUI(holder, status);

                holder.btn_cancel.setOnClickListener(v -> {
                    dbHelper.updateAppointmentStatus(model.getAppointment_id(), "cancelled");
                    model.setStatus("cancelled");
                    updateUI(holder, "cancelled");
                });

                holder.btn_confirm.setOnClickListener(v -> {
                    dbHelper.updateAppointmentStatus(model.getAppointment_id(), "confirmed");
                    model.setStatus("confirmed");
                    updateUI(holder, "confirmed");
                });
                break;

            case "patient":
                String name1 = dbHelper.getDoctorName(model.getDoctor_id());
                holder.tv_name.setText(name1);

                String status1 = model.getStatus();

                switch (status1) {
                    case "pending":
                        holder.btn_cancel.setVisibility(View.GONE);
                        holder.btn_confirm.setVisibility(View.GONE);
                        holder.tv_status.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Pending");
                        break;
                    case "confirmed":
                        holder.btn_cancel.setVisibility(View.GONE);
                        holder.btn_confirm.setVisibility(View.GONE);
                        holder.tv_status.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Confirmed");
                        holder.tv_status.setTextColor(context.getResources().getColor(R.color.confirmed));
                        break;
                    case "cancelled":
                        holder.btn_cancel.setVisibility(View.GONE);
                        holder.btn_confirm.setVisibility(View.GONE);
                        holder.tv_status.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Cancelled");
                        holder.tv_status.setTextColor(context.getResources().getColor(R.color.cancelled));
                        break;
                }


                break;
        }

    }

    @Override
    public int getItemCount()
    {
        return appointmentModelList.size() == 0 ? 0 : appointmentModelList.size();
    }

    private void updateUI(ViewHolder holder, String status) {
        switch (status) {
            case "pending":
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.tv_status.setVisibility(View.GONE);
                break;
            case "confirmed":
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_confirm.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText("Confirmed");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.confirmed));
                break;
            case "cancelled":
                holder.btn_cancel.setVisibility(View.GONE);
                holder.btn_confirm.setVisibility(View.GONE);
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText("Cancelled");
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.cancelled));
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_name, tv_purpose, tv_status, tv_spec;
        private MaterialButton btn_cancel, btn_confirm;
        private CardView cv;
        private LinearLayout ll;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tvName);
            tv_purpose = itemView.findViewById(R.id.tvPurpose);
            btn_cancel = itemView.findViewById(R.id.btnCancel);
            btn_confirm = itemView.findViewById(R.id.btnConfirm);
            cv = itemView.findViewById(R.id.cv);
            tv_status = itemView.findViewById(R.id.tvStatus);
            tv_spec = itemView.findViewById(R.id.tvSpec);
            ll = itemView.findViewById(R.id.ll);
        }
    }

}
