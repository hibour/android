package com.dsquare.hibour.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.PostComments;
import com.dsquare.hibour.activities.Profile;
import com.dsquare.hibour.interfaces.WebServiceResponseCallback;
import com.dsquare.hibour.network.HibourConnector;
import com.dsquare.hibour.network.NetworkDetector;
import com.dsquare.hibour.network.PostsClient;
import com.dsquare.hibour.pojos.posts.Feeds;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.Hibour;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by ASHOK on 1/31/2016.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> implements View.OnClickListener {

  private List<Feeds> listItems = new ArrayList<>();
  private Context context;
  private NetworkDetector networkDetector;
  private Gson gson;
  private PostsClient postsClient;
  private Hibour application;
  private ProgressDialog dialog;
  private ImageLoader imageLoader;

  public FeedsAdapter(Context context, List<Feeds> listItems) {
    this.context = context;
    this.listItems = listItems;
    networkDetector = new NetworkDetector(context);
    gson = new Gson();
    postsClient = new PostsClient(context);
    application = Hibour.getInstance(context);
    //imageLoader = new ImageLoader(context)
    imageLoader = HibourConnector.getInstance(context).getImageLoader();
  }

  @Override
  public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_feeds
        , parent, false);
    final ViewHolder holder = new ViewHolder(v);
//    holder.likesLayout.setOnClickListener(this);
//    holder.likesLayout.setTag(holder);
    holder.commentsLayout.setOnClickListener(this);
    holder.commentsLayout.setTag(holder);
    holder.userImage.setOnClickListener(this);
    holder.userImage.setTag(holder);
    holder.feedImage.setOnClickListener(this);
    holder.feedImage.setTag(holder);
    holder.userImageDefault.setOnClickListener(this);
    holder.userImageDefault.setTag(holder);
    return holder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.message.setText(listItems.get(position).getPostDescription());
    String categoryString = "";
    if (Constants.categoriesMap.containsKey(listItems.get(position).getPostyType()))
      categoryString = Constants.categoriesMap.get(listItems.get(position).getPostyType());
    holder.userText.setText(listItems.get(position).getPostedUserName());
//        holder.categoryName.setText(categoryString);
//    holder.likes.setText(listItems.get(position).getLikesCount());
    holder.comments.setText(listItems.get(position).getCommentsCount());
    if (listItems.get(position).getPostImage().length() > 10) {
      Log.d("image", listItems.get(position).getPostImage());
      try {
      //  imageLoader.get(listItems.get(position).getPostImage().replace("\\", ""), ImageLoader.getImageListener(holder.feedImage
         //   , R.drawable.avatar1, R.drawable.avatar1));
        holder.imgUrl = listItems.get(position).getPostImage().replace("\\", "");
         new DownloadWebpageTask().execute(holder);
      } catch (Exception e) {
        e.printStackTrace();
        holder.feedImage.setVisibility(View.GONE);
      }
    } else {
      holder.feedImage.setVisibility(View.GONE);
    }
    if (listItems.get(position).isUserLiked().equals("true")) {
//      Bitmap likesIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_thumb_up_filled);
//      holder.likesImage.setImageBitmap(likesIcon);
    } else {
//      Bitmap likesIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_thumb_up);
//      holder.likesImage.setImageBitmap(likesIcon);
    }
    holder.timeStamp.setText(getTimeStamp(listItems.get(position).getPostDate()
        , listItems.get(position).getPostTime()));
    if (listItems.get(position).getPostedUserImage() != null) {
      Log.d("*****-----*****", listItems.get(position).getPostedUserImage());
    }
    holder.userImageDefault.setVisibility(View.VISIBLE);
    holder.userImageDefault.setText(getInitials(listItems.get(position).getPostedUserName()));
  }

  private String getInitials(String uname) {
    String[] list = uname.split("\\s+");
    String initials = "";
    for (int i = 0; i < list.length; i++) {
      initials += list[i].charAt(0);
    }
    return initials.toUpperCase();
  }

  @Override
  public int getItemCount() {
    return listItems.size();
  }

  @Override
  public void onClick(View view) {
    final ViewHolder viewHolder = (ViewHolder) view.getTag();
    final int position = viewHolder.getAdapterPosition();
    switch (view.getId()) {
      case R.id.feeds_comments_layout:
        openCommentsDialog(listItems.get(position).getPostId(), listItems.get(position).getLikesCount()
            , listItems.get(position).isUserLiked(), listItems.get(position).getPostDescription()
            , listItems.get(position).getPostImage());
        break;
      case R.id.feeds_image:
          openCommentsDialog(listItems.get(position).getPostId(), listItems.get(position).getLikesCount()
                  , listItems.get(position).isUserLiked(), listItems.get(position).getPostDescription()
                  , listItems.get(position).getPostImage());
          break;
//      case R.id.feeds_likes_layout:
//        likePost(listItems.get(position).getPostId());
//        changeLikesCount(position);
//        break;
      case R.id.feeds_user_image:
        openUserProfile(listItems.get(position).getPostedUserId());
        break;
    }
  }

  /*open user profile*/
  private void openUserProfile(String userId) {
    Intent profileIntent = new Intent(context, Profile.class);
    profileIntent.putExtra("userId", userId);
    context.startActivity(profileIntent);
  }

  /* change likes count*/
  private void changeLikesCount(int position) {
    Feeds feed = new Feeds();
    feed.setPostId(listItems.get(position).getPostId());
    feed.setCommentsCount(listItems.get(position).getCommentsCount());
    feed.setPostDate(listItems.get(position).getPostDate());
    feed.setPostTime(listItems.get(position).getPostTime());
    feed.setPostDescription(listItems.get(position).getPostDescription());
    feed.setPostImage(listItems.get(position).getPostImage());
    feed.setPostedUserId(listItems.get(position).getPostedUserId());
    feed.setPostedUserImage(listItems.get(position).getPostedUserImage());
    feed.setPostedUserName(listItems.get(position).getPostedUserName());
    if (listItems.get(position).isUserLiked().equals("true")) {
      feed.setUserLiked("false");
      int likesCount = Integer.valueOf(listItems.get(position).getLikesCount()) - 1;
      feed.setLikesCount(likesCount + "");
    } else {
      feed.setUserLiked("true");
      int likesCount = Integer.valueOf(listItems.get(position).getLikesCount()) + 1;
      feed.setLikesCount(likesCount + "");
    }
    listItems.set(position, feed);
    notifyItemChanged(position);
  }

  /* get timestamp from feed*/
  private String getTimeStamp(String date, String time) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String postedDate = date;//getDateFromString(date);
    String todayDate = getTodayDate();
    try {
      if ((formatter.parse(postedDate).compareTo(formatter.parse(todayDate))) < 0) {
        long secs = formatter.parse(todayDate).getTime()
            - formatter.parse(postedDate).getTime();
        int diffInDays = (int) ((secs) / (1000 * 60 * 60 * 24));
        return diffInDays + "d";
      } else {
        String postTime = time;
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        Date date2;
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
            , Integer.valueOf(postTime.substring(0, 2)), Integer.valueOf(postTime.substring(3, 5)));
        date2 = cal.getTime();
        long result = date1.getTime() - date2.getTime();
        int secs = (int) result / (1000);


        if (secs > 0) {
          int mins = (int) secs / (60);
          int hours = (int) mins / (60);
          if (hours > 0) {
            return hours + "h";
          } else if (mins > 0) {
            return mins + "m";
          } else {
            return secs + "s";
          }
        } else {
          secs = secs * (-1);
          int mins = (int) secs / (60);
          int hours = (int) mins / (60);
          if (hours > 0) {
            return hours + "h";
          } else if (mins > 0) {
            return mins + "m";
          } else {
            return secs + "s";
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /* get today date*/
  private String getTodayDate() {
    try {
      Calendar currentDate = Calendar.getInstance(); //Get the current date
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
      return formatter.format(currentDate.getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /* get date from string*/
  public String getDateFromString(String days) {
    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    currentDate.add(Calendar.DATE, Integer.parseInt(days));
    return formatter.format(currentDate.getTime());
  }

  /* open post comments*/
  private void openCommentsDialog(String postId, String likes, String liked, String message, String imgUrl) {
    Intent commentsIntent = new Intent(context, PostComments.class);
    commentsIntent.putExtra("postId", postId);
    commentsIntent.putExtra("likes", likes);
    commentsIntent.putExtra("liked", liked);
    commentsIntent.putExtra("message", message);
    commentsIntent.putExtra("img", imgUrl);
    context.startActivity(commentsIntent);
  }

  /* like a post*/
  private void likePost(String postId) {
    if (networkDetector.isConnected()) {
      dialog = ProgressDialog.show(context, "", "Please Wait...");
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

  /* close likes dialog*/
  private void closeDialog() {
    if (dialog != null) {
      if (dialog.isShowing()) {
        dialog.dismiss();
        dialog = null;
      }
    }
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

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView timeStamp, message, categoryName, likes, comments, userText, userImageDefault;
    private ImageView userImage, shareImage, likesImage, feedImage;
    private LinearLayout commentsLayout, likesLayout;
    private String imgUrl;
    private Bitmap bitmap;
    public ViewHolder(View itemView) {
      super(itemView);
      timeStamp = (TextView) itemView.findViewById(R.id.feeds_timestamp);
      message = (TextView) itemView.findViewById(R.id.feeds_message);
//            categoryName = (TextView)itemView.findViewById(R.id.feeds_category_name);
//      likes = (TextView) itemView.findViewById(R.id.feeds_likes_text);
      comments = (TextView) itemView.findViewById(R.id.feeds_comments_text);
      userImageDefault = (TextView) itemView.findViewById(R.id.feeds_user_image_text);
      userImage = (ImageView) itemView.findViewById(R.id.feeds_user_image);
      userText = (TextView) itemView.findViewById(R.id.feeds_user_textview);
//      likesImage = (ImageView) itemView.findViewById(R.id.feeds_likes_image);
      feedImage = (ImageView) itemView.findViewById(R.id.feeds_image);
      commentsLayout = (LinearLayout) itemView.findViewById(R.id.feeds_comments_layout);
//      likesLayout = (LinearLayout) itemView.findViewById(R.id.feeds_likes_layout);
    }
  }

  private class DownloadWebpageTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

    @Override
    protected ViewHolder doInBackground(ViewHolder... params) {
      Bitmap bitmap = null;
      // params comes from the execute() call: params[0] is the url.

      ViewHolder viewHolder = params[0];
      try {
        URL imageURL = new URL(viewHolder.imgUrl);
        viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
      } catch (IOException e) {
        // TODO: handle exception
        Log.e("error", "Downloading Image Failed");
        viewHolder.bitmap = null;
      }
      return viewHolder;

    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ViewHolder result) {
      Bitmap bitmap = null;
      if (result.bitmap == null) {
        //result.feedImage.setImageResource(R.drawable.postthumb_loading);
      } else {
        result.feedImage.setImageBitmap(result.bitmap);
      }

    }
  }

}
