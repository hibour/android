package com.dsquare.hibour.activities;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.AdapterPostComments;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.PostComment;
import com.dsquare.hibour.pojos.posts.PostLikedUser;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostComments extends AppCompatActivity implements View.OnClickListener{

    private ImageView postIcon;
    private EditText commentsText;
    private RecyclerView commentsList;
    private List<String[]> commentslist = new ArrayList<>();
    private AdapterPostComments adapter;
    private ProgressDialog dialog;
    private NetworkDetector networkDetector;
    private PostsClient client;
    private String postId = "";
    private TextView likesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);
        initializeViews();
        prepareCommentsList();
        initializeEventListeners();
    }
    /* initialize views*/
    private void initializeViews(){
        postId = getIntent().getStringExtra("postId");
        likesText = (TextView)findViewById(R.id.comments_likes_text);
        postIcon = (ImageView)findViewById(R.id.comments_post_icon);
        commentsText = (EditText)findViewById(R.id.comments_edit_text);
        commentsList = (RecyclerView)findViewById(R.id.comments_post_list);
        networkDetector = new NetworkDetector(this);
        client = new PostsClient(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setLayoutManager(layoutManager);
        commentsList.setHasFixedSize(true);

    }
    /* initialize event listeners*/
    private void initializeEventListeners(){
        postIcon.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.comments_post_list:
                break;
        }
    }
    /* prepare comments list*/
    private void prepareCommentsList(){
        if(!postId.equals("")){
            Postpojos post = Constants.postpojosMap.get(postId);
            List<PostComment> comments = post.getPostComments();
            List<PostLikedUser> likedUsers = post.getPostLikedUsers();
            String likesCount = post.getPostLikesCount()+"";
            adapter = new AdapterPostComments(this,comments);
            likesText.setText(likesCount+" members liked this.");
            commentsList.setAdapter(adapter);
        }
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
}
