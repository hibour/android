package com.dsquare.hibour.fragments;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deepthi on 1/22/16.
 */

public class CreatePost extends android.support.v4.app.Fragment implements View.OnClickListener {

    private String category;

    private Button send;
    private ImageView gallary;
    private EditText text;
    private Typeface proxima;
    private DialogFragment chooserDialog;
    private ArrayAdapter<String> categoriesAdapter;
    private String[] List={"General","Suggestions", "Classifieds", "Crime & Safety", "Lost & Found"};
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
    private LinearLayout generalCat, suggestionsCat, classifiedsCat, crimeSafetyCat, lostFoundCat;
    private EditText editPost;
    public CreatePost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createpost, container, false);
        initializeViews(view);
        initializeEventListeners();
        return view;
    }

    private void initializeEventListeners() {
        generalCat.setOnClickListener(this);
        suggestionsCat.setOnClickListener(this);
        classifiedsCat.setOnClickListener(this);
        crimeSafetyCat.setOnClickListener(this);
        lostFoundCat.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_general:
                onCategorySelected("general");
                break;

            case R.id.category_suggestions:
                onCategorySelected("suggestions");
                break;

            case R.id.category_classifieds:
                onCategorySelected("classifieds");
                break;

            case R.id.category_crime_safety:
                onCategorySelected("crime_safety");
                break;

            case R.id.category_lost_found:
                onCategorySelected("lost_found");
                break;
            case R.id.newpost_send:
                validatepostData();
                break;
        }
    }

    private void onCategorySelected(String cat) {
        category = cat;
        LinearLayout postFragment = (LinearLayout) this.getActivity().findViewById(R.id.post_fragment);
        LinearLayout postWidget = (LinearLayout) this.getActivity().findViewById(R.id.post_widget);
        LinearLayout categoryList = (LinearLayout) this.getActivity().findViewById(R.id.category_list);

        ViewGroup.LayoutParams lp = postFragment.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        postFragment.setGravity(Gravity.TOP);
        postFragment.setLayoutParams(lp);

        categoryList.setVisibility(View.GONE);
        postWidget.setVisibility(View.VISIBLE);

        setPlaceholderText(category);
    }

    private void setPlaceholderText(String category) {
        switch (category) {
            case "general":
                editPost.setHint(R.string.cat_ph_general);
                break;

            case "suggestions":
                editPost.setHint(R.string.cat_ph_suggestions);
                break;

            case "classifieds":
                editPost.setHint(R.string.cat_ph_classifieds);
                break;

            case "crime_safety":
                editPost.setHint(R.string.cat_ph_crime_safety);
                break;

            case "lost_found":
                editPost.setHint(R.string.cat_ph_lost_found);
                break;

            default:
                editPost.setHint(R.string.cat_ph_general);
                break;
        }
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    /*validate proof data*/
    private void validatepostData(){
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

    /* send data to server*/
    private void sendPostData(String posttypeid,String postMessage,String postImage){
        if(networkDetector.isConnected()){
            String cat_str = category;
            newpostDialogue = ProgressDialog.show(getActivity(),"",getResources()
                    .getString(R.string.progress_dialog_text));
            postsClient.insertonPost(application.getUserId(),cat_str,posttypeid,postMessage,postImage
                    ,posttypeid,new WebServiceResponseCallback() {
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
                text.setText("");
            }else{
                Toast.makeText(getActivity(),"Post updation failed",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
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

    private void initializeViews(View view) {
        generalCat = (LinearLayout)view.findViewById(R.id.category_general);
        suggestionsCat = (LinearLayout)view.findViewById(R.id.category_suggestions);
        classifiedsCat = (LinearLayout)view.findViewById(R.id.category_classifieds);
        crimeSafetyCat = (LinearLayout)view.findViewById(R.id.category_crime_safety);
        lostFoundCat = (LinearLayout)view.findViewById(R.id.category_lost_found);

        editPost = (EditText) view.findViewById(R.id.newposts_edittest);

        application = Hibour.getInstance(getActivity());
        send = (Button)view.findViewById(R.id.newpost_send);
        text = (EditText)view.findViewById(R.id.newposts_edittest);
        postImage = (ImageView)view.findViewById(R.id.post_image);
        networkDetector = new NetworkDetector(getActivity());
        postsClient = new PostsClient(getActivity());
        gson = new Gson();

        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        send.setTypeface(proxima);
    }


}
