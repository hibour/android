package com.dsquare.hibour.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.dsquare.hibour.pojos.neighours.Neighourhoodpojo;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dsquare Android on 2/1/2016.
 */

public class NewPost extends android.support.v4.app.Fragment implements View.OnClickListener
        ,ImagePicker,AdapterView.OnItemClickListener {
    private String category;
    private Button send;
    private EditText text;
    private Typeface proxima;
    private DialogFragment chooserDialog;
    private TextView done,cancel;
    private ArrayAdapter<String> categoriesAdapter;
    private ArrayAdapter<String> neighoursAdapter;
    private static final int REQUEST_IMAGE_SELECTOR = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private java.util.List<String> categoriesList = new ArrayList<>();
    private java.util.List<String[]> categoriesLists = new ArrayList<>();
    private Map<String, String> categoriesMap = new LinkedHashMap<>();
    private java.util.List<String> neighourList = new ArrayList<>();
    private Map<String, String> neighourMap = new LinkedHashMap<>();
    private NetworkDetector networkDetector;
    private PostsClient postsClient;
    private ProgressDialog newpostDialogue;
    private Gson gson;
    private String categoriesString = "", postimagesstring = "aa";
    private String categoriesTypeId = "",neighoursTypeId = "";
    private Bitmap bitmap;
    private Hibour application;
    private ImageView postImage,gallary,delete;
    private EditText editPost;
    private Spinner categoriesSpinner,neighboursSpinner;
    private RelativeLayout layout,layout1;
    private View views;
    private ListView categoriesRecycler;
    private String[] details;
    private String catFrmPre="";

    public interface PostsListener{
        void onCancelClicked();
        void onDoneClicked();
    }
    private PostsListener mListener;
    public NewPost() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PostsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post_screen, container, false);
        catFrmPre = getArguments().getString("category","");
        initializeViews(view);
        initializeEventListeners();
        getNeighbourHoods(application.getUserId());
        setCategories();
        return view;
    }

    private void initializeEventListeners() {
//        categoriesRecycler.setOnItemClickListener(this);
        done.setOnClickListener(this);
        gallary.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        editPost.requestFocus();
        /*editPost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.showSoftInput(editPost, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_post_done:
                validatepostData();
                break;
            case R.id.creat_imageview_post_icon:
                openImageChooser();
                break;
            case R.id.create_delete_image:
                opendeleteImage();
                break;
            case R.id.creat_post_cancel:
                mListener.onCancelClicked();
        }
    }

    private void opendeleteImage() {
        gallary.setVisibility(View.VISIBLE);
//        postImage.setImageBitmap(null);
        layout.setVisibility(View.GONE);
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
                if(layout.getVisibility()==View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    postImage.setImageBitmap(bitmap);
                    gallary.setVisibility(View.GONE);
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
                if(layout.getVisibility()==View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    postImage.setImageBitmap(bitmap);
                    gallary.setVisibility(View.GONE);
                }
                postimagesstring=getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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



    public void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    /*validate proof data*/
    private void validatepostData() {
        if (!categoriesTypeId.equals("") && text.getText().toString().equals(null)
                && !text.getText().toString().equals("null") &&
                !text.getText().toString().equals("") &&
                !postimagesstring.equals("")) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
//            sendPostData(categoriesTypeId ,text.getText().toString()
//                    , postimagesstring);
        } else {
            Log.d("userid", application.getUserId());
            sendPostData("1", text.getText().toString()
                    , postimagesstring);
        }
    }

    /* send data to server*/
    private void sendPostData(String posttypeid, String postMessage, String postImage) {
        if (networkDetector.isConnected()) {
            String cat_str = categoriesTypeId;
            newpostDialogue = ProgressDialog.show(getActivity(), "", getResources()
                    .getString(R.string.progress_dialog_text));
            postsClient.insertonPost(application.getUserId(), cat_str, posttypeid, postMessage, postImage
                    , "1", new WebServiceResponseCallback() {
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
        } else {
            Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    /* parse insert post  data*/
    private void parsePostDetails(JSONObject jsonObject) {
        Log.d("json", jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Toast.makeText(getActivity(), "Post update successfully", Toast.LENGTH_LONG).show();
            mListener.onDoneClicked();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Post updation failed", Toast.LENGTH_LONG).show();
        }

    }

    /* close post dialog*/
    private void closePostDialog() {
        if (newpostDialogue != null) {
            if (newpostDialogue.isShowing()) {
                newpostDialogue.dismiss();
                newpostDialogue = null;
            }
        }
    }

    private void initializeViews(View view) {
        //  bgColors = getActivity().getResources().getStringArray(R.array.movie_serial_bg);
        categoriesSpinner = (Spinner)view.findViewById(R.id.newpost_categories_spinner);
        neighboursSpinner = (Spinner)view.findViewById(R.id.newpost_neighbourhood_spinner);
        editPost = (EditText) view.findViewById(R.id.newposts_edittest);
        done = (TextView) view.findViewById(R.id.create_post_done);
         cancel = (TextView) view.findViewById(R.id.creat_post_cancel);
        gallary = (ImageView) view.findViewById(R.id.creat_imageview_post_icon);
        postImage = (ImageView) view.findViewById(R.id.creat_imageview_display_icon);
        delete = (ImageView) view.findViewById(R.id.create_delete_image);
        layout = (RelativeLayout)view.findViewById(R.id.create_relative);
        layout1 = (RelativeLayout)view.findViewById(R.id.home_app_bar1);
        views = (View)view.findViewById(R.id.views);
        application = Hibour.getInstance(getActivity());
        // categoriesRecycler = (ListView) view.findViewById(R.id.categories_recycler);

        prepareCategoriesList();
//        send = (Button) view.findViewById(R.id.newpost_send);
        text = (EditText) view.findViewById(R.id.newposts_edittest);
//        postImage = (ImageView) view.findViewById(R.id.post_image);
        networkDetector = new NetworkDetector(getActivity());
        postsClient = new PostsClient(getActivity());
        gson = new Gson();

        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        categoriesAdapter = new ArrayAdapter<String>(getActivity()
                ,android.R.layout.simple_dropdown_item_1line,categoriesList);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String categoriesType = categoriesList.get(position);
                    Log.d("categoriestype", categoriesType);
                    if (!categoriesType.equals("Select Categories")) {
                        categoriesTypeId = Constants.postTypesMap.get(categoriesType).get("id");
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

        neighoursAdapter = new ArrayAdapter<String>(getActivity()
                ,android.R.layout.simple_dropdown_item_1line,neighourList);
        neighboursSpinner.setAdapter(neighoursAdapter);
        neighboursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String categoriesType = neighourList.get(position);
                    Log.d("categoriestype", categoriesType);
                    if (!categoriesType.equals("Select neighbours")) {
                        neighoursTypeId = neighourMap.get(categoriesType);
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


    /*get all neighbourhoods */
    private void getNeighbourHoods(String userId){
        if(networkDetector.isConnected()){
//            newpostDialogue = ProgressDialog.show(getActivity(),"","Please Wait...");
            postsClient.getAllNeighbourhoods(userId,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseNeighbourHoods(jsonObject);
                    closePostDialog();
                }
                @Override
                public void onFailure(VolleyError error) {
                    closePostDialog();
                    Log.d("error",error.toString());
                }
            });
        }else{

        }
    }
    /* parse neighbourhoods*/
    private void parseNeighbourHoods(JSONObject jsonObject){
        Log.d("data",jsonObject.toString());
        try {
            Neighourhoodpojo neighour = gson.fromJson(jsonObject.toString(), Neighourhoodpojo.class);
            List<com.dsquare.hibour.pojos.neighours.Datum> data = neighour.getData();
            if(data.size()>0) {
                neighourList.clear();
                neighourList.add("Select neighbours");
                neighourMap.clear();
                for (com.dsquare.hibour.pojos.neighours.Datum d : data) {
                    neighourMap.put(d.getAddress(), d.getId() + "");
                    if(!d.getAddress().equals("") && !d.getAddress().equals(null)
                            && !d.getAddress().equals("null")) {
                        if(!neighourList.contains(d.getAddress())){
                            neighourList.add(d.getAddress());
                        }
                    }
                }
                neighoursAdapter = new ArrayAdapter<String>(getActivity()
                        , android.R.layout.simple_dropdown_item_1line, neighourList);
                neighboursSpinner.setAdapter(neighoursAdapter);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    private void prepareCategoriesList(){
//        categoriesList.add("Select Categories");
        neighourList.add("Select neighbours");
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
    /* set categories*/
    private void setCategories(){
        if(Constants.postTypesMap.size()>0){
            categoriesList.clear();
            categoriesMap.clear();
            int i=0;
            int j=0;
            for(String type:Constants.postTypesMap.keySet()){
                categoriesList.add(type);
                if(catFrmPre.equals(type)){
                    j=i;
                }
                i++;
            }
            categoriesAdapter = new ArrayAdapter<String>(getActivity()
                    , android.R.layout.simple_dropdown_item_1line, categoriesList);
            categoriesSpinner.setAdapter(categoriesAdapter);
            categoriesSpinner.setSelection(j);
            categoriesTypeId = Constants.postTypesMap.get(catFrmPre).get("id");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        String s = categoriesLists.get(position)[0];
        // categoriesList.add(s);

        LinearLayout postFragment = (LinearLayout) this.getActivity().findViewById(R.id.post_fragment);
        LinearLayout postWidget = (LinearLayout) this.getActivity().findViewById(R.id.post_widget);
        RelativeLayout categoryList = (RelativeLayout) this.getActivity().findViewById(R.id.category_list);
        LinearLayout relativeLayout = (LinearLayout) this.getActivity().findViewById(R.id.post_liner_layout);

        ViewGroup.LayoutParams lp = postFragment.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        postFragment.setGravity(Gravity.TOP);
        postFragment.setLayoutParams(lp);

        categoryList.setVisibility(View.GONE);
        postWidget.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        categoriesSpinner.setSelection(position);
        editPost.setHint(Constants.postTypesMap.get(s).get("placeholder"));
    }
}
