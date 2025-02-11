package com.example.clinicapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicapp.R;
import com.example.clinicapp.models.ScheduleModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>
{
    private List<ScheduleModel> scheduleModelList;
    private Context context;
    private OnDateClickListener listener;

    public interface OnDateClickListener
    {
        void onDateSelected(ScheduleModel model);
    }


    public void setScheduleModelList(List<ScheduleModel> scheduleModelList)
    {
        this.scheduleModelList = scheduleModelList;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_mini_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position)
    {
        ScheduleModel scheduleModel = scheduleModelList.get(position);
        String dateStr = scheduleModel.getDate();

        Log.d("debug", "Date: " + dateStr);

        String[] parts = dateStr.split("-");
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String monthName = monthNames[month - 1];

        holder.tv_date.setText(String.valueOf(day));
        holder.tv_day.setText(getDayOfWeek(dateStr));
        holder.tv_month.setText(monthName);

        holder.cv.setOnClickListener(cv -> listener.onDateSelected(scheduleModelList.get(position)));

    }

    public void setListener(OnDateClickListener listener)
    {
        this.listener = listener;
    }


    @Override
    public int getItemCount()
    {
        return scheduleModelList.size() == 0 ? 0 : scheduleModelList.size();
    }

    private String getDayOfWeek(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault()); // Mon, Tue, etc.
            return dayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_date, tv_day, tv_month;
        private CardView cv;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_date = itemView.findViewById(R.id.date);
            tv_day = itemView.findViewById(R.id.day);
            tv_month = itemView.findViewById(R.id.month);
            cv = itemView.findViewById(R.id.cvMain);
        }
    }

}
