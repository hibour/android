package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Proof;
import com.dsquare.hibour.activities.SocialPrefernce;
import com.dsquare.hibour.adapters.SelectDateFragment;
import com.dsquare.hibour.activities.Notifications;
import com.dsquare.hibour.dialogs.PostsImagePicker;
import com.dsquare.hibour.interfaces.ImagePicker;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.settings.Data;
import com.dsquare.hibour.pojos.settings.Settingspojo;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment implements View.OnClickListener,ImagePicker {

    private ImageView menuIcon,notifIcon,inputImage,imageUploaded,dobimage;
    private RadioGroup gender;
    private EditText name;
    private EditText email;
    private EditText password;
    private static EditText dob;
    private EditText moblie;
    private TextView proof,soclize;
    private List<String> cardsList=new ArrayList<>();
    private List<String> genderList = new ArrayList<>();
    private NavDrawerCallback callback;
    private Button submitButton;
    private ProgressDialog dialog;
    private static final int REQUEST_IMAGE_SELECTOR=1000;
    private static final int REQUEST_IMAGE_CAPTURE=1001;
    private int PICK_IMAGE_REQUEST = 1;
    private Typeface tf,proxima;
    private DialogFragment chooserDialog;
    private Bitmap bitmap;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog uploadProofsDialog;
    private Gson gson;
    private Hibour application;
    private String genderstring="",cardImageString="a";
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeViews(view);
        initializeEventListeners();
        getAllPrefs();
        return view;
    }
    /* initialize views*/
    private void initializeViews(View view){
        accountsClient = new AccountsClient(getActivity());
        networkDetector = new NetworkDetector(getActivity());
        gson = new Gson();
        application =  Hibour.getInstance(getActivity());
        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        menuIcon = (ImageView)view.findViewById(R.id.settings_menu_icon);
        notifIcon = (ImageView)view.findViewById(R.id.settings_notif_icon);
        inputImage = (ImageView)view.findViewById(R.id.settings_image);
        imageUploaded = (ImageView)view.findViewById(R.id.setting_image_editer);
        dobimage = (ImageView)view.findViewById(R.id.settings_name_dob);
        submitButton = (Button)view.findViewById(R.id.settings_submit);
        gender = (RadioGroup)view.findViewById(R.id.group_radio);
        name = (EditText)view.findViewById(R.id.settings_name_edit);
        email = (EditText)view.findViewById(R.id.settings_email_edit);
        password = (EditText)view.findViewById(R.id.settings_password_Edit);
        dob = (EditText)view.findViewById(R.id.settings_dob_edit);
        moblie = (EditText)view.findViewById(R.id.settings_phone_edit);
        proof = (TextView)view.findViewById(R.id.settings_proof);
        soclize = (TextView)view.findViewById(R.id.settings_prefernce);
        String text = "<u>Change Proof</u>";
        String text1 = "<u>Change SocialPrefernce</u>";
        proof.setText(Html.fromHtml(text));
        soclize.setText(Html.fromHtml(text1));
        tf = Fonts.getTypeFace(getActivity());
        submitButton.setTypeface(proxima);
        name.setTypeface(proxima);
        email.setTypeface(proxima);
        password.setTypeface(proxima);
        dob.setTypeface(proxima);
        moblie.setTypeface(proxima);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String string = button.getText().toString();
                if (button.getText().toString().equals("Male")) {
                    genderstring = "0";
                } else {
                    genderstring = "1";
                }
            }

        });
    }
    /* initialize event listeners*/
     private void initializeEventListeners(){
         menuIcon.setOnClickListener(this);
         notifIcon.setOnClickListener(this);
         submitButton.setOnClickListener(this);
         imageUploaded.setOnClickListener(this);
         proof.setOnClickListener(this);
         soclize.setOnClickListener(this);
         dobimage.setOnClickListener(this);
     }
    /*prepare cards list*/
    private void prepareCardsList(){
        cardsList.add("Select Card");
    }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.settings_menu_icon:
             callback.drawerOpen();
             break;
         case R.id.settings_notif_icon:
             openNotifications();
             break;
         case R.id.settings_submit:
             updateprofileActivity();
             break;
         case R.id.setting_image_editer:
             openImageChooser();
             break;
         case R.id.settings_proof:
             openproofActivity();
             break;
         case R.id.settings_prefernce:
             openprefernceActivity();
             break;
         case R.id.settings_name_dob:
             final DialogFragment newFragment = new SelectDateFragment();
                 Bundle args = new Bundle();
                 args.putString("data", "a");  //This method is used to send the data from one fragment to another fragment
                 newFragment.setArguments(args);
                 newFragment.show(getFragmentManager(), "DatePicker");

             break;
     }
    }
    //Method to display the text
    public static String showDate(int year, int month, int day) {
        try {
            dob.setText(day + "/" + month + "/" + year);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void openprefernceActivity() {
        Intent intent = new Intent(getActivity(), SocialPrefernce.class);
        startActivity(intent);
    }

    private void openproofActivity() {
        Intent intent = new Intent(getActivity(), Proof.class);
        startActivity(intent);
    }

    /* open notifications activity*/
    private void openNotifications(){
        Intent notifIntent = new Intent(getActivity(), Notifications.class);
        startActivity(notifIntent);
    }
    private void openImageChooser(){
        chooserDialog = new PostsImagePicker();
        chooserDialog.show(getActivity().getSupportFragmentManager(), "chooser dialog");
        chooserDialog.setTargetFragment(this, 0);
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK
                &&  data != null && data.getData() != null) {
//            imageUploaded.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
//                postimagesstring=getStringImage(bitmap);
                inputImage.setImageBitmap(bitmap);
                cardImageString=getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK
                &&  data != null && data.getData() != null){
//            imageUploaded.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
//                postimagesstring=getStringImage(bitmap);
                inputImage.setImageBitmap(bitmap);
                cardImageString=getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
//        bitmap = BitmapFactory.decodeFile(imgPath,
//                options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }

    @Override
    public void pickerSelection(int choice) {
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
    private void updateprofileActivity() {
        String userName = name.getText().toString();
        String userMail = email.getText().toString();
        String userPass = password.getText().toString();
        if (!userName.equals(null) && !userName.equals("null") && !userName.equals("") &&
                !userMail.equals(null) && !userMail.equals("null") && !userMail.equals("") &&
                !userPass.equals(null) && !userPass.equals("null") && !userPass.equals("")) {
            if (application.validateEmail(userMail)) {
                if (moblie.getText().toString().length() < 11 && moblie.getText().toString().length() > 9) {
                    if (gender.getCheckedRadioButtonId() != -1) {
                    updateProfiletoUser(userName, userMail, userPass,moblie.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Please select Gender", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Invalid mobile number", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Enter valid email", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity(), "Enter valid credentials", Toast.LENGTH_LONG).show();
        }
    }
    private void updateProfiletoUser(String userName, String userMail, String userPass, String mobile) {
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(getActivity(),"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getAllUpdateSettings(application.getUserId(), userName, userMail, userPass, genderstring, mobile
                  ,  cardImageString  , new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseUpdateDetails(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("signup", error.toString());
                    closeDialog();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Network not connected.", Toast.LENGTH_LONG).show();
        }

    }
     public void parseUpdateDetails(JSONObject jsonObject){
         closeDialog();
         getAllPrefs();
     }

    /* get all prefs*/
    private void getAllPrefs(){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(getActivity(),"",getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getAllSettings(application.getUserId(),new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseUserPrefs(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_LONG).show();
        }
    }
    /* parse user prefs*/
    private void parseUserPrefs(JSONObject jsonObject){
        try {
        Settingspojo settingspojo = gson.fromJson(jsonObject.toString(), Settingspojo.class);
            Data data = settingspojo.getData();
        name.setText(data.getUsername());
        email.setText(data.getEmail());
        password.setText(data.getPassword());
        moblie.setText(data.getMobileNumber());
        String genderValue = data.getGender();
         Log.d("gender",  data.getGender());
        if(genderValue != null && genderValue.equalsIgnoreCase("0")){
            gender.check(R.id.radioMale);
        }else{
            gender.check(R.id.radioFemale);
        }

    }catch (JsonSyntaxException e) {
        e.printStackTrace();
    }catch (final IllegalArgumentException e) {
        // Handle or log or ignore
        e.printStackTrace();
    }}
    /* close dialog*/
    private void closeDialog(){
        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
                dialog=null;
            }
        }
    }
}
