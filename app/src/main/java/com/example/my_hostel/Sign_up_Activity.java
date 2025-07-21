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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sign_up_Activity extends AppCompatActivity {

    CircleImageView profile_pic;
    Uri image_uri;
    /// Profile URI

    Button create_account;
    EditText email;
    EditText password;
    EditText password_confirm;
    EditText hostel_name;
    EditText owner_name;
    EditText address;
    EditText phone_number;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    /// Girls or boys Type Hostel
    RadioGroup type_hostel;
    RadioButton type_hostel_boys;
    RadioButton type_hostel_girls;

    CheckBox email_personal;
    /// Is Email Personal or not

    CheckBox Link_phone;

    /// Link Phone Number So it will be Required During Login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profile_pic = findViewById(R.id.profile_pic);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);
        hostel_name = findViewById(R.id.hostel_name);
        owner_name = findViewById(R.id.owner_name);
        address = findViewById(R.id.address);
        phone_number = findViewById(R.id.phone_number);
        type_hostel_boys = findViewById(R.id.radioButton_boys);
        type_hostel_girls = findViewById(R.id.radioButton_girls);
        type_hostel = findViewById(R.id.type_hostel_radiogroup);
        email_personal = findViewById(R.id.checkBox4);
        create_account = findViewById(R.id.sign_up);
        Link_phone = findViewById(R.id.Link_phone);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// Create Account
                sign_up_Check();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Link_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog dialog = new AlertDialog.Builder(Sign_up_Activity.this)
                            .setTitle("Link Phone Number")
                            .setMessage("Enable the Requirement of Phone Number Linked with the account the Next time You Login")
                            .setPositiveButton("Link", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /// Sign Up
                                    Link_phone.setChecked(true);
                                    Toast.makeText(Sign_up_Activity.this, "Phone Number will be Linked To Your Account", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Link_phone.setChecked(false);
                                }
                            }).show();
                } else {

                }

            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_selector();

            }
        });


    }

    public void image_selector()
    {
        // Select Image For Profile

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            image_uri=data.getData();
            profile_pic.setImageURI(data.getData());
        }
    }

    ////Sign Up _ Check For Required Fields Are Filled
    public void sign_up_Check()
    {
        /// Create Account
        if(email.getText().toString().isEmpty()||(password.getText().toString().isEmpty())||(password_confirm.getText().toString().isEmpty())||(hostel_name.getText().toString().isEmpty())||(owner_name.getText().toString().isEmpty()))
        {
            Toast.makeText(this, "Please fill all the fields Marked * ", Toast.LENGTH_SHORT).show();
        }
        else if(!type_hostel_boys.isChecked() && !type_hostel_girls.isChecked())
        {
            Toast.makeText(this, "Please Select Type of Hostel", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!(password.getText().toString()).equals(password_confirm.getText().toString()))
            {
                Toast.makeText(this, "Password and Confirm Password are not same", Toast.LENGTH_SHORT).show();
            }
            else if(!Link_phone.isChecked())
            {
                AlertDialog dialog= new AlertDialog.Builder(this)
                        .setTitle("Do you want to Continue ?")
                        .setMessage("You can link your phone number later")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                /// Sign Up
                                String email_text=email.getText().toString();
                                String password_text=password.getText().toString();
                                ///// Calling The Main Sign UP Method to Create An Account
                                sign_up(email_text,password_text);
                            }
                        }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        }).show();
            }
            else
            {
                /// Sign Up
                ///// Calling The Main Sign UP Method to Create An Account
                String email_text=email.getText().toString();
                String password_text=password.getText().toString();
                sign_up(email_text,password_text);
            }
        }
    }

    public void sign_up(String mail,String password)
    {
        auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            /// Sign Up Success
                            database_entry();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(Sign_up_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void database_entry()
    {
        /// Getting Data
            String email_text = email.getText().toString().replace(".", ",");
            String hostel_name_text = hostel_name.getText().toString();
            String owner_name_text = owner_name.getText().toString();
            String address_text;
            String is_Link_phone_enabled;
            if(Link_phone.isChecked())
            {
                is_Link_phone_enabled = "true";
            }
            else
            {
                is_Link_phone_enabled = "false";

            }
            if(!address.getText().toString().isEmpty())
            {
                address_text = address.getText().toString();
            }
            else
            {
                address_text = "";
            }
            String phone_number_text = phone_number.getText().toString();
            String type_hostel_text;
            if (type_hostel_boys.isChecked())
            {
                type_hostel_text = "Boys";
            } else
            {
                type_hostel_text = "Girls";
            }
            String email_personal_text;
            if (email_personal.isChecked())
            {
                email_personal_text = "Personal";
            }
            else
            {
                email_personal_text = "Not Personal";
            }
        /////// Database Entry
            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Email").setValue(email_text);
            databaseReference.child("Users").child(auth.getUid()).child("Settings").child("Email_Type").setValue(email_personal_text);
            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Hostel Name").setValue(hostel_name_text);
            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Owner Name").setValue(owner_name_text);
            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Hostel Type").setValue(type_hostel_text);
            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Address").setValue(address_text);
            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Phone Number").setValue(phone_number_text);
            databaseReference.child("Users").child(auth.getUid()).child("Settings").child("Phone_Number_Linked").setValue(is_Link_phone_enabled);
            /// Uploading Profile
            storageReference.child(auth.getUid()).child("Profile_Picture").child("profile.jpg").putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    storageReference.child(auth.getUid()).child("Profile_Picture").child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            databaseReference.child("Users").child(auth.getUid()).child("Personal Details").child("Profile Picture").setValue(uri.toString());
                            UserProfileChangeRequest userChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uri).setDisplayName(owner_name_text).build();
                            auth.getCurrentUser().updateProfile(userChangeRequest);
                            Toast.makeText(Sign_up_Activity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sign_up_Activity.this,MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(Sign_up_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Sign_up_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_up_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        if(item.getItemId()==android.R.id.home)
        {
            finishActivity(1);
        }
        if (item.getItemId()==R.id.reset)
        {
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}