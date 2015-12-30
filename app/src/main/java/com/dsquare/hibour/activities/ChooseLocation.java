package com.dsquare.hibour.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dsquare.hibour.R;

public class ChooseLocation extends AppCompatActivity implements View.OnClickListener{

    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        initializeViews();
        initializeEventListeners();
    }

    /*initialize views*/
    private void initializeViews(){
        next = (Button)findViewById(R.id.location_next_button);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.location_next_button:
                openSocializeActivity();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_location, menu);
        return true;
    }

    /* open socialize activity*/
    private void openSocializeActivity(){
        Intent socialIntent = new Intent(this,SocialCategories.class);
        startActivity(socialIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
