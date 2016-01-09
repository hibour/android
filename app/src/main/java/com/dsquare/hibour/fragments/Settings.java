package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.NavDrawerCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment implements View.OnClickListener{

    private ImageView menuIcon,notifIcon,inputImage;
    private NavDrawerCallback callback;
    private Button submitButton;

    private EditText inputName, inputEmail, inputPassword;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private int PICK_IMAGE_REQUEST = 1;
    private Typeface avenir;

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
        return view;
    }
    /* initialize views*/
    private void initializeViews(View view){
        menuIcon = (ImageView)view.findViewById(R.id.settings_menu_icon);
        notifIcon = (ImageView)view.findViewById(R.id.settings_notif_icon);
        submitButton = (Button)view.findViewById(R.id.settings_submit);

        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);
        inputName = (EditText) view.findViewById(R.id.input_name);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);
        inputImage = (ImageView) view.findViewById(R.id.input_image);
        avenir = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AvenirLTStd-Book.otf");
        submitButton.setTypeface(avenir);
        inputLayoutEmail.setTypeface(avenir);
        inputLayoutName.setTypeface(avenir);
        inputLayoutPassword.setTypeface(avenir);
        inputName.setTypeface(avenir);
        inputEmail.setTypeface(avenir);
        inputPassword.setTypeface(avenir);
    }
    /* initialize event listeners*/
     private void initializeEventListeners(){
         menuIcon.setOnClickListener(this);
         notifIcon.setOnClickListener(this);
         submitButton.setOnClickListener(this);
     }

    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.settings_menu_icon:
             callback.drawerOpen();
             break;
         case R.id.settings_notif_icon:
             break;
         case R.id.settings_submit:
             break;
     }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getActivity(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
}
