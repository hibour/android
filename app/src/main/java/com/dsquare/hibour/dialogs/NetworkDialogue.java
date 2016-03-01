package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dsquare.hibour.R;

/**
 * Created by Dsquare Android on 3/1/2016.
 */
public class NetworkDialogue extends android.app.DialogFragment implements View.OnClickListener {
    public interface NetworkCallback {
        void closeDialog(NetworkDialogue networkDialogue);
    }

    //    SignUpInterface mSignUpInterface;
    private TextView body;
    private String positiveText="",negetiveText="",bodyText="";
    private TextView text,text1, ok;
    private NetworkCallback callback;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("Welcome dialog", "welcome");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signup, null);
        builder.setView(view);
        initializeViews(view);
        initializeEventListeners();
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (NetworkCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private void initializeViews(View view) {
        ok = (TextView) view.findViewById(R.id.signdailog_ok);
        text = (TextView)view.findViewById(R.id.signdailog_text);
        text1 = (TextView)view.findViewById(R.id.signup_text1);

        text.setText("Network Error");
        text1.setText("Please check your Network Connection!");
    }

    private void initializeEventListeners() {
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        callback.closeDialog(this);
    }

}
