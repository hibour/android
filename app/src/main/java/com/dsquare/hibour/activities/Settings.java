package com.dsquare.hibour.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.SelectDateFragment;
import com.dsquare.hibour.dialogs.ImagePickerDialog;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.interfaces.SettingsToHomeCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.settings.Data;
import com.dsquare.hibour.pojos.settings.Settingspojo;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends AppCompatActivity implements View.OnClickListener, ImagePickerDialog.ImageChooserListener {
  private static final int REQUEST_IMAGE_SELECTOR = 100;
  private static final int REQUEST_IMAGE_CAPTURE = 200;
  private static EditText dob;
  private ImageView menuIcon, notifIcon, inputImage, imageUploaded, dobimage;
  private RadioGroup gender;
  private RadioButton male, female;
  private EditText name, lastname;
  private EditText email;
  private EditText password;
  private EditText moblie;
  private TextView proof, soclize;
  private List<String> cardsList = new ArrayList<>();
  private List<String> genderList = new ArrayList<>();
  private NavDrawerCallback callback;
  private Button submitButton;
  private ProgressDialog dialog;
  private int PICK_IMAGE_REQUEST = 1;
  private Typeface tf, proxima;
  private DialogFragment chooserDialog;
  private Bitmap bitmap;
  private NetworkDetector networkDetector;
  private AccountsClient accountsClient;
  private ProgressDialog uploadProofsDialog;
  private Gson gson;
  private Hibour application;
  private String genderstring = "", cardImageString = "a";
  private ImageLoader imageLoader;
  private SharedPreferences sharedPreferences;
  private Uri imageUri;
  private CoordinatorLayout coordinatorLayout;
  private LinearLayout moreLayout, feedsLayout, socializeLayout, messagesLayout;
  private SettingsToHomeCallback settingsCallback;

  public Settings() {
    // Required empty public constructor
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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_settings);
    // Inflate the layout for this fragment
    //   View view = inflater.inflate(R.layout.fragment_settings, container, false);
    initializeViews();
    initializeEventListeners();
    getLoginDetails();
    //   return view;
  }


  /* initialize views*/
  private void initializeViews() {
    accountsClient = new AccountsClient(this);
    networkDetector = new NetworkDetector(this);
    gson = new Gson();
    application = Hibour.getInstance(this);
    coordinatorLayout = (CoordinatorLayout) findViewById(R.id
        .coordinatorLayout);
    proxima = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
    //menuIcon = (ImageView)view.findViewById(R.id.settings_menu_icon);
    notifIcon = (ImageView) findViewById(R.id.settings_notif_icon);
    inputImage = (ImageView) findViewById(R.id.settings_image);
    imageUploaded = (ImageView) findViewById(R.id.setting_image_editer);
    dobimage = (ImageView) findViewById(R.id.settings_name_dob);
    submitButton = (Button) findViewById(R.id.settings_submit);
    gender = (RadioGroup) findViewById(R.id.group_radio);
    name = (EditText) findViewById(R.id.settings_name_edit);
    lastname = (EditText) findViewById(R.id.settings_Last_name_edit);
    email = (EditText) findViewById(R.id.settings_email_edit);
    password = (EditText) findViewById(R.id.settings_password_Edit);
    dob = (EditText) findViewById(R.id.settings_dob_edit);
    moblie = (EditText) findViewById(R.id.settings_phone_edit);
    proof = (TextView) findViewById(R.id.settings_location);
    soclize = (TextView) findViewById(R.id.settings_prefernce);
    male = (RadioButton) findViewById(R.id.radioMale);
    female = (RadioButton) findViewById(R.id.radioFemale);
    String text = "<u>Change Location</u>";
    String text1 = "<u>Change SocialPrefernce</u>";
    proof.setText(Html.fromHtml(text));
    soclize.setText(Html.fromHtml(text1));
    tf = Fonts.getTypeFace(this);
    submitButton.setTypeface(proxima);
    name.setTypeface(proxima);
    lastname.setTypeface(proxima);
    email.setTypeface(proxima);
    password.setTypeface(proxima);
    dob.setTypeface(proxima);
    moblie.setTypeface(proxima);
    male.setTypeface(proxima);
    female.setTypeface(proxima);
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
    imageLoader = HibourConnector.getInstance(this).getImageLoader();
    sharedPreferences = getSharedPreferences("Login Credentials", Context.MODE_PRIVATE);
    moreLayout = (LinearLayout) findViewById(R.id.settings_more_layout);
    feedsLayout = (LinearLayout) findViewById(R.id.settings_feed_layout);
    socializeLayout = (LinearLayout) findViewById(R.id.settings_socialize_layout);
    messagesLayout = (LinearLayout) findViewById(R.id.settings_message_layout);
  }

  /* initialize event listeners*/
  private void initializeEventListeners() {
    //menuIcon.setOnClickListener(this);
    notifIcon.setOnClickListener(this);
    submitButton.setOnClickListener(this);
    imageUploaded.setOnClickListener(this);
    proof.setOnClickListener(this);
    soclize.setOnClickListener(this);
    dobimage.setOnClickListener(this);
    dob.setOnClickListener(this);
    moreLayout.setOnClickListener(this);
    feedsLayout.setOnClickListener(this);
    messagesLayout.setOnClickListener(this);
    socializeLayout.setOnClickListener(this);
  }

  /*prepare cards list*/
  private void prepareCardsList() {
    cardsList.add("Select Card");
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.settings_socialize_layout:
        Constants.FRAGMENT_POS = 4;
        settingsCallback.onTabsChoosed(0);
        break;
      case R.id.settings_feed_layout:
        Constants.FRAGMENT_POS = 0;
        settingsCallback.onTabsChoosed(0);
        break;
      case R.id.settings_message_layout:
        Constants.FRAGMENT_POS = 5;
        settingsCallback.onTabsChoosed(0);
        break;
      case R.id.settings_more_layout:
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
      case R.id.settings_location:
        openLocationActivity();
        break;
      case R.id.settings_prefernce:
        openprefernceActivity();
        break;
      case R.id.settings_name_dob:
        final DialogFragment newFragment = new SelectDateFragment();
        Bundle args = new Bundle();
        args.putString("data", "a");  //This method is used to send the data from one fragment to another fragment
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
        break;
      case R.id.settings_dob_edit:
        final DialogFragment newFragment1 = new SelectDateFragment();
        Bundle args1 = new Bundle();
        args1.putString("data", "a");  //This method is used to send the data from one fragment to another fragment
        newFragment1.setArguments(args1);
        newFragment1.show(getSupportFragmentManager(), "DatePicker");
        break;
    }
  }

  private void openprefernceActivity() {
    Intent intent = new Intent(this, SocialPrefernce.class);
    startActivity(intent);
  }

  private void openLocationActivity() {
    Intent intent = new Intent(this, UpdateLocation.class);
    startActivity(intent);
  }

  /* open notifications activity*/
  private void openNotifications() {
    Intent notifIntent = new Intent(this, Notifications.class);
    startActivity(notifIntent);
  }

  private void openImageChooser() {
    chooserDialog = new ImagePickerDialog();
    chooserDialog.show(getSupportFragmentManager(), "chooser dialog");
    //   chooserDialog.setTargetFragment(getApplicationContext(), 3);
  }

  /* open gallary intent*/
  private void openGallary() {
    Log.d("Settings", "open gallery");
    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    this.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
  }

  /* open camera*/
  private void openCamera() {
    Log.d("Settings", "open camera");
    Intent intent = new Intent(
        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    if (intent.resolveActivity(getPackageManager()) != null) {
      this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("Seeings", "result");
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
      bitmap = (Bitmap) data.getExtras().get("data");
      inputImage.setImageBitmap(bitmap);
      cardImageString = getStringImage(bitmap);
      Log.d("bitmap", "camera");
      Log.d("bitmap", bitmap + "");

    } else if (requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK
        && data != null && data.getData() != null) {
      Log.d("Settings", "gallery");
      Uri filePath = data.getData();
      try {
        //Getting the Bitmap from Gallery
        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                postimagesstring=getStringImage(bitmap);
        inputImage.setImageBitmap(bitmap);
        cardImageString = getStringImage(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getStringImage(Bitmap bmp) {
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

  /*@Override
  public void onAttach(Activity activity) {
      super.onAttach(activity);
      callback = (NavDrawerCallback) activity;
      settingsCallback = (SettingsToHomeCallback)activity;
  }*/
  @Override
  public void onChoose(int choice) {
    switch (choice) {
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
    String userLastName = lastname.getText().toString();
    String userMail = email.getText().toString();
    String userPass = password.getText().toString();
    String date = dob.getText().toString();
    if (!userName.equals(null) && !userName.equals("null") && !userName.equals("") &&
        !userLastName.equals(null) && !userLastName.equals("null") && !userLastName.equals("") &&
        !userMail.equals(null) && !userMail.equals("null") && !userMail.equals("") &&
        !userPass.equals(null) && !userPass.equals("null") && !userPass.equals("")) {
      if (application.validateEmail(userMail)) {
        if (moblie.getText().toString().length() < 11 && moblie.getText().toString().length() > 9) {
          if (gender.getCheckedRadioButtonId() != -1) {
            updateProfiletoUser(userName, userLastName, userMail, userPass, moblie.getText().toString(), date);
          } else {
            Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Please select Gender!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
          }
        } else {
          Snackbar snackbar = Snackbar
              .make(coordinatorLayout, "Invalid mobile number!", Snackbar.LENGTH_LONG);
          // Changing action button text color
          View sbView = snackbar.getView();
          TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
          textView.setTextColor(Color.RED);
          snackbar.show();
        }
      } else {
        Snackbar snackbar = Snackbar
            .make(coordinatorLayout, "Enter valid email!", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
      }
    } else {
      Snackbar snackbar = Snackbar
          .make(coordinatorLayout, "Enter valid credentials!", Snackbar.LENGTH_LONG);
      // Changing action button text color
      View sbView = snackbar.getView();
      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(Color.RED);
      snackbar.show();
    }
  }

  private void updateProfiletoUser(String userName, String userLastName, String userMail, String userPass, String mobile, String dob) {
    if (networkDetector.isConnected()) {

      dialog = ProgressDialog.show(this, "", getResources()
          .getString(R.string.progress_dialog_text));
      accountsClient.getAllUpdateSettings(application.getUserId(), userName, userLastName, userMail, userPass, genderstring, mobile
          , dob, cardImageString, new WebServiceResponseCallback() {
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
    } else {
      Snackbar snackbar = Snackbar
          .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
      // Changing action button text color
      View sbView = snackbar.getView();
      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(Color.RED);
      snackbar.show();
    }

  }

  public void parseUpdateDetails(JSONObject jsonObject) {
    Log.d("update data", jsonObject.toString());
    closeDialog();
    getAllPrefs();
    try {
      JSONObject data = jsonObject.getJSONObject("data");

      String firstname = data.getString("First_name");
      String lastname = data.getString("Last_name");
      String gender = data.getString("Gender");
      String dob = data.getString("dob");
      String passWord = data.getString("Password");
      String email = data.getString("Email");
      String mobile = data.getString("Mobilenumber");
      String image = data.getString("profileimage");

      Map<String, String> userDetails = new HashMap<>();
        userDetails.put(Constants.PREFERENCE_USER_ID,data.getString("userId"));
      userDetails.put(Constants.SF_FIRST, firstname);
      userDetails.put(Constants.SF_LAST, lastname);
      userDetails.put(Constants.SF_EMAIL, email);
      userDetails.put(Constants.SF_PASS, passWord);
      userDetails.put(Constants.SF_DOB, dob);
      userDetails.put(Constants.SF_IMAGE, image);
      userDetails.put(Constants.SF_GENDER, gender);
      userDetails.put(Constants.SF_REGTYPE, "");
      userDetails.put(Constants.SF_MOBILE, mobile);
      application.setUserDetails(userDetails);

      Snackbar snackbar = Snackbar
          .make(coordinatorLayout, "Successfully Updated!", Snackbar.LENGTH_LONG);
      // Changing action button text color
      View sbView = snackbar.getView();
      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(Color.RED);
      snackbar.show();
      getLoginDetails();
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  /* get all prefs*/
  private void
  getAllPrefs() {
    if (networkDetector.isConnected()) {
           /* dialog = ProgressDialog.show(getActivity(),"",getResources()
                    .getString(R.string.progress_dialog_text));*/
      accountsClient.getAllSettings(application.getUserId(), new WebServiceResponseCallback() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
          parseUserPrefs(jsonObject);
          //closeDialog();
        }

        @Override
        public void onFailure(VolleyError error) {
          //   closeDialog();
        }
      });
    } else {
      Snackbar snackbar = Snackbar
          .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
      // Changing action button text color
      View sbView = snackbar.getView();
      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(Color.RED);
      snackbar.show();
    }
  }

  /* parse user prefs*/
  private void parseUserPrefs(JSONObject jsonObject) {
    try {
      Settingspojo settingspojo = gson.fromJson(jsonObject.toString(), Settingspojo.class);
      Data data = settingspojo.getData();
      name.setText(data.getFirstName());
      lastname.setText(data.getLastName());
      email.setText(data.getEmail());
      password.setText(data.getPassword());
      moblie.setText(data.getMobileNumber());
      imageLoader.get(data.getImage(), ImageLoader.getImageListener(inputImage
          , R.drawable.avatar, R.drawable.avatar));
      String genderValue = data.getGender();
      Log.d("gender", data.getGender());
      if (genderValue != null && genderValue.equalsIgnoreCase("0")) {
        gender.check(R.id.radioMale);
      } else {
        gender.check(R.id.radioFemale);
      }

    } catch (JsonSyntaxException e) {
      e.printStackTrace();
    } catch (final IllegalArgumentException e) {
      // Handle or log or ignore
      e.printStackTrace();
    }
  }

  /* close dialog*/
  private void closeDialog() {
    if (dialog != null) {
      if (dialog.isShowing()) {
        dialog.dismiss();
        dialog = null;
      }
    }
  }

  private void getLoginDetails() {
    Map<String, String> userDetails = application.getUserDetails();
    String firstname = userDetails.get(Constants.SF_FIRST);
    String lastName = userDetails.get(Constants.SF_LAST);
    String gender1 = userDetails.get(Constants.SF_GENDER);
    String dobbb = userDetails.get(Constants.SF_DOB);
    String notifications = userDetails.get(Constants.SF_NOTIF);
    String passWord = userDetails.get(Constants.SF_PASS);
    String eMail = userDetails.get(Constants.SF_EMAIL);
    String mobileNum = userDetails.get(Constants.SF_MOBILE);
    String image = userDetails.get(Constants.SF_IMAGE);
    // Log.d("image",image);
    name.setText(firstname);
    lastname.setText(lastName);
    if (gender1 != null && gender1.equalsIgnoreCase("0")) {
      gender.check(R.id.radioMale);
    } else {
      gender.check(R.id.radioFemale);
    }
    email.setText(eMail);
    password.setText(passWord);
    moblie.setText(mobileNum);
    dob.setText(dobbb);
    imageLoader.get(image, ImageLoader.getImageListener(inputImage
        , R.drawable.avatar, R.drawable.avatar));
       /* if(image!=null&& image!=""){
            imageLoader.get(image, ImageLoader.getImageListener(inputImage
                    , R.drawable.avatar, R.drawable.avatar));
        }*/
  }


}
