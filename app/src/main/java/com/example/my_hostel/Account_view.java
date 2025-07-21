package com.example.my_hostel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account_view extends AppCompatActivity
{
    ///Components///////////////////
    private CircleImageView profile_pic;
    ///TextViews
    private TextView owner_name;
    ///EditTexts
    private EditText hostel_name,email,phone,address;
    ///Buttons
    private ImageButton button_edit_hostelName,user_verified,phone_edit_button,address_edit_button;
    private Button save;
    private CheckBox type_hostel;

    FirebaseAuth authentication=FirebaseAuth.getInstance();
    FirebaseUser user= authentication.getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    /// Data Variables
    String Hostel_Name;
    String Owner_Name;
    Uri profile;
    String phone_number;
    String address_text;
    String type;

    ///Boolean value for the Edit Buttons
    boolean boolean_edit_hostel_name =false;
    boolean boolean_edit_phone =false;
    boolean boolean_edit_address=false;

    ///Count variables for the Edit Buttons
    int count_edit_hostel_name=0;
    int count_edit_phone=0;
    int count_edit_address=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolbar_account_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /// ID's for the Components in the activity
        profile_pic=findViewById(R.id.profile_pic_account_activity);
        owner_name=findViewById(R.id.owner_account_activity);
        hostel_name=findViewById(R.id.hostel_name_account_activity);
        email=findViewById(R.id.edit_text_Email_Acc_activity);
        phone=findViewById(R.id.phone_acc_activity);
        address=findViewById(R.id.edit_address_acc_Activity);
        button_edit_hostelName=findViewById(R.id.edit_profile);
        phone_edit_button=findViewById(R.id.edit_button_phone);
        user_verified=findViewById(R.id.user_verified);
        user_verified.setVisibility(View.GONE);
        address_edit_button=findViewById(R.id.edit_button_address);
        save=findViewById(R.id.save_button);
        type_hostel=findViewById(R.id.checkBox_type_acc_activity);
        /// Disable Components Before The Edit Buttons are Clicked
        disable_components();
        ///Getting Data from the Database
        get_Data_From_Database();
        /// Calling Edit Buttons Handling Methods
        hostel_name_edit_handle();
        phone_edit_handle();
        address_edit_handle();
        ///Handling The Save Button Click
        handle_save();

        user.reload();
        if(user.isEmailVerified())
        {
            user_verified.setVisibility(View.VISIBLE);
        }
        else
        {
            user_verified.setVisibility(View.GONE);
        }

    }

    ///Handling Edit Buttons ////////////////////

    ///Hostel Name Handling////////////////
    private void hostel_name_edit_handle()
    {
        //Hostel Name Handling
        button_edit_hostelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(count_edit_hostel_name%2==0)
                {
                    button_edit_hostelName.setImageDrawable(getResources().getDrawable(R.drawable.baseline_check_circle_24));
                    hostel_name.setEnabled(true);
                    count_edit_hostel_name++;
                }
                else
                {
                    hostel_name.setEnabled(false);
                    if (hostel_name.getText().toString().isEmpty())
                    {
                        boolean_edit_hostel_name =false;
                        Toast.makeText(Account_view.this, "No Changes Were Made", Toast.LENGTH_SHORT).show();
                        hostel_name.setText(Hostel_Name);
                        save_visibility_handle();
                    }
                    if (hostel_name.getText().toString().equals(Hostel_Name)) {
                        boolean_edit_hostel_name = false;
                        save_visibility_handle();

                    } else
                    {
                        boolean_edit_hostel_name = true;
                        Toast.makeText(Account_view.this, "Hostel Name Will be Updated", Toast.LENGTH_SHORT).show();
                        save.setVisibility(View.VISIBLE);
                    }
                    count_edit_hostel_name++;
                    button_edit_hostelName.setImageDrawable(getResources().getDrawable(R.drawable.baseline_edit_24));
                }
            }
        });

    }
    ///Phone Handling////////////////
    private void phone_edit_handle()
    {
       phone_edit_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               if(count_edit_phone%2==0)
               {
                   phone_edit_button.setImageDrawable(getResources().getDrawable(R.drawable.baseline_check_circle_24));
                   phone.setEnabled(true);
                   count_edit_phone++;
               }
               else {
                   phone.setEnabled(false);
                   if (phone.getText().toString().isEmpty()) {
                       boolean_edit_phone = false;
                       Toast.makeText(Account_view.this, "No Changes Were Made", Toast.LENGTH_SHORT).show();
                       phone.setText(phone_number);
                       save_visibility_handle();
                   }
                   if (phone.getText().toString().equals(phone_number))
                   {
                       boolean_edit_phone = false;
                       save_visibility_handle();

                   } else
                   {
                       Toast.makeText(Account_view.this, "Your Phone Number will be Updated", Toast.LENGTH_SHORT).show();
                       boolean_edit_phone = true;
                       save.setVisibility(View.VISIBLE);
                   }
                   count_edit_phone++;
                   phone_edit_button.setImageDrawable(getResources().getDrawable(R.drawable.baseline_edit_24));
               }

           }
       });
    }
    ///Address Handling////////////////
    private void address_edit_handle()
    {
        address_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(count_edit_address%2==0)
                {
                    address_edit_button.setImageDrawable(getResources().getDrawable(R.drawable.baseline_check_circle_24));
                    address.setEnabled(true);
                    count_edit_address++;
                }
                else
                {
                    address.setEnabled(false);
                    if (address.getText().toString().isEmpty())
                    {
                        boolean_edit_address = false;
                        Toast.makeText(Account_view.this, "No Changes Were Made", Toast.LENGTH_SHORT).show();
                        address.setText(address_text);
                        save_visibility_handle();

                    }
                    if (address.getText().toString().equals(address_text))
                    {
                        boolean_edit_address = false;
                        save_visibility_handle();

                    }
                    else
                    {
                        Toast.makeText(Account_view.this, "Your Address will be Updated", Toast.LENGTH_SHORT).show();
                        boolean_edit_address = true;
                        save.setVisibility(View.VISIBLE);
                    }
                    count_edit_address++;
                    address_edit_button.setImageDrawable(getResources().getDrawable(R.drawable.baseline_edit_24));
                }


            }
        });
    }

    ///Save Button Handling ////////////////////
    private void handle_save()
    {
        ///Check For Changes //////////////////
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!boolean_edit_address  && !boolean_edit_hostel_name && !boolean_edit_phone)
                {
                    Toast.makeText(Account_view.this, "No Changes Were Made", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   update_database();
                }

            }
        });

    }
    ///Save Button Visibility Handling////////////
    private void save_visibility_handle()
    {
        if(!boolean_edit_address &&  !boolean_edit_hostel_name && !boolean_edit_phone)
        {
            save.setVisibility(View.GONE);
        }
        else
        {
            save.setVisibility(View.VISIBLE);
        }

    }

    private void update_database()
    {
        if(boolean_edit_address)
        {
            reference.child("Users").child(user.getUid()).child("Personal Details").child("Address").setValue(address.getText().toString());
        }
        if(boolean_edit_hostel_name)
        {
            reference.child("Users").child(user.getUid()).child("Personal Details").child("Hostel Name").setValue(hostel_name.getText().toString());
        }
        if(boolean_edit_phone)
        {
            reference.child("Users").child(user.getUid()).child("Personal Details").child("Phone Number").setValue(phone.getText().toString());
        }
        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }
    ///Options Menu Creation

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            getMenuInflater().inflate(R.menu.account_menu,menu);
            return super.onCreateOptionsMenu(menu);
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
            return true;
        }
        if(item.getItemId()==R.id.settings_menu)
        {
            startActivity(new Intent(Account_view.this,Settings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///Method to Disable Components Before The Edit Buttons are Clicked////////////////
    public void disable_components()
    {
        hostel_name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        address.setEnabled(false);
        type_hostel.setClickable(false);
        save.setVisibility(View.GONE);
    }
    /// Method to get Data from the Database/////////////////////////////
    public void get_Data_From_Database()
    {
        /// Getting Basic Info From Database
        reference.child("Users").child(user.getUid()).child("Personal Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    Hostel_Name=snapshot.child("Hostel Name").getValue(String.class);
                    Owner_Name=snapshot.child("Owner Name").getValue(String.class);
                    profile= Uri.parse(snapshot.child("Profile Picture").getValue().toString());
                    phone_number=snapshot.child("Phone Number").getValue(String.class);
                    address_text=snapshot.child("Address").getValue(String.class);;
                    type=snapshot.child("Hostel Type").getValue(String.class);
                    // Setting Data from the Database
                    Picasso.get().load(profile).into(profile_pic);
                    owner_name.setText(Owner_Name);
                    hostel_name.setText(Hostel_Name);
                    email.setText(user.getEmail());
                    phone.setText(phone_number);
                    address.setText(address_text);
                    type_hostel.setChecked(true);
                    type_hostel.setText(type);
                    getSupportActionBar().setTitle("Hello "+Owner_Name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Account_view.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}