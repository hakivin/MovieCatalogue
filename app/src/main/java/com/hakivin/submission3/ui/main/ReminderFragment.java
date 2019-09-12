package com.hakivin.submission3.ui.main;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.hakivin.submission3.R;
import com.hakivin.submission3.entity.Reminder;
import com.hakivin.submission3.notification.ReminderPreference;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderFragment extends DialogFragment {

    private OnCompleteListener listener;

    static ReminderFragment newInstance(){
        return new ReminderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        Switch switchDaily = view.findViewById(R.id.switch_daily);
        Switch switchRelease = view.findViewById(R.id.switch_release);
        ReminderPreference preference = new ReminderPreference(view.getContext());
        Reminder reminder = preference.getPreference();
        switchDaily.setChecked(reminder.getDaily());
        switchRelease.setChecked(reminder.getRelease());
        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                ReminderPreference preference1 = new ReminderPreference(view.getContext());
                Reminder reminder1 = preference1.getPreference();
                reminder1.setDaily(isChecked);
                preference1.setPreference(reminder1);
            }
        });
        switchRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                ReminderPreference preference1 = new ReminderPreference(view.getContext());
                Reminder reminder1 = preference1.getPreference();
                reminder1.setRelease(isChecked);
                preference1.setPreference(reminder1);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.listener = (OnCompleteListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Reminder reminder = new ReminderPreference(Objects.requireNonNull(getContext())).getPreference();
        this.listener.onComplete(reminder);

    }

    public interface OnCompleteListener{
        void onComplete(Reminder reminder);
    }
}
