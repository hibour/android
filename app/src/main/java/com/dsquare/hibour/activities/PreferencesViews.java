package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NeighboursAdapter;
import com.dsquare.hibour.interfaces.NavDrawerCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.pojos.Socialize.ChoosedUser;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 1/25/2016.
 */
public class PreferencesViews extends AppCompatActivity implements View.OnClickListener {
    private ImageView menuIcon,searchIcon;
    private RelativeLayout searchLayout;
    private RecyclerView neighboursRecycler;
    private NeighboursAdapter adapter;
    private List<UserDetail> neighboursList = new ArrayList<>();
    private NetworkDetector networkDetector;
    private ProgressDialog dialog;
    private NavDrawerCallback callback;
    private boolean frmAdapter = false;
    private TextView neighboursTitle;
    private Typeface proxima;
    private List<String> autocompleteList = new ArrayList<>();
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefernceview);
        frmAdapter = getIntent().getBooleanExtra("frmAdapter",false);
        prepareNeighboursList();
        initializeViews();
        initializeEventListeners();
    }
    /* initialize views*/
    private void initializeViews(){
        //frmAdapter = getIntent().getBooleanExtra("frmAdapter",false);
        menuIcon = (ImageView)findViewById(R.id.messages_menu_icon);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.neighbours_search_autocomplete);
        proxima = Typeface.createFromAsset(getAssets(), Fonts.getTypeFaceName());
        searchLayout = (RelativeLayout)findViewById(R.id.neighbours_search_layout);
        searchIcon = (ImageView)findViewById(R.id.messages_search_icon);
        neighboursTitle = (TextView)findViewById(R.id.neighbours_title);
        neighboursRecycler = (RecyclerView)findViewById(R.id.prefernce_neighbours_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        neighboursRecycler.setLayoutManager(layoutManager);
        neighboursRecycler.setHasFixedSize(true);
        adapter = new NeighboursAdapter(this,neighboursList);
        neighboursRecycler.setAdapter(adapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line,
                        autocompleteList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName = autoCompleteTextView.getText().toString();
                if (parent != null && parent.getChildAt(0) != null) {
                    String neighbourName = autocompleteList.get(position);
                    Log.d("neighbourName", neighbourName);
//                        if(!cardType.equals("Select Card")){
//                            cardTypeId = searchMap.get(cardType);
//                            Log.d("cardtype",cardType);
//                        }
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.black_1));
                    ((TextView) parent.getChildAt(0)).setTypeface(proxima);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    Log.d("itemname", itemName);
//                setRecyclerList(itemName);
                }}
        });
    }
    /* initialize eventlisteners*/
    private void initializeEventListeners(){
        menuIcon.setOnClickListener(this);
        searchIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.messages_menu_icon:
                this.finish();
                break;
            case R.id.messages_search_icon:
                if(searchLayout.getVisibility()==View.GONE){
                    searchLayout.setVisibility(View.VISIBLE);
                    neighboursTitle.setVisibility(View.GONE);
                }else{
                    searchLayout.setVisibility(View.GONE);
                    neighboursTitle.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    /* prepare neighbours list*/
    private void prepareNeighboursList(){
        for(int i=0;i<10;i++){
            UserDetail user = new UserDetail();
            user.id = i;
            user.Username = "Ashok Madduru";
            user.Address = "Hardware Engineer";
            neighboursList.add(user);
        if(frmAdapter){
            List<ChoosedUser> user = Constants.prefsMap.get(getIntent()
                    .getStringExtra("prefId")).getChoosedUsers();
            for(int i=0;i< user.size();i++){
                if(user.get(i).getUserName()!=null &&!user.get(i).getUserName().equals("null")){
                    String[] data = new String[3];
                    data[0] = user.get(i).getUserName();
                    data[1] = user.get(i).getUserId();
                    data[2] = user.get(i).getUserImage();
                    neighboursList.add(data);
                    autocompleteList.add(user.get(i).getUserName());
                }
            }
        }else{
            for(int i=0;i< Constants.membersList.size();i++){
                if(Constants.membersList.get(i).getUserName()!=null &&
                        !Constants.membersList.get(i).getUserName().equals("null")){
                    String[] data = new String[3];
                    data[0] = Constants.membersList.get(i).getUserName();
                    data[1] = Constants.membersList.get(i).getUserId();
                    data[2] = Constants.membersList.get(i).getUserImage();
                    neighboursList.add(data);
                    autocompleteList.add(Constants.membersList.get(i).getUserName());
                }
            }
        }
    }
}
