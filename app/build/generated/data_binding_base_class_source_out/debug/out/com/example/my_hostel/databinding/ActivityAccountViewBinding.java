// Generated by view binder compiler. Do not edit!
package com.example.my_hostel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.my_hostel.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityAccountViewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final CheckBox checkBoxTypeAccActivity;

  @NonNull
  public final ConstraintLayout constraintLayout;

  @NonNull
  public final View divider2;

  @NonNull
  public final View divider4;

  @NonNull
  public final View divider5;

  @NonNull
  public final View divider6;

  @NonNull
  public final View divider7;

  @NonNull
  public final EditText editAddressAccActivity;

  @NonNull
  public final ImageButton editButtonAddress;

  @NonNull
  public final ImageButton editButtonPhone;

  @NonNull
  public final ImageButton editProfile;

  @NonNull
  public final EditText editTextEmailAccActivity;

  @NonNull
  public final EditText hostelNameAccountActivity;

  @NonNull
  public final ImageView imageView3;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView ownerAccountActivity;

  @NonNull
  public final EditText phoneAccActivity;

  @NonNull
  public final CircleImageView profilePicAccountActivity;

  @NonNull
  public final RelativeLayout relativeLayout2;

  @NonNull
  public final Button saveButton;

  @NonNull
  public final TextView textView22;

  @NonNull
  public final TextView textView23;

  @NonNull
  public final TextView textView24;

  @NonNull
  public final TextView textView25;

  @NonNull
  public final Toolbar toolbarAccountActivity;

  @NonNull
  public final ImageButton userVerified;

  private ActivityAccountViewBinding(@NonNull ConstraintLayout rootView,
      @NonNull CheckBox checkBoxTypeAccActivity, @NonNull ConstraintLayout constraintLayout,
      @NonNull View divider2, @NonNull View divider4, @NonNull View divider5,
      @NonNull View divider6, @NonNull View divider7, @NonNull EditText editAddressAccActivity,
      @NonNull ImageButton editButtonAddress, @NonNull ImageButton editButtonPhone,
      @NonNull ImageButton editProfile, @NonNull EditText editTextEmailAccActivity,
      @NonNull EditText hostelNameAccountActivity, @NonNull ImageView imageView3,
      @NonNull ConstraintLayout main, @NonNull TextView ownerAccountActivity,
      @NonNull EditText phoneAccActivity, @NonNull CircleImageView profilePicAccountActivity,
      @NonNull RelativeLayout relativeLayout2, @NonNull Button saveButton,
      @NonNull TextView textView22, @NonNull TextView textView23, @NonNull TextView textView24,
      @NonNull TextView textView25, @NonNull Toolbar toolbarAccountActivity,
      @NonNull ImageButton userVerified) {
    this.rootView = rootView;
    this.checkBoxTypeAccActivity = checkBoxTypeAccActivity;
    this.constraintLayout = constraintLayout;
    this.divider2 = divider2;
    this.divider4 = divider4;
    this.divider5 = divider5;
    this.divider6 = divider6;
    this.divider7 = divider7;
    this.editAddressAccActivity = editAddressAccActivity;
    this.editButtonAddress = editButtonAddress;
    this.editButtonPhone = editButtonPhone;
    this.editProfile = editProfile;
    this.editTextEmailAccActivity = editTextEmailAccActivity;
    this.hostelNameAccountActivity = hostelNameAccountActivity;
    this.imageView3 = imageView3;
    this.main = main;
    this.ownerAccountActivity = ownerAccountActivity;
    this.phoneAccActivity = phoneAccActivity;
    this.profilePicAccountActivity = profilePicAccountActivity;
    this.relativeLayout2 = relativeLayout2;
    this.saveButton = saveButton;
    this.textView22 = textView22;
    this.textView23 = textView23;
    this.textView24 = textView24;
    this.textView25 = textView25;
    this.toolbarAccountActivity = toolbarAccountActivity;
    this.userVerified = userVerified;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAccountViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAccountViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_account_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAccountViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.checkBox_type_acc_activity;
      CheckBox checkBoxTypeAccActivity = ViewBindings.findChildViewById(rootView, id);
      if (checkBoxTypeAccActivity == null) {
        break missingId;
      }

      id = R.id.constraintLayout;
      ConstraintLayout constraintLayout = ViewBindings.findChildViewById(rootView, id);
      if (constraintLayout == null) {
        break missingId;
      }

      id = R.id.divider2;
      View divider2 = ViewBindings.findChildViewById(rootView, id);
      if (divider2 == null) {
        break missingId;
      }

      id = R.id.divider4;
      View divider4 = ViewBindings.findChildViewById(rootView, id);
      if (divider4 == null) {
        break missingId;
      }

      id = R.id.divider5;
      View divider5 = ViewBindings.findChildViewById(rootView, id);
      if (divider5 == null) {
        break missingId;
      }

      id = R.id.divider6;
      View divider6 = ViewBindings.findChildViewById(rootView, id);
      if (divider6 == null) {
        break missingId;
      }

      id = R.id.divider7;
      View divider7 = ViewBindings.findChildViewById(rootView, id);
      if (divider7 == null) {
        break missingId;
      }

      id = R.id.edit_address_acc_Activity;
      EditText editAddressAccActivity = ViewBindings.findChildViewById(rootView, id);
      if (editAddressAccActivity == null) {
        break missingId;
      }

      id = R.id.edit_button_address;
      ImageButton editButtonAddress = ViewBindings.findChildViewById(rootView, id);
      if (editButtonAddress == null) {
        break missingId;
      }

      id = R.id.edit_button_phone;
      ImageButton editButtonPhone = ViewBindings.findChildViewById(rootView, id);
      if (editButtonPhone == null) {
        break missingId;
      }

      id = R.id.edit_profile;
      ImageButton editProfile = ViewBindings.findChildViewById(rootView, id);
      if (editProfile == null) {
        break missingId;
      }

      id = R.id.edit_text_Email_Acc_activity;
      EditText editTextEmailAccActivity = ViewBindings.findChildViewById(rootView, id);
      if (editTextEmailAccActivity == null) {
        break missingId;
      }

      id = R.id.hostel_name_account_activity;
      EditText hostelNameAccountActivity = ViewBindings.findChildViewById(rootView, id);
      if (hostelNameAccountActivity == null) {
        break missingId;
      }

      id = R.id.imageView3;
      ImageView imageView3 = ViewBindings.findChildViewById(rootView, id);
      if (imageView3 == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.owner_account_activity;
      TextView ownerAccountActivity = ViewBindings.findChildViewById(rootView, id);
      if (ownerAccountActivity == null) {
        break missingId;
      }

      id = R.id.phone_acc_activity;
      EditText phoneAccActivity = ViewBindings.findChildViewById(rootView, id);
      if (phoneAccActivity == null) {
        break missingId;
      }

      id = R.id.profile_pic_account_activity;
      CircleImageView profilePicAccountActivity = ViewBindings.findChildViewById(rootView, id);
      if (profilePicAccountActivity == null) {
        break missingId;
      }

      id = R.id.relativeLayout2;
      RelativeLayout relativeLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (relativeLayout2 == null) {
        break missingId;
      }

      id = R.id.save_button;
      Button saveButton = ViewBindings.findChildViewById(rootView, id);
      if (saveButton == null) {
        break missingId;
      }

      id = R.id.textView22;
      TextView textView22 = ViewBindings.findChildViewById(rootView, id);
      if (textView22 == null) {
        break missingId;
      }

      id = R.id.textView23;
      TextView textView23 = ViewBindings.findChildViewById(rootView, id);
      if (textView23 == null) {
        break missingId;
      }

      id = R.id.textView24;
      TextView textView24 = ViewBindings.findChildViewById(rootView, id);
      if (textView24 == null) {
        break missingId;
      }

      id = R.id.textView25;
      TextView textView25 = ViewBindings.findChildViewById(rootView, id);
      if (textView25 == null) {
        break missingId;
      }

      id = R.id.toolbar_account_activity;
      Toolbar toolbarAccountActivity = ViewBindings.findChildViewById(rootView, id);
      if (toolbarAccountActivity == null) {
        break missingId;
      }

      id = R.id.user_verified;
      ImageButton userVerified = ViewBindings.findChildViewById(rootView, id);
      if (userVerified == null) {
        break missingId;
      }

      return new ActivityAccountViewBinding((ConstraintLayout) rootView, checkBoxTypeAccActivity,
          constraintLayout, divider2, divider4, divider5, divider6, divider7,
          editAddressAccActivity, editButtonAddress, editButtonPhone, editProfile,
          editTextEmailAccActivity, hostelNameAccountActivity, imageView3, main,
          ownerAccountActivity, phoneAccActivity, profilePicAccountActivity, relativeLayout2,
          saveButton, textView22, textView23, textView24, textView25, toolbarAccountActivity,
          userVerified);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
