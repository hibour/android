package com.dsquare.hibour.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.dialogs.ImagePickerDialog;
import com.dsquare.hibour.utils.Fonts;

/**
 * Created by Aditya Ravikanti on 1/19/2016.
 */
public class NewPosts extends Fragment implements View.OnClickListener,ImagePickerDialog.ImageChooserListener {

   private Spinner spinner;
   private Button send;
   private ImageView gallary;
    private EditText text;
    private Typeface proxima;
    private DialogFragment chooserDialog;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] List={"Music","Dance","Sports","Design","Clubbing"};
    private static final int REQUEST_IMAGE_SELECTOR=1000;
    private static final int REQUEST_IMAGE_CAPTURE=1001;
    public NewPosts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newposts, container, false);
        initializeViews(view);
        initializeEventListeners(view);
        return view;
    }



    private void initializeViews(View view) {

        spinner = (Spinner)view.findViewById(R.id.newpost_spinner);
        send = (Button)view.findViewById(R.id.newpost_send);
        gallary = (ImageView)view.findViewById(R.id.newposts_gallary);
        text = (EditText)view.findViewById(R.id.newposts_edittest);
        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        send.setTypeface(proxima);
        spinnerAdapter = new ArrayAdapter<String>(getActivity()
                ,android.R.layout.simple_dropdown_item_1line,List);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTypeface(proxima);
                    Log.d("padding", parent.getChildAt(0).getPaddingLeft() + "");
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initializeEventListeners(View view) {

        gallary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.newposts_gallary:
//                openImageChooser();
                break;
        }
    }

    private void openImageChooser(){
        chooserDialog = new ImagePickerDialog();
        chooserDialog.show(getActivity().getSupportFragmentManager(), "chooser dialog");
    }

    @Override
    public void onChoose(int choice) {
        switch (choice){
            case 0:
                openGallary();
                break;
            case 1:
                openCamera();
                break;
        }
        chooserDialog.dismiss();
    }
    /* open gallary intent*/
    private void openGallary(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
    }
    /* open camera*/
    private void openCamera(){
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            imageUploaded.setVisibility(View.VISIBLE);
        }else if(requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK){
//            imageUploaded.setVisibility(View.VISIBLE);
        }
    }
}