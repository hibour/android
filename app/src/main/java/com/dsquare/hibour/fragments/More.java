package com.dsquare.hibour.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.utils.UIHelper;

/**
 * Created by deepthi on 2/25/16.
 */
public class More extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String LOG_TAG = Message.class.getSimpleName();

    private UIHelper uiHelper;
    private TextView inviteNbours, logout, settings;

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
        return view;
    }

    /* initialize views*/
    private void initializeViews(View view) {
    }

    /* initialize eventlisteners*/
    private void initializeEventListeners() {
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
