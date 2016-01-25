package com.dsquare.hibour.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.dsquare.hibour.fragments.Settings;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    String data;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        //calculate max date
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        data=getArguments().getString("data");   //This method is used to retrive the data from Issue fragment
        return dialog;
        }

        //This method will override when we implement DatePickerdialog
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar dob = Calendar.getInstance();
            dob.set(year, month, day);
            Calendar postage=Calendar.getInstance();
            postage.set(year, month, day);


            //retriving date from issue
            if (data.equals("a")){
                Settings.showDate(year, month + 1, day);
            }



        }
    }


