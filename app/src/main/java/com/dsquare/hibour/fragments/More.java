package com.dsquare.hibour.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Settings;
import com.dsquare.hibour.activities.SignIn;
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by deepthi on 2/25/16.
 */
public class More extends HibourBaseTabFragment implements View.OnClickListener {
    private static final String LOG_TAG = Message.class.getSimpleName();

    private UIHelper uiHelper;
    private TextView inviteNbours, logout, settings,name;
    private RelativeLayout userLayout,inviteLayout,logoutLayout;
    private ImageView profile;
    private Hibour application;
    private ImageLoader imageLoader;


    public More() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        uiHelper = new UIHelper(this.getContext());

        settings = (TextView) view.findViewById(R.id.settings_item);
        inviteNbours = (TextView) view.findViewById(R.id.invite_item);
        logout = (TextView) view.findViewById(R.id.logout_item);

        settings.setOnClickListener(this);
        initializeViews(view);
        initializeEventListeners();
        getDetails();
        return view;
    }



    /* initialize views*/
    private void initializeViews(View view) {
        name=(TextView)view.findViewById(R.id.settings_item);
        profile=(ImageView)view.findViewById(R.id.profile_pic);
        application=Hibour.getInstance(getActivity());
        userLayout=(RelativeLayout)view.findViewById(R.id.more_userLayout);
        inviteLayout=(RelativeLayout)view.findViewById(R.id.more_inviteFriendslayout);
        logoutLayout=(RelativeLayout)view.findViewById(R.id.more_logoutlayout);
        imageLoader = HibourConnector.getInstance(getActivity()).getImageLoader();
    }

    /* initialize eventlisteners*/
    private void initializeEventListeners() {
        userLayout.setOnClickListener(this);
        inviteLayout.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
    }
    private void getDetails() {
        Map<String,String> details = application.getUserDetails();
        String firstname=details.get(Constants.SF_FIRST);
        String lastname=details.get(Constants.SF_LAST);
        String image=details.get(Constants.SF_IMAGE);
        String username=firstname + " " + lastname;
        name.setText(username);
        if(image!=null&& image!="") {
            imageLoader.get(image, ImageLoader.getImageListener(profile, R.drawable.avatar, R.drawable.avatar));
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.more_userLayout:
                Intent openSettings=new Intent(getActivity(), Settings.class);
                startActivity(openSettings);
                break;
            case R.id.more_inviteFriendslayout:
                inviteFriends(getString(R.string.invite_msg));
                break;
            case R.id.more_logoutlayout:
                Intent openSignin=new Intent(getActivity(), SignIn.class);
                startActivity(openSignin);
                break;
        }
    }
    /*invite friends*/
    private void inviteFriends(String invitationMessage) {
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
        sendIntent.setType("text/plain");

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            Log.d("Package Name", packageName);
            if (packageName.contains("android.talk")
                    || packageName.contains("facebook")
                    || packageName.contains("whatsapp")
                    || packageName.contains("mms")
                    || packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
                intent.setPackage(packageName);
                targetShareIntents.add(intent);
            }
        }
        if (!targetShareIntents.isEmpty()) {
            System.out.println("Have Intent");
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to invite friends");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        } else {

        }
    }

}
