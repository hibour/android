package com.dsquare.hibour.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsquare.hibour.R;

public class TermsAndConditions extends AppCompatActivity implements View.OnClickListener {

    private TextView termsText;
    private ImageView closeTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
    }
    /* initialize Views*/
    private void initializeViews(){
        termsText = (TextView)findViewById(R.id.terms_text);
    }
    /* initialize Event listeners*/

    @Override
    public void onClick(View v) {

    }
}
