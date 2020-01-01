package com.example.werkstuk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimeInstaceAdapter extends RecyclerView.Adapter<TimeInstaceAdapter.TimeInstanceHolder> {
    private List<TimeInstance> timeInstanceList = new ArrayList<TimeInstance>();

    @NonNull
    @Override
    public TimeInstanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeInstanceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.time_instance_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeInstanceHolder holder, int position) {
        TimeInstance currentTimeInstance = timeInstanceList.get(position);
        holder.nameTextView.setText(currentTimeInstance.getName());
        holder.daysTextView.setText(currentTimeInstance.getDaysStr());
        holder.timeTextView.setText(currentTimeInstance.getTimeStr());
        holder.onSwitch.setChecked(currentTimeInstance.isOn());
    }

    @Override
    public int getItemCount() {
        return timeInstanceList.size();
    }

    public void setTimeInstanceList(List<TimeInstance> timeInstanceList) {
        this.timeInstanceList = timeInstanceList;
        //change
        notifyDataSetChanged();
    }

    class TimeInstanceHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView;
        private TextView daysTextView;
        private TextView timeTextView;
        private Switch onSwitch;


        public TimeInstanceHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_name);
            daysTextView = itemView.findViewById(R.id.text_view_days);
            timeTextView = itemView.findViewById(R.id.text_view_time);
            onSwitch = itemView.findViewById(R.id.switch_on);
        }
    }
}
