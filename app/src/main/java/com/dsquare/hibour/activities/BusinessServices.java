package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.AccountsClient;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.pojos.business.BusinessType;
import com.dsquare.hibour.pojos.business.Datum;
import com.dsquare.hibour.pojos.businesssubtype.BusinessSubType;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Aditya Ravikanti on 1/30/2016.
 */
public class BusinessServices extends AppCompatActivity implements View.OnClickListener {
    private Spinner services,subtypes;
    private Button sumbit;
    private NetworkDetector networkDetector;
    private AccountsClient accountsClient;
    private ProgressDialog uploadProofsDialog;
    private Gson gson;
    private Typeface tf;
    private ArrayAdapter<String> servicesAdapter,subservicesAdapter;
    private List<String> servicesList=new ArrayList<>();
    private List<String> subservicesList=new ArrayList<>();
    private Map<String,String> serviceMap = new LinkedHashMap<>();
    private Map<String,String> subserviceMap = new LinkedHashMap<>();
    private String serviceTypeId = "",subserviceId="";
    private Hibour application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_services);
        initializeViews();
        initializeEventListeners();
        getAllBusinessserviceTypes();
    }

    private void initializeViews() {
         tf = Typeface.createFromAsset(getAssets(),
                "fonts/pn_regular.otf");
        services = (Spinner) findViewById(R.id.business_services_shops);
        subtypes = (Spinner) findViewById(R.id.business_services_types);
        sumbit = (Button) findViewById(R.id.business_services_submit);
        networkDetector = new NetworkDetector(this);
        accountsClient = new AccountsClient(this);
        gson = new Gson();
        application = Hibour.getInstance(this);

        servicesAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_dropdown_item_1line,servicesList);
            subservicesAdapter = new ArrayAdapter<String>(this
                ,android.R.layout.simple_dropdown_item_1line,subservicesList);
        services.setAdapter(servicesAdapter);
        subtypes.setAdapter(subservicesAdapter);
        services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String serviceType = servicesList.get(position);
                    Log.d("serviceType",serviceType);
                    if(!serviceType.equals("Select Business Services")){
                        serviceTypeId = serviceMap.get(serviceType);
                        Log.d("serviceType",serviceType);
                    }
                    getAllBusinessserviceSubTypes();
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.black_1));
                    ((TextView) parent.getChildAt(0)).setTypeface(tf);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subtypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String subtype = subservicesList.get(position);
                    Log.d("servicesubType",subtype);
                    if(!subtype.equals("Select Categories")){
                        subserviceId = subserviceMap.get(subtype);
                        Log.d("servicesubType",subtype);
                    }
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.black_1));
                    ((TextView) parent.getChildAt(0)).setTypeface(tf);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeEventListeners() {
        sumbit.setOnClickListener(this);
    }

    /*prepare cards list*/
    private void prepareCardsList(){
        servicesList.add("Select Business Services");
        subservicesList.add("Select Categories");
    }


    @Override
    public void onClick(View v) {
        openSocilizeScreen();
    }

    private void openSocilizeScreen() {
        Intent intent = new Intent(this,SocialCategories.class);
        startActivity(intent);
        this.finish();
    }
    /* get all businessservices types*/
    private void getAllBusinessserviceTypes(){
        if(networkDetector.isConnected()){
            uploadProofsDialog = ProgressDialog.show(this, "", getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getAllBusinessServiceTypes(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseAllBusinessserviceTypes(jsonObject);
                    closebusinessDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("busi", error.toString());
                    closebusinessDialog();
                }
            });
        }else{
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }
    private void parseAllBusinessserviceTypes(JSONObject jsonObject) {
        try {
            BusinessType businessType = gson.fromJson(jsonObject.toString(), BusinessType.class);
            List<com.dsquare.hibour.pojos.business.Datum> data = businessType.getData();
            if(data.size()>0){
                servicesList.clear();
                servicesList.add("Select Business Services");
                serviceMap.clear();
                for(Datum d:data){
                    serviceMap.put(d.getBusinessName(), d.getId() + "");
                }
                servicesAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_dropdown_item_1line,servicesList);
                services.setAdapter(servicesAdapter);
            }
        }catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }

    /* get all businessservices subtypes*/
    private void getAllBusinessserviceSubTypes(){
        if(networkDetector.isConnected()){
            uploadProofsDialog = ProgressDialog.show(this, "", getResources()
                    .getString(R.string.progress_dialog_text));
            accountsClient.getAllBusinessServiceSubTypes(application.getUserId(),serviceTypeId,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseAllBusinessserviceSubTypes(jsonObject);
                    closebusinessDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("busi", error.toString());
                    closebusinessDialog();
                }
            });
        }else{
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void parseAllBusinessserviceSubTypes(JSONObject jsonObject) {
        try {
            BusinessSubType businessSubType = gson.fromJson(jsonObject.toString(), BusinessSubType.class);
            List<com.dsquare.hibour.pojos.businesssubtype.Datum> data = businessSubType.getData();
            if(data.size()>0){
                subservicesList.clear();
                subservicesList.add("Select Categories");
                subserviceMap.clear();
                for(com.dsquare.hibour.pojos.businesssubtype.Datum d:data){
                    subserviceMap.put(d.getServiceName(), d.getId() + "");
                }
                subservicesAdapter = new ArrayAdapter<String>(this
                        ,android.R.layout.simple_dropdown_item_1line,subservicesList);
                subtypes.setAdapter(subservicesAdapter);
            }
        }catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    /* close proof dialog*/
    private void closebusinessDialog(){
        if(uploadProofsDialog!=null){
            if(uploadProofsDialog.isShowing()){
                uploadProofsDialog.dismiss();
                uploadProofsDialog=null;
            }
        }
    }
}


