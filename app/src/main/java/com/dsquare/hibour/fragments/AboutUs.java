package com.dsquare.hibour.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Notifications;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AboutUsClient;
import com.dsquare.hibour.network.NetworkDetector;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment implements View.OnClickListener{

    private ImageView menuIcon,notifIcon;
    private TextView aboutText;
    private NetworkDetector networkDetector;
    private ProgressDialog dialog;
    private AboutUsClient client;
    private NavDrawerCallback callback;
    public AboutUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        initializeViews(view);
        initializeEventListeners();
        return view;
    }

    /* initialize views*/
    private void initializeViews(View view){
        menuIcon = (ImageView)view.findViewById(R.id.about_us_menu_icon);
        notifIcon = (ImageView)view.findViewById(R.id.about_us_notif_icon);
        aboutText = (TextView)view.findViewById(R.id.about_us_text);
        networkDetector = new NetworkDetector(getActivity());
    }

    /* initialize eventListeners*/
    private void initializeEventListeners(){
        menuIcon.setOnClickListener(this);
        notifIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_us_menu_icon:
                callback.drawerOpen();
                break;
            case R.id.about_us_notif_icon:
                openNotifications();
                break;
        }
    }
    /* open notifications icon*/
    private void openNotifications(){
        Intent notifIntent = new Intent(getActivity(), Notifications.class);
        startActivity(notifIntent);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (NavDrawerCallback) activity;
    }
    /*get about us data*/
    private void getAboutUsData(){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(getActivity(),""
                    ,getActivity().getResources().getString(R.string.progress_dialog_text));
            client.getAboutUs(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("about us",error.toString());
                }
            });
        }else{
            Toast.makeText(getActivity(),"No network connection",Toast.LENGTH_LONG).show();
        }
    }
    /* close dialog*/
    private void closeDialog(){
        if(dialog!=null){
            if (dialog.isShowing()){
                dialog.dismiss();
                dialog=null;
            }
        }
    }
}
