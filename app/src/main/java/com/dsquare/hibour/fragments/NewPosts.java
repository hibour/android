package com.dsquare.hibour.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.dialogs.PostsImagePicker;
import com.dsquare.hibour.interfaces.ImagePicker;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posttype.Datum;
import com.dsquare.hibour.pojos.posttype.PostTypeCatg;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Aditya Ravikanti on 1/19/2016.
 */
public class NewPosts extends Fragment implements View.OnClickListener,ImagePicker {

    private Spinner spinner;
    private Button send;
    private ImageView gallary;
    private EditText text;
    private Typeface proxima;
    private DialogFragment chooserDialog;
    private ArrayAdapter<String> categoriesAdapter;
    private String[] List={"Suggestions","Classifields"};
    private static final int REQUEST_IMAGE_SELECTOR=1000;
    private static final int REQUEST_IMAGE_CAPTURE=1001;
    private List<String> categoriesList = new ArrayList<>();
    private Map<String,String> categoriesMap = new LinkedHashMap<>();
    private NetworkDetector networkDetector;
    private PostsClient postsClient;
    private ProgressDialog newpostDialogue;
    private Gson gson;
    private String categoriesString="",postimagesstring="aa";
    private String categoriesTypeId="";
    private Bitmap bitmap;
    private Hibour application;
    private ImageView postImage;
    public NewPosts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newposts, container, false);
        initializeViews(view);
        initializeEventListeners();
        getAllCategoriesTypes();
        return view;
    }

    private void initializeViews(View view) {
        application = Hibour.getInstance(getActivity());
        spinner = (Spinner)view.findViewById(R.id.newpost_spinner);
        send = (Button)view.findViewById(R.id.newpost_send);
        gallary = (ImageView)view.findViewById(R.id.newposts_gallary);
        text = (EditText)view.findViewById(R.id.newposts_edittest);
        postImage = (ImageView)view.findViewById(R.id.post_image);
        networkDetector = new NetworkDetector(getActivity());
        postsClient = new PostsClient(getActivity());
        gson = new Gson();
        prepareCategoriesList();
        //categoriesAdapter = new ArrayAdapter<String>(getActivity()
        //        ,android.R.layout.simple_dropdown_item_1line,categoriesList);
        //spinner.setAdapter(categoriesAdapter);

        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        send.setTypeface(proxima);
        categoriesAdapter = new ArrayAdapter<String>(getActivity()
                ,android.R.layout.simple_dropdown_item_1line,List);
        spinner.setAdapter(categoriesAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String categoriesType = categoriesList.get(position);
                    Log.d("categoriestype", categoriesType);
                    if (!categoriesType.equals("Select Categories")) {
                        categoriesTypeId = categoriesMap.get(categoriesType);
                        Log.d("categoriestype", categoriesType);
                    }
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTypeface(proxima);
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
    private void initializeEventListeners() {

        gallary.setOnClickListener(this);
        send.setOnClickListener(this);
    }
    /*prepare cards list*/
    private void prepareCategoriesList(){
        categoriesList.add("Select Categories");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.newposts_gallary:
                openImageChooser();
                break;
            case R.id.newpost_send:
                validatepostData();
                break;
        }
    }

    /*validate proof data*/
    private void validatepostData(){
//        Log.d("cardtype",cardTypeId);
        Log.d("postmsg",text.getText().toString());
        Log.d("postimage", postimagesstring);
        Log.d("categories", categoriesString);
        if(!categoriesTypeId.equals("")&&text.getText().toString().equals(null)
                &&!text.getText().toString().equals("null")&&
                !text.getText().toString().equals("")&&
                !postimagesstring.equals("")){
            Toast.makeText(getActivity(),"All fields are required",Toast.LENGTH_LONG).show();
//            sendPostData(categoriesTypeId ,text.getText().toString()
//                    , postimagesstring);
        }else{
            Log.d("userid",application.getUserId());
            sendPostData(categoriesTypeId, text.getText().toString()
                    , postimagesstring);
        }
    }

    private void openImageChooser(){
        chooserDialog = new PostsImagePicker();
        chooserDialog.show(getActivity().getSupportFragmentManager(), "chooser dialog");
        chooserDialog.setTargetFragment(this, 0);
    }


    /* open gallary intent*/
    private void openGallary(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
    }
    /* open camera*/
    private void openCamera(){
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK
                &&  data != null && data.getData() != null) {
//            imageUploaded.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if(postImage.getVisibility()==View.GONE){
                    postImage.setVisibility(View.VISIBLE);
                    postImage.setImageBitmap(bitmap);
                }
                postimagesstring=getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK
                &&  data != null && data.getData() != null){
//            imageUploaded.setVisibility(View.VISIBLE);
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                if(postImage.getVisibility()==View.GONE){
                    postImage.setVisibility(View.VISIBLE);
                    postImage.setImageBitmap(bitmap);
                }
                postimagesstring=getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* send data to server*/
    private void sendPostData(String posttypeid,String postMessage,String postImage){
        if(networkDetector.isConnected()){
            newpostDialogue = ProgressDialog.show(getActivity(),"",getResources()
                    .getString(R.string.progress_dialog_text));
            postsClient.insertonPost(application.getUserId(),posttypeid,posttypeid,postMessage,postImage
                    ,posttypeid,"",new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parsePostDetails(jsonObject);
                    closePostDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt", error.toString());
                    closePostDialog();
                }
            });
        }else{
            Toast.makeText(getActivity(),"Network connection error",Toast.LENGTH_LONG).show();
        }
    }

    /* parse insert post  data*/
    private void parsePostDetails(JSONObject jsonObject){
        Log.d("json",jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            String result = data.getString("result");
            if(result.endsWith("true")){
                Toast.makeText(getActivity(),"Post update successfully",Toast.LENGTH_LONG).show();
                postImage.setVisibility(View.GONE);
                spinner.setSelection(0);
                text.setText("");
            }else{
                Toast.makeText(getActivity(),"Post updation failed",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /* get all categories types*/
    private void getAllCategoriesTypes(){
        Log.d("NEWPOST","categories");
        if(networkDetector.isConnected()){
            newpostDialogue = ProgressDialog.show(getActivity(),"",getResources()
                    .getString(R.string.progress_dialog_text));
            postsClient.getAllcategoriesTypes(new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseAllCategoriesTypes(jsonObject);
                    closePostDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("govt", error.toString());
                    closePostDialog();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_LONG).show();
        }
    }
    /* parse categories types*/
    private void parseAllCategoriesTypes(JSONObject jsonObject){
        try {
            PostTypeCatg proofsData = gson.fromJson(jsonObject.toString(),PostTypeCatg.class);
            List<Datum> data = proofsData.getData();
            if(data.size()>0) {
                categoriesList.clear();
                categoriesList.add("Select Categories");
                categoriesMap.clear();
                for (Datum d : data) {
                    categoriesMap.put(d.getPosttypename(), d.getId() + "");
                    categoriesList.add(d.getPosttypename());
                    Log.d("new Post", d.getId() + "");
                    Log.d("new post",d.getPosttypename());
                }
                categoriesAdapter = new ArrayAdapter<String>(getActivity()
                        , android.R.layout.simple_dropdown_item_1line, categoriesList);
                spinner.setAdapter(categoriesAdapter);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    /* close post dialog*/
    private void closePostDialog(){
        if(newpostDialogue!=null){
            if(newpostDialogue.isShowing()){
                newpostDialogue.dismiss();
                newpostDialogue=null;
            }
        }
    }
    /* convert image to string*/
    public String getStringImage(Bitmap bmp){
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
//        bitmap = BitmapFactory.decodeFile(imgPath,
//                options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void pickerSelection(int choice) {
        switch (choice){
            case 0:
                openGallary();
                break;
            case 1:
                openCamera();
                break;
        }
        chooserDialog.dismiss();
    }

}