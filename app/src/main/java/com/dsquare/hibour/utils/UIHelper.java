package com.dsquare.hibour.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

public class UIHelper {
  private Context context;

  public UIHelper(Context context) {
    this.context = context;
  }

  public void hideKeyboard() {
    View view = ((AppCompatActivity) context).getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  public void showViewFromBottom(View view) {
    YoYo.with(Techniques.SlideInUp).duration(Constants.DURATION_VERY_LONG).playOn(view);
  }

  public void hideViewToBottom(View view) {
    YoYo.with(Techniques.SlideOutDown).duration(Constants.DURATION_VERY_LONG).playOn(view);
  }

  public void shakeView(View view) {
    YoYo.with(Techniques.Shake).duration(Constants.DURATION_MEDIUM).playOn(view);
  }

  public void zoomInView(final View view) {
    YoYo.with(Techniques.ZoomIn).duration(Constants.DURATION_MEDIUM)
        .withListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
            view.setVisibility(View.VISIBLE);
          }

          @Override
          public void onAnimationEnd(Animator animation) {

          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {

          }
        }).playOn(view);
  }

  public void zoomOutView(final View view) {
    YoYo.with(Techniques.ZoomOut).duration(Constants.DURATION_MEDIUM)
        .withListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {

          }

          @Override
          public void onAnimationEnd(Animator animation) {
            view.setVisibility(View.GONE);
          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {

          }
        }).playOn(view);

  }
}
