package com.dsquare.hibour.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.dsquare.hibour.R;

public abstract class UserChatOptionDialoge extends DialogFragment {

  private static final String LOG_TAG = UserChatOptionDialoge.class.getSimpleName();
  private LayoutInflater inflater;
  private View dialogView;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    dialogView = inflater.inflate(R.layout.dialoge_user_chat_option, null);

    configureView(dialogView);

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
    alertDialogBuilder.setView(dialogView);
    setCancelable(true);
    return alertDialogBuilder.create();
  }

  private void configureView(View dialogView) {
    dialogView.findViewById(R.id.delete_chat).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteChat();
        dismiss();
      }
    });
  }

  public abstract void deleteChat();
}
