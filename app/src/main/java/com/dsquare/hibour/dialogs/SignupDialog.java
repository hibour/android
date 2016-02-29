package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dsquare.hibour.R;

/**
 * Created by Dsquare Android on 2/29/2016.
 */
public class SignupDialog extends DialogFragment {
    public interface SignUpInterface{
        void onPositiveButtonClick();
        void onNegetiveButtonClick();
    }

    SignUpInterface mSignUpInterface;
    private TextView body;
    private String positiveText="",negetiveText="",bodyText="";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle data = getArguments();
        positiveText = data.getString("positiveText","");
        negetiveText = data.getString("negetiveText","");
        bodyText = data.getString("bodyText","");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_signup, null);
        body = (TextView)view.findViewById(R.id.signup_dialog_text);
        body.setText(bodyText);
        builder.setView(view);
        builder.setNegativeButton(negetiveText,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(positiveText,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mSignUpInterface = (SignUpInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


}
