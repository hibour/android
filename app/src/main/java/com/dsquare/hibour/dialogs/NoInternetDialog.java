package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dsquare.impfocuscustomer.R;

/**
 * Created by Dsquare Android on 10/16/2015.
 */
public class NoInternetDialog extends DialogFragment {
    public interface NoInternetListener {
        public void onExitClick(DialogFragment dialog);
        public void OnSettingsClick(DialogFragment dialog);
    }
    NoInternetListener mListener;
    private TextView title,text;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoInternetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_no_internet, null);
        title=(TextView)view.findViewById(R.id.network_title);
        text = (TextView)view.findViewById(R.id.netowrk_text);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.OnSettingsClick(NoInternetDialog.this);
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onExitClick(NoInternetDialog.this);
                    }
                });
        return builder.create();
    }
}
