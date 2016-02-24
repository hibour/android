package com.dsquare.hibour.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dsquare.hibour.R;

/**
 * Created by deepthi on 2/18/16.
 */
public class RateHibour extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity(), R.style.AppCompatAlertDialogStyle);

        alertDialog.setTitle(R.string.rate_dialogue_title);

        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_hibour, null);
        alertDialog.setView(dialogView);

        Button positive = (Button) dialogView.findViewById(R.id.btn_rate_us);
        Button negative = (Button) dialogView.findViewById(R.id.btn_send_feedback);
        Button neutral = (Button) dialogView.findViewById(R.id.btn_close);

        positive.setOnClickListener(positiveButtonClick);
        negative.setOnClickListener(negativeButtonClick);
        neutral.setOnClickListener(neutralButtonClick);

        return alertDialog.create();
    }

    private View.OnClickListener positiveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rateHibourOnPlayStore();
            dismiss();
        }
    };


    private View.OnClickListener negativeButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


    private View.OnClickListener neutralButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //flurry
            dismiss();
        }
    };

    public void rateHibourOnPlayStore() {
        //flurry
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("market://details?id=[Id]"));
        intent.setData(Uri.parse(getString(R.string.hibour_market_url)));
        if (!MyStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
//            intent.setData(Uri.parse("https://play.google.com/store/apps/details?[Id]"));
            intent.setData(Uri.parse(getString(R.string.hibour_market_url)));
            if (!MyStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(this.getActivity().getBaseContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void closeRateDialog() {
        //Flurry that the dialog is closed
        dismiss();
    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
