package com.example.clinicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.DBHelper;
import com.example.clinicapp.R;
import com.example.clinicapp.models.AppointmentModel;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>
{
    private List<AppointmentModel> transactionModelList;
    private Context context;

    public void setTransactionModelList(List<AppointmentModel> transactionModelList)
    {
        this.transactionModelList = transactionModelList;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_transaction_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position)
    {
        AppointmentModel model = transactionModelList.get(position);

        DBHelper dbHelper = new DBHelper(context);
        String name = dbHelper.getPatientName(model.getAppointment_id());
        holder.tv_name.setText(name);
    }

    @Override
    public int getItemCount()
    {
        return transactionModelList.size() == 0 ? 0 : transactionModelList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_name;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tvName);
        }
    }

}
