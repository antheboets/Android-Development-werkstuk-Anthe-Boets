package com.example.werkstuk;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.werkstuk.OptionActivity._24HOURS_DAY;


public class TimeInstaceAdapter extends RecyclerView.Adapter<TimeInstaceAdapter.TimeInstanceHolder> {
    private List<TimeInstance> timeInstanceList = new ArrayList<TimeInstance>();
    private OnClickListener itemClickListener;
    private OnSwitchChangeListener switchChangeListener;
    private boolean _24hoursDays;

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
        holder.timeTextView.setText(currentTimeInstance.getTimeStr(_24hoursDays));
        holder.onSwitch.setOnCheckedChangeListener(null);
        holder.onSwitch.setChecked(currentTimeInstance.isOn());
        final int pos = position;
        holder.onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchChangeListener != null) {
                    TimeInstance timeInstance = timeInstanceList.get(pos);
                    timeInstance.setOn(isChecked);
                    switchChangeListener.onSwitchChange(timeInstance);
                }

            }
        });
    }

    public void set24HoursDay(boolean _24hoursDays) {
        this._24hoursDays = _24hoursDays;
    }

    public TimeInstaceAdapter(boolean _24hoursDays) {
        this._24hoursDays = _24hoursDays;
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

    public void resetListView() {

        notifyDataSetChanged();
    }

    public TimeInstance getTimeInstanceAt(int pos) {
        return timeInstanceList.get(pos);
    }

    class TimeInstanceHolder extends RecyclerView.ViewHolder {

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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (itemClickListener != null && pos != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(timeInstanceList.get(pos));
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClick(TimeInstance timeInstance);
    }

    public interface OnSwitchChangeListener {
        void onSwitchChange(TimeInstance timeInstance);
    }


    public void setOnItemClickListener(OnClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnSwitchChange(OnSwitchChangeListener listener) {
        this.switchChangeListener = listener;
    }
}
