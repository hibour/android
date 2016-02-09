package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import com.dsquare.hibour.R;

/**
 * Created by Anji on 2/9/2016.
 */

public class WelcomeDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener{
    private Button hi;
  //  NoticeDialogListener mListener;

   /* public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }*/



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("welcome dialog","welcome");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.welcome_dialog, null);
        builder.setView(view);
        initializeViews(view);
        initializeEventListeners();
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        /*try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }*/
    }



    private void initializeViews(View view) {
        hi=(Button)view.findViewById(R.id.welcome_dialog_hi);
    }

    private void initializeEventListeners() {
        hi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
