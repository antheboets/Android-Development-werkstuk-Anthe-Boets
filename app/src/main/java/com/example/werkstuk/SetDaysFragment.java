package com.example.werkstuk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.werkstuk.R;

public class SetDaysFragment extends Fragment {

    private static final String DAYS_ARRAY = "com.example.werkstuk.DAYS_ARRAY";

    private boolean[] daysArr;

    public interface SetDaysFragmentListener {
        void updateActivity(boolean[] daysArr);
        void onFragmentInteraction(boolean[] daysArr);
    }
    private SetDaysFragmentListener setDaysFragmentListener;

    private Switch moSwitch;
    private Switch tuSwitch;
    private Switch weSwitch;
    private Switch thSwitch;
    private Switch frSwitch;
    private Switch saSwitch;
    private Switch suSwitch;
    private Button closeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.time_instance_edit_days_fragment, container, false);

        moSwitch = view.findViewById(R.id.edit_days_switch_mo);
        tuSwitch = view.findViewById(R.id.edit_days_switch_tu);
        weSwitch = view.findViewById(R.id.edit_days_switch_we);
        thSwitch = view.findViewById(R.id.edit_days_switch_th);
        frSwitch = view.findViewById(R.id.edit_days_switch_fr);
        saSwitch = view.findViewById(R.id.edit_days_switch_sa);
        suSwitch = view.findViewById(R.id.edit_days_switch_su);
        closeButton = view.findViewById(R.id.set_day_close_button);

        moSwitch.setChecked(daysArr[0]);
        tuSwitch.setChecked(daysArr[1]);
        weSwitch.setChecked(daysArr[2]);
        thSwitch.setChecked(daysArr[3]);
        frSwitch.setChecked(daysArr[4]);
        saSwitch.setChecked(daysArr[5]);
        suSwitch.setChecked(daysArr[6]);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] newDaysArr = new boolean[]{
                        moSwitch.isChecked(),
                        tuSwitch.isChecked(),
                        weSwitch.isChecked(),
                        thSwitch.isChecked(),
                        frSwitch.isChecked(),
                        saSwitch.isChecked(),
                        suSwitch.isChecked()
                };
                sendBack(newDaysArr);
            }
        });
        moSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());
        tuSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());
        weSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());
        thSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());
        frSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());
        saSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());
        suSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());

        return view;
    }

    public class  onCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean[] newDaysArr = new boolean[]{
                    moSwitch.isChecked(),
                    tuSwitch.isChecked(),
                    weSwitch.isChecked(),
                    thSwitch.isChecked(),
                    frSwitch.isChecked(),
                    saSwitch.isChecked(),
                    suSwitch.isChecked()
            };
            update(newDaysArr);
        }
    }

    public SetDaysFragment(){

    }

    public  void sendBack(boolean[] daysArr){
        if(setDaysFragmentListener != null){
            setDaysFragmentListener.onFragmentInteraction(daysArr);
        }
    }
    public void update(boolean[] daysArr){
        if(setDaysFragmentListener != null){
            setDaysFragmentListener.updateActivity(daysArr);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            boolean[] daysArrTemp =  getArguments().getBooleanArray(DAYS_ARRAY);
            if(daysArrTemp != null){
                daysArr = daysArrTemp;
            }
            else{
                daysArr = new boolean[]{false,false,false,false,false,false,false};
            }

        }
    }

    public static SetDaysFragment newInstance(boolean[] daysArr){
        SetDaysFragment fragment = new SetDaysFragment();
        Bundle args = new Bundle();
        args.putBooleanArray(DAYS_ARRAY, daysArr);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SetDaysFragment.SetDaysFragmentListener){
            setDaysFragmentListener = (SetDaysFragment.SetDaysFragmentListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement TimeInstanceFragmentListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setDaysFragmentListener = null;
    }


}
