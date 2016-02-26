package com.dsquare.hibour.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
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

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.dialogs.PostsImagePicker;
import com.dsquare.hibour.interfaces.ImagePicker;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Fonts;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dsquare Android on 2/1/2016.
 */

public class NewPost extends android.support.v4.app.Fragment implements View.OnClickListener
    , ImagePicker, AdapterView.OnItemClickListener {
    private static final int REQUEST_IMAGE_SELECTOR = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    public static final String CATEGORY_BUNDLE_ARG = "category";

    private String category;
    private Button send;
    private EditText text;
    private Typeface proxima;
    private DialogFragment chooserDialog;
    private TextView done, cancel;
    private ArrayAdapter<String> categoriesAdapter;
    private ArrayAdapter<String> neighoursAdapter;
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
    private String categoriesTypeId = "", neighoursTypeId = "";
    private Bitmap bitmap;
    private Hibour application;
    private ImageView postImage, gallary, delete;
    private EditText editPost;
    private Spinner categoriesSpinner, neighboursSpinner;
    private RelativeLayout layout, layout1;
    private LinearLayout postWidget;
    private View views;
    private ListView categoriesRecycler;
    private String[] details;
    private String catFrmPre = "";
    private boolean isMessage = false;
    private PostsListener mListener;
    private CoordinatorLayout coordinatorLayout;
    public NewPost() {
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
        catFrmPre = getArguments().getString(CATEGORY_BUNDLE_ARG, "");
        initializeViews(view);
        initializeEventListeners();
        //  getNeighbourHoods(application.getUserId());
        setCategories();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        catFrmPre = getArguments().getString(CATEGORY_BUNDLE_ARG, "");
    }

    private void initializeEventListeners() {
        gallary.setOnClickListener(this);
        delete.setOnClickListener(this);
       // done.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_post_done:
                validatepostData();
                break;
            case R.id.creat_imageview_post_icon:
                gallary.setVisibility(View.VISIBLE);
                openImageChooser();
                break;
            case R.id.create_delete_image:
                opendeleteImage();
                break;
            case R.id.creat_post_cancel:
                mListener.onCancelClicked();
                break;
        }
    }

    private void opendeleteImage() {
        gallary.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
    }

    private void openImageChooser() {
        chooserDialog = new PostsImagePicker();
        chooserDialog.show(getActivity().getSupportFragmentManager(), "chooser dialog");
        chooserDialog.setTargetFragment(this, 0);
    }

    /* open gallary intent*/
    private void openGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
    }

    /* open camera*/
    private void openCamera() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK
            && data != null && data.getData() != null) {
//            imageUploaded.setVisibility(View.VISIBLE);
            Log.d("camera", "yes");
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                if (layout.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);
                    postImage.setAdjustViewBounds(true);
                    postImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    postImage.setImageBitmap(scaled);
                    gallary.setVisibility(View.GONE);
                    getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
                postimagesstring = getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK
                &&  data != null && data.getData() != null){
            Log.d("gallery", "yes");
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                if (layout.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);
                    postImage.setAdjustViewBounds(true);
                    postImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    postImage.setImageBitmap(scaled);
                    gallary.setVisibility(View.GONE);
                    getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
                postimagesstring = getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
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
        if (!categoriesTypeId.equals("") && text.getText().toString().equals("null")
                && !text.getText().toString().equals("null") &&
                !text.getText().toString().equals("") &&
                !postimagesstring.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "All fields are required!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        } else {
            Log.d("userid", application.getUserId());
            sendPostData("1", text.getText().toString()
                    , postimagesstring);
        }
    }

    //  send data to server
    private void sendPostData(String posttypeid, String postMessage, String postImage) {
        if (networkDetector.isConnected()) {
            String cat_str = categoriesTypeId;
            newpostDialogue = ProgressDialog.show(getActivity(), "", getResources()
                .getString(R.string.progress_dialog_text));
            postsClient.insertonPost(application.getUserId(), cat_str, posttypeid, postMessage, postImage
                , "1", "", new WebServiceResponseCallback() {
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
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }

    /* parse insert post  data*/
    private void parsePostDetails(JSONObject jsonObject) {
        Log.d("json", jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Post update successfully!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
            mListener.onDoneClicked();
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Post updation failed!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
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
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);
        getActivity().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        done = (TextView) view.findViewById(R.id.create_post_done);
        done.setTextColor(getActivity().getResources().getColor(R.color.gray));
        //  bgColors = getActivity().getResources().getStringArray(R.array.movie_serial_bg);
        categoriesSpinner = (Spinner) view.findViewById(R.id.newpost_categories_spinner);
        // neighboursSpinner = (Spinner)view.findViewById(R.id.newpost_neighbourhood_spinner);
        cancel = (TextView) view.findViewById(R.id.creat_post_cancel);
        gallary = (ImageView) view.findViewById(R.id.creat_imageview_post_icon);
        postImage = (ImageView) view.findViewById(R.id.creat_imageview_display_icon);
        delete = (ImageView) view.findViewById(R.id.create_delete_image);
        layout = (RelativeLayout) view.findViewById(R.id.create_relative);
        layout1 = (RelativeLayout) view.findViewById(R.id.home_app_bar1);
        views = (View) view.findViewById(R.id.views);
        application = Hibour.getInstance(getActivity());
        text = (EditText) view.findViewById(R.id.newposts_edittest);
        editPost = (EditText) view.findViewById(R.id.newposts_edittest);
        postWidget = (LinearLayout) view.findViewById(R.id.post_widget);
        editPost.requestFocus();

        editPost.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                Log.d("edit text","On Foucs. Has Focus = " + hasFocus);
                if (hasFocus){
                        done.setTextColor(getActivity().getResources().getColor(R.color.black_1));
                        setOnClickForDone();
                    //open keyboard
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v,
                            InputMethodManager.SHOW_FORCED);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editPost, InputMethodManager.SHOW_IMPLICIT);
                }
                else{
                    //close keyboard
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v,
                            InputMethodManager.SHOW_FORCED);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editPost, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        //Set on click listener to clear focus
        editPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View clickedView)
            {
                clickedView.clearFocus();
                clickedView.requestFocus();
            }
        });


        //  InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//        editPost.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View v, boolean hasFocus){
//                Log.d("edit text","On Foucs. Has Focus = " + hasFocus);
//                if (hasFocus){
//                    done.setTextColor(getActivity().getResources().getColor(R.color.black_1));
//                    setOnClickForDone();
//                  InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                    if (imm != null) {
//                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//                    }
//
//                }
//                else{
//
//                    InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(editPost.getWindowToken(), 0);
//                    Log.d("gg","12");
//                }
//            }
//        });
        //Set on click listener to clear focus
//        postWidget.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View clickedView) {
//                editPost.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getActivity().
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//                    done.setTextColor(getActivity().getResources().getColor(R.color.black_1));
//                   clickedView.clearFocus();
//                }
//              //  clickedView.clearFocus();
//
//            }
//        });

        networkDetector = new NetworkDetector(getActivity());
        postsClient = new PostsClient(getActivity());
        gson = new Gson();
        proxima = Typeface.createFromAsset(getActivity().getAssets(), Fonts.getTypeFaceName());
        categoriesAdapter = new ArrayAdapter<String>(getActivity()
            , android.R.layout.simple_spinner_dropdown_item, categoriesList);
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String categoriesType = categoriesList.get(position);
                    if (!categoriesType.equals("Select Categories")) {
                        categoriesTypeId = Constants.postTypesMap.get(categoriesType).get("id");
                        editPost.setHint(Constants.postTypesMap.get(categoriesType).get("placeholder"));
                    }
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources()
                            .getColor(R.color.gray));
                    ((TextView) parent.getChildAt(0)).setTypeface(proxima);
                    ((TextView) parent.getChildAt(0)).setPadding(0, 0, 0, 0);
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        neighoursAdapter = new ArrayAdapter<String>(getActivity()
            , android.R.layout.simple_spinner_dropdown_item, neighourList);
        /*neighboursSpinner.setAdapter(neighoursAdapter);
        neighboursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null && parent.getChildAt(0) != null) {
                    String categoriesType = neighourList.get(position);
                    Log.d("categoriestype", categoriesType);
                    if (!categoriesType.equals("Select neighbours")) {
                        //neighoursTypeId = neighourMap.get(categoriesType);
                        Log.d("categoriestype", categoriesType);
                        filled[3] = true;
                        if(filled[0] == true && filled[1] == true ){
                            done.setTextColor(getActivity().getResources().getColor(R.color.black_1));
                            setOnClickForDone();
                        }
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
        });*/

    }

    private void setOnClickForDone() {
        done.setOnClickListener(this);
    }

    /*get all neighbourhoods */
    private void getNeighbourHoods(String userId) {
        if (networkDetector.isConnected()) {
//            newpostDialogue = ProgressDialog.show(getActivity(),"","Please Wait...");
            postsClient.getAllNeighbourhoods(userId, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseNeighbourHoods(jsonObject);
                    closePostDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closePostDialog();
                    Log.d("error", error.toString());
                }
            });
        } else {

        }
    }

    /* parse neighbourhoods*/
    private void parseNeighbourHoods(JSONObject jsonObject) {
        Log.d("data", jsonObject.toString());
        try {
            JSONArray data = jsonObject.getJSONArray("data");
            if (data.length() > 0) {
                neighourList.clear();
                neighourList.add("Select neighbours");
                neighourMap.clear();
                for (int i = 0; i < data.length(); i++) {
                    //neighourMap.put(d.getAddress(), d.getId() + "");
                    String hood = data.getString(i);
                    if (!hood.equals("") && !hood.equals(null)
                        && !hood.equals("null")) {
                        if (!neighourList.contains(hood)) {
                            neighourList.add(hood);
                        }
                    }
                }
                neighoursAdapter = new ArrayAdapter<String>(getActivity()
                        , android.R.layout.simple_dropdown_item_1line, neighourList);
//                neighboursSpinner.setAdapter(neighoursAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareCategoriesList() {
//        categoriesList.add("Select Categories");
        neighourList.add("Select neighbours");
    }

    @Override
    public void pickerSelection(int choice) {
        switch (choice) {
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
    private void setCategories() {
        if (Constants.postTypesMap.size() > 0) {
            categoriesList.clear();
            categoriesMap.clear();

            int i = 0;
            int j = 0;
            for (String type : Constants.postTypesMap.keySet()) {
                categoriesList.add(type);
                if (catFrmPre.equals(type)) {
                    j = i;
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
        //  RelativeLayout categoryList = (RelativeLayout) this.getActivity().findViewById(R.id.category_list);
        LinearLayout relativeLayout = (LinearLayout) this.getActivity().findViewById(R.id.post_liner_layout);

        ViewGroup.LayoutParams lp = postFragment.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        postFragment.setGravity(Gravity.TOP);
        postFragment.setLayoutParams(lp);

        //      categoryList.setVisibility(View.GONE);
        postWidget.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        categoriesSpinner.setSelection(position);
        editPost.setHint(Constants.postTypesMap.get(s).get("placeholder"));
    }

    public interface PostsListener {
        void onCancelClicked();

        void onDoneClicked();
    }

}
