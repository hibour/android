package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.dsquare.hibour.R;

/**
 * Created by Dsquare Android on 1/8/2016.
 */
public class ImagePickerDialog extends DialogFragment implements View.OnClickListener{
    public interface ImageChooserListener{
        void onChoose(int choice);
    }
    ImageChooserListener listener;

    private RelativeLayout gallaryLayout,cameraLayout;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ImageChooserListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_image_chooser, null);
        builder.setView(view);
        initializeViews(view);
        initializeEventListeners();
        return builder.create();
    }
    /* initialize Views*/
    private void initializeViews(View view){
        gallaryLayout = (RelativeLayout)view.findViewById(R.id.gallary_layout);
        cameraLayout = (RelativeLayout)view.findViewById(R.id.camera_layout);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        gallaryLayout.setOnClickListener(this);
        cameraLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gallary_layout:
                listener.onChoose(0);
                break;
            case R.id.camera_layout:
                listener.onChoose(1);
                break;
        }
    }
}
