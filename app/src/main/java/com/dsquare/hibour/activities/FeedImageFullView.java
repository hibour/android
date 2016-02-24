package com.dsquare.hibour.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dsquare.hibour.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FeedImageFullView extends AppCompatActivity {

  private ImageView imageView, backIcon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed_image_full_view);
    initializeObjects();
  }

  private void initializeObjects() {
    try {
      imageView = (ImageView) findViewById(R.id.feed_image_full_view);
      backIcon = (ImageView) findViewById(R.id.feed_image_back_icon);
      backIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          FeedImageFullView.this.finish();
        }
      });
      Intent intent = getIntent();
      ConnectivityManager connMgr = (ConnectivityManager)
          getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.isConnected()) {
        new DownloadWebpageTask().execute(intent.getStringExtra("image"));
      } else {
        // textView.setText("No network connection available.");
      }

    } catch (Exception e) {
      e.printStackTrace();
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

  private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {

      // params comes from the execute() call: params[0] is the url.
      try {
        final Bitmap bitmap = downloadUrl(urls[0]);
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            imageView.setImageBitmap(bitmap);

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
