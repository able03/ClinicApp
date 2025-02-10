package com.example.clinicapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.OnItemClickListener;
import com.example.clinicapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>
{
    private ArrayList<String> dayList;
    private OnItemClickListener listener;

    public CalendarAdapter(ArrayList<String> dayList, OnItemClickListener listener)
    {
        this.dayList = dayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_calendar_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position)
    {
        String day = dayList.get(position);
        holder.tv_date.setText(day);

        if (day.isEmpty()) {
            holder.tv_date.setBackgroundColor(0x00000000); // Transparent for empty days
        } else {
            holder.tv_date.setBackgroundResource(android.R.color.darker_gray);
        }

        holder.tv_date.setOnClickListener(v -> {
            if (!day.isEmpty()) {
                listener.onItemClick(day);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_date;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_day);
        }
    }
}
