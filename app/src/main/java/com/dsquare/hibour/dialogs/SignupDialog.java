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
 * Created by Dsquare Android on 2/29/2016.
 */
public class SignupDialog extends android.app.DialogFragment implements View.OnClickListener {


    //    public interface SignUpInterface{
//        void onPositiveButtonClick();
//        void onNegetiveButtonClick();
//    }
public interface SignUpCallback {
       void closeDialog(SignupDialog signupDialog);
    }

//    SignUpInterface mSignUpInterface;
    private TextView body;
    private String positiveText="",negetiveText="",bodyText="";
    private TextView text, ok;
    private SignUpCallback callback;



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
            callback = (SignUpCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private void initializeViews(View view) {
        ok = (TextView) view.findViewById(R.id.signdailog_ok);
    }

    private void initializeEventListeners() {
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        callback.closeDialog(this);
    }


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        Bundle data = getArguments();
////        positiveText = data.getString("positiveText","");
////        negetiveText = data.getString("negetiveText","");
////        bodyText = data.getString("bodyText","");
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_signup, null);
//        ok = (TextView) view.findViewById(R.id.signdailog_ok);
////        body = (TextView)view.findViewById(R.id.signup_dialog_text);
////        body.setText(bodyText);
//
//        ok.setOnClickListener(this);
//        builder.setView(view);
////        builder.setNegativeButton(negetiveText,new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////
////            }
////        });
////        builder.setPositiveButton(positiveText,new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////
////            }
////        });
//        return builder.create();
//    }
//    @Override
//    public void onClick(View view) {
//        callback.closeDialog(this);
//    }
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            callback = (SignUpCallback) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement NoticeDialogListener");
//        }



}
