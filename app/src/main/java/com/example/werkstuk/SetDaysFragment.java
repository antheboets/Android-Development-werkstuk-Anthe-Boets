package com.example.werkstuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.werkstuk.R;

public class SetDaysFragment extends Fragment {

    public interface SetDaysFragmentListener {

    }
    private SetDaysFragmentListener setDaysFragmentListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.time_instance_edit_days_fragment, container, false);


        return view;
    }

    /*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof TimeInstanceListFragment.TimeInstanceFragmentListListener){
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
    */

}
