package com.dsquare.hibour.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dsquare.hibour.R;

import java.util.ArrayList;
import java.util.List;

public class GovtProof extends AppCompatActivity implements View.OnClickListener
        ,AdapterView.OnItemSelectedListener{

    private Spinner cardsSpinner;
    private List<String> cardsList=new ArrayList<>();
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_proof);
        initializeViews();
        initializeEventListeners();
    }

    /*initialize views*/
    private void initializeViews(){
        next = (Button)findViewById(R.id.govt_next_button);
        cardsSpinner = (Spinner)findViewById(R.id.govt_cards_spinner);
        prepareCardsList();
        ArrayAdapter<String> cardsAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_dropdown_item_1line,cardsList);
        cardsSpinner.setAdapter(cardsAdapter);
        cardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    //((TextView) parent.getChildAt(0)).setTypeface(numbers);
                    Log.d("padding", parent.getChildAt(0).getPaddingLeft() + "");
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /*initialize event listeners*/
    private void initializeEventListeners(){
        next.setOnClickListener(this);
        //cardsSpinner.setOnItemSelectedListener(this);
    }
    /*prepare cards list*/
    private void prepareCardsList(){
        cardsList.add("Select Card");
        for(int i=0;i<10;i++){
            cardsList.add("Pan Card");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.govt_next_button:
                openLocationActivity();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /* open location activity*/
    private void openLocationActivity(){
        Intent locationIntent = new Intent(this,ChooseLocation.class);
        startActivity(locationIntent);
    }
}
