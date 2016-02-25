package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.AdapterPostComments;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.comments.Comments;
import com.dsquare.hibour.pojos.comments.Datum;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostComments extends AppCompatActivity implements View.OnClickListener{

    private ImageView postIcon, likeIcon, backIcon;
    private EditText commentsText;
    private RecyclerView commentsList;
    private List<String[]> commentslist = new ArrayList<>();
    private AdapterPostComments adapter;
    private ProgressDialog dialog;
    private PostsClient client;
    private String postId = "",likes = "";
    private TextView likesText, postMessage;
    private Button sumbit;
    private NetworkDetector networkDetector;
    private Gson gson;
    private Hibour application;
    private PostsClient postsClient;
    private ProgressDialog postsDialog;
    private List<String[]> postsList = new ArrayList<>();
    private String liked = "", message = "", image = "";
    private RelativeLayout likesLayout, noCommentsLayout;
    private ImageView postImage;
    private ImageLoader imageLoader;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);
        initializeViews();
        initializeEventListeners();
        getAllComments();
    }
    /* initialize views*/
    private void initializeViews(){
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        imageLoader = HibourConnector.getInstance(this).getImageLoader();
        backIcon = (ImageView) findViewById(R.id.comments_bacl_icon);
        postImage = (ImageView) findViewById(R.id.comments_image);
        postMessage = (TextView) findViewById(R.id.comments_message);
        noCommentsLayout = (RelativeLayout) findViewById(R.id.no_comments_layout);
        postsClient = new PostsClient(this);
        postId = getIntent().getStringExtra("postId");
        likes = getIntent().getStringExtra("likes");
        liked = getIntent().getStringExtra("liked");
        message = getIntent().getStringExtra("message");
        image = getIntent().getStringExtra("img");
    /*    if(liked.equals("true")){
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up);
            likeIcon.setImageBitmap(likesIcon);
        }else{
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up);
            likeIcon.setImageBitmap(likesIcon);
        }*/
        postMessage.setText(message);
        if (image.length() > 10) {
            postImage.setVisibility(View.VISIBLE);
           // imageLoader.get(image.replace("\\", ""), ImageLoader.getImageListener(postImage
             //   , R.drawable.avatar1, R.drawable.avatar1));
            new DownloadWebpageTask().execute(image);
        } else {
            postImage.setVisibility(View.GONE);
        }
        //likesText = (TextView)findViewById(R.id.comments_likes_text);
        //setLikesText(Integer.valueOf(likes));
        //postIcon = (ImageView)findViewById(R.id.comments_post_icon);
        commentsText = (EditText)findViewById(R.id.comments_edit_text);
        commentsList = (RecyclerView)findViewById(R.id.comments_post_list);
        sumbit = (Button)findViewById(R.id.comments_sumbit);
        networkDetector = new NetworkDetector(this);
        client = new PostsClient(this);
        application =  Hibour.getInstance(this);
        gson=new Gson();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setLayoutManager(layoutManager);
        commentsList.setHasFixedSize(true);
    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        postImage.setOnClickListener(this);
        sumbit.setOnClickListener(this);
        //likeIcon.setOnClickListener(this);
        //likesLayout.setOnClickListener(this);
        backIcon.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.comments_image:
                openFullImage();
                break;
            case R.id.comments_sumbit:
                postComment(application.getUserId(),postId,commentsText.getText().toString());
                commentsText.setText("");
                break;
            case R.id.comments_bacl_icon:
                this.finish();
                break;
        }
    }

    private void openFullImage() {
        Intent imageIntent = new Intent(this, FeedImageFullView.class);
        imageIntent.putExtra("image", image);
        startActivity(imageIntent);
    }

    /* change likes count and icon*/
    private void changeLikes() {
        if (liked.equals("true")) {
            liked = "fale";
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up);
            likeIcon.setImageBitmap(likesIcon);
            likes = Integer.valueOf(likes) - 1 + "";
            setLikesText(Integer.valueOf(likes) - 1);
        } else {
            liked = "true";
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up_filled);
            likeIcon.setImageBitmap(likesIcon);
            setLikesText(Integer.valueOf(likes) + 1);
            likes = Integer.valueOf(likes) + 1 + "";
        }
    }
    private void openLikesScreen() {
        Intent intent = new Intent(getApplicationContext(),PostLikes.class);
        intent.putExtra("postId",postId);
        startActivity(intent);
        //this.finish();
        }

    /* prepare comments list*/
    private void prepareCommentsList(JSONObject jsonObject){
        Log.d("json",jsonObject.toString());
        try {
            Comments comments = gson.fromJson(jsonObject.toString(), Comments.class);
            List<Datum> data = comments.getData();
            if (data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    String[] comment = new String[4];
                    comment[0] = String.valueOf(data.get(i).getUser().getName());
                    comment[1] = data.get(i).getCommentTime();
                    comment[2] = data.get(i).getCommentMessage();
                    comment[3] = data.get(i).getCommentDate();
                    postsList.add(comment);
                }
                setAdapters(postsList);
            } else {
                if (commentsList.getVisibility() == View.VISIBLE) {
                    commentsList.setVisibility(View.GONE);
                }
                if (noCommentsLayout.getVisibility() == View.GONE) {
                    noCommentsLayout.setVisibility(View.VISIBLE);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        }
    private void setAdapters(List<String[]> postsList){
        if (commentsList.getVisibility() == View.GONE) {
            commentsList.setVisibility(View.VISIBLE);
        }
        if (noCommentsLayout.getVisibility() == View.VISIBLE) {
            noCommentsLayout.setVisibility(View.GONE);
        }
        commentsList.setAdapter(new AdapterPostComments(this, postsList));
    }
    /* post comment*/
    private void postComment(String userId,String postId,String postComment){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            client.commentOnPost(userId,postId,postComment,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    closeDialog();
                    postsList.clear();
                    getAllComments();
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                }
            });
        }else{
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();

        }
    }

    /* post comment*/
    private void getAllComments(){
        Log.d("ids",postId);
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this,"",getResources()
                    .getString(R.string.progress_dialog_text));
            client.getcommentOnPost(postId, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    closeDialog();
                    prepareCommentsList(jsonObject);
                }

                @Override
                public void onFailure(VolleyError error) {
                    closeDialog();
                }
            });
        }else{
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }
    /*close dialog*/
    private void closeDialog(){
        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
                dialog=null;
            }
        }
    }

    /* set likes text*/
    private void setLikesText(int likesCount) {
        Log.d("likes count", likesCount + "");
        if (likesCount > 1) {
            likesText.setText(likesCount + " members liked this");
        } else if (likesCount == 1) {
            likesText.setText("1 member liked this");
        } else {
            likesText.setText("Be the first one to like this post.");
        }
    }

    /* like a post*/
    private void likePost(String postId) {
        if (networkDetector.isConnected()) {
            dialog = ProgressDialog.show(this, "", "Please Wait...");
            postsClient.likePost(application.getUserId(), postId, new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseLike(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("error in liking", error.toString());
                    closeDialog();
                }
            });
        } else {

        }
    }

    /* parse likes */
    private void parseLike(JSONObject jsonObject) {
        Log.d("data", jsonObject.toString());
    }
    private Bitmap downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("img tag", "The response is: " + response);
            is = conn.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);
            //  return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                final Bitmap bitmap = downloadUrl(urls[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postImage.setImageBitmap(bitmap);

                    }
                });

                return "";
                //return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // textView.setText(result);
        }
    }


}
