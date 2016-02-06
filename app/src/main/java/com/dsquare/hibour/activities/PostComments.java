package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.AdapterPostComments;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.comments.Comments;
import com.dsquare.hibour.pojos.comments.Datum;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostComments extends AppCompatActivity implements View.OnClickListener{

    private ImageView postIcon,likeIcon,backIcon;
    private EditText commentsText;
    private RecyclerView commentsList;
    private List<String[]> commentslist = new ArrayList<>();
    private AdapterPostComments adapter;
    private ProgressDialog dialog;
    private PostsClient client;
    private String postId = "",likes = "";
    private TextView likesText;
    private Button sumbit;
    private NetworkDetector networkDetector;
    private Gson gson;
    private Hibour application;
    private PostsClient postsClient;
    private ProgressDialog postsDialog;
    private List<String[]> postsList = new ArrayList<>();
    private String liked="";
    private RelativeLayout likesLayout;
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
        likeIcon = (ImageView)findViewById(R.id.comments_like_icon);
        backIcon = (ImageView)findViewById(R.id.comments_bacl_icon);
        likesLayout = (RelativeLayout)findViewById(R.id.comments_likes_layout);
        postsClient = new PostsClient(this);
        postId = getIntent().getStringExtra("postId");
        likes = getIntent().getStringExtra("likes");
        liked = getIntent().getStringExtra("liked");
        if(liked.equals("true")){
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up);
            likeIcon.setImageBitmap(likesIcon);
        }else{
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up);
            likeIcon.setImageBitmap(likesIcon);
        }
        likesText = (TextView)findViewById(R.id.comments_likes_text);
        setLikesText(Integer.valueOf(likes));
        //likesText.setText(likes+" members liked this");
        postIcon = (ImageView)findViewById(R.id.comments_post_icon);
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
        postIcon.setOnClickListener(this);
        sumbit.setOnClickListener(this);
        likeIcon.setOnClickListener(this);
        likesLayout.setOnClickListener(this);
        backIcon.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.comments_post_icon:
                openLikesScreen();
                break;
            case R.id.comments_sumbit:
                postComment(application.getUserId(),postId,commentsText.getText().toString());
                commentsText.setText("");
                break;
            case R.id.comments_like_icon:
                likePost(postId);
                changeLikes();
                break;
            case R.id.comments_likes_layout:
                openLikesScreen();
                break;
            case R.id.comments_bacl_icon:
                this.finish();
                break;
        }
    }

    /* change likes count and icon*/
    private void changeLikes(){
        if(liked.equals("true")){
            liked = "fale";
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up);
            likeIcon.setImageBitmap(likesIcon);
            likes = Integer.valueOf(likes)-1+"";
            setLikesText(Integer.valueOf(likes)-1);
        }else{
            liked = "true";
            Bitmap likesIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_thumb_up_filled);
            likeIcon.setImageBitmap(likesIcon);
            setLikesText(Integer.valueOf(likes)+1);
            likes = Integer.valueOf(likes)+1+"";
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
            Comments comments = gson.fromJson(jsonObject.toString(),Comments.class);
            List<Datum> data = comments.getData();
            for(int i=0;i<data.size();i++){
                String[] comment = new String[3];
                comment[0] = String.valueOf(data.get(i).getUser().getName());
                comment[1] = data.get(i).getCommentTime();
                comment[2] = data.get(i).getCommentMessage();
                postsList.add(comment);
            }
            setAdapters(postsList);
            }

         catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        }
    private void setAdapters(List<String[]> postsList){
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
            Toast.makeText(this,"Network Connection error",Toast.LENGTH_LONG).show();
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
            Toast.makeText(this,"Network Connection error",Toast.LENGTH_LONG).show();
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
    private void setLikesText(int likesCount){
        Log.d("likes count",likesCount+"");
        if(likesCount>1){
            likesText.setText(likesCount+" members liked this");
        }else if(likesCount==1){
            likesText.setText("1 member liked this");
        }else{
            likesText.setText("Be the first one to like this post.");
        }
    }
    /* like a post*/
    private void likePost(String postId){
        if(networkDetector.isConnected()){
            dialog = ProgressDialog.show(this,"","Please Wait...");
            postsClient.likePost(application.getUserId(),postId,new WebServiceResponseCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    parseLike(jsonObject);
                    closeDialog();
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.d("error in liking",error.toString());
                    closeDialog();
                }
            });
        }else{

        }
    }
    /* parse likes */
    private void parseLike(JSONObject jsonObject){
        Log.d("data",jsonObject.toString());
    }
}
