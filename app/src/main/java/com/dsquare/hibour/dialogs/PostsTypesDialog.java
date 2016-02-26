package com.dsquare.hibour.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.CategoriesAdapter;
import com.dsquare.hibour.interfaces.CategoriesCallback;
import com.dsquare.hibour.utils.Constants;
import com.dsquare.hibour.utils.GridLayoutSpacing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dsquare Android on 2/3/2016.
 */
public class PostsTypesDialog extends DialogFragment implements View.OnClickListener, CategoriesCallback {

  CategoryChooserListener listener;
  private Context context;
  private RecyclerView categoriesRecycler;
  private List<String> categoriesList = new ArrayList<>();
  private ImageView cancelPost;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      listener = (CategoryChooserListener) getTargetFragment();
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement NoticeDialogListener");
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogSlideAnim);
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_cateogries_chooser, null);
    AlertDialog d = builder.setView(view).create();

    WindowManager.LayoutParams wmlp = d.getWindow().getAttributes();
    wmlp.gravity = Gravity.BOTTOM;

    initializeViews(view);
    initializeEventListeners();
    return d;
  }

  /* initialize Views*/
  private void initializeViews(View view) {
    cancelPost = (ImageView) view.findViewById(R.id.new_post_cancel_button);
    categoriesRecycler = (RecyclerView) view.findViewById(R.id.post_categories_list);
    categoriesRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    categoriesRecycler.addItemDecoration(new GridLayoutSpacing(2, 5, true));
    categoriesRecycler.setHasFixedSize(true);
    setCategories();
  }

  /* initialize event listeners*/
  private void initializeEventListeners() {
    cancelPost.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.new_post_cancel_button:
        listener.onCancel(this);
        break;
    }
  }

  /* set categories*/
  private void setCategories() {
    Log.d("categories", Constants.postTypesMap.size() + "");
    if (Constants.postTypesMap.size() > 0) {
      categoriesList.clear();
      for (String type : Constants.postTypesMap.keySet()) {
        categoriesList.add(type);
      }
      CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), categoriesList);
      adapter.setCallback(this);
      categoriesRecycler.setAdapter(adapter);
    }
  }

  @Override
  public void onCategoryChoosed(String categoryName) {
    listener.onCategorySelected(categoryName, this);
  }

  public interface CategoryChooserListener {
    void onCancel(DialogFragment dialog);

    void onCategorySelected(String categoryName, DialogFragment dialog);
  }
}
