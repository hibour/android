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
import com.dsquare.hibour.interfaces.ImagePicker;


/**
 * Created by Dsquare Android on 1/21/2016.
 */

public class PostsImagePicker extends DialogFragment implements View.OnClickListener{

    private RelativeLayout gallaryLayout,cameraLayout;
    private ImagePicker host;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            host = (ImagePicker)getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
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
                host.pickerSelection(0);
                break;
            case R.id.camera_layout:
                host.pickerSelection(1);
                break;
        }
    }

}

