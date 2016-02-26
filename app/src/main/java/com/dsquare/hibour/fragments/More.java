package com.dsquare.hibour.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.dsquare.hibour.utils.UIHelper;

import java.util.Map;

/**
 * Created by deepthi on 2/25/16.
 */
public class More extends HibourBaseTabFragment implements View.OnClickListener {
    private static final String LOG_TAG = Message.class.getSimpleName();

    private UIHelper uiHelper;
    private TextView inviteNbours, logout, settings,name;
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
        getDetails();
        return view;
    }



    /* initialize views*/
    private void initializeViews(View view) {
        name=(TextView)view.findViewById(R.id.settings_item);
        profile=(ImageView)view.findViewById(R.id.profile_pic);
        application=Hibour.getInstance(getActivity());
        imageLoader = HibourConnector.getInstance(getActivity()).getImageLoader();
    }

    /* initialize eventlisteners*/
    private void initializeEventListeners() {
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
            case R.id.settings_item:

                break;
            case R.id.invite_item:

                break;
            case R.id.logout_item:
                break;
        }
    }

}
