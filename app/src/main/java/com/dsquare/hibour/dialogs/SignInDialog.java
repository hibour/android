package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;

/**
 * Created by Dsquare Android on 2/25/2016.
 */
public class SignInDialog extends android.app.DialogFragment implements View.OnClickListener {


  private TextView text, ok;
  private ImageView close;
  private SignInCallback callback;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Log.d("Welcome dialog", "welcome");
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.signin_dialog, null);
    builder.setView(view);
    initializeViews(view);
    initializeEventListeners();
    return builder.create();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      callback = (SignInCallback) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement NoticeDialogListener");
    }
  }

  private void initializeViews(View view) {
    text = (TextView) view.findViewById(R.id.signdailog_text);
    ok = (TextView) view.findViewById(R.id.signdailog_ok);
  }

  private void initializeEventListeners() {
    ok.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    callback.closeDialog(this);
  }

  public interface SignInCallback {
    void closeDialog(SignInDialog dialogFragment);
  }
}
