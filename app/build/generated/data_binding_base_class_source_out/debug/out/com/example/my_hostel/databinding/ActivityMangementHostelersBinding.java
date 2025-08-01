// Generated by view binder compiler. Do not edit!
package com.example.my_hostel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.my_hostel.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMangementHostelersBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView rvManagementHostlers;

  @NonNull
  public final SearchView searchHostlers;

  @NonNull
  public final Toolbar toolbarManagementHostelers;

  private ActivityMangementHostelersBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView rvManagementHostlers, @NonNull SearchView searchHostlers,
      @NonNull Toolbar toolbarManagementHostelers) {
    this.rootView = rootView;
    this.rvManagementHostlers = rvManagementHostlers;
    this.searchHostlers = searchHostlers;
    this.toolbarManagementHostelers = toolbarManagementHostelers;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMangementHostelersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMangementHostelersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_mangement_hostelers, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMangementHostelersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rv_management_hostlers;
      RecyclerView rvManagementHostlers = ViewBindings.findChildViewById(rootView, id);
      if (rvManagementHostlers == null) {
        break missingId;
      }

      id = R.id.search_hostlers;
      SearchView searchHostlers = ViewBindings.findChildViewById(rootView, id);
      if (searchHostlers == null) {
        break missingId;
      }

      id = R.id.toolbar_management_hostelers;
      Toolbar toolbarManagementHostelers = ViewBindings.findChildViewById(rootView, id);
      if (toolbarManagementHostelers == null) {
        break missingId;
      }

      return new ActivityMangementHostelersBinding((ConstraintLayout) rootView,
          rvManagementHostlers, searchHostlers, toolbarManagementHostelers);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
