package com.example.my_hostel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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

import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class Settings extends AppCompatActivity
{
    ///Componnents////////////////////////////////
    Button verify,reset_password,change_email;
    ImageButton verified;

    CheckBox email_type;
    Switch phone_link;

    TextView email;
    String phone_number_Linked;

    String phone_number;
    String mail_type;

    /// Screens //////////
    FrameLayout authentication_screen,change_email_screen,change_password_screen;

    ///Screen Components/////
    /// Authentication Screen //////////
    EditText frame_email,frame_password;
    Button authenticate;
    ImageButton button_closeFrame;

    ///Change Email Screen //////////
    EditText new_email;
    ImageButton back_email_screen;
    Button button_verify_email;

    Button email_update_button_final;

    TextView email_help_info;

    ///Change Password Screen //////////
    EditText new_password,confirm_password;
    ImageButton back_password_screen;
    Button update_password;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ///Main Screen IDs
        verify=findViewById(R.id.verify_email);
        verified=findViewById(R.id.verified);
        email_type=findViewById(R.id.checkBox_email_type);
        phone_link=findViewById(R.id.switch_phoneLink);
        reset_password=findViewById(R.id.password_reset_button);
        change_email=findViewById(R.id.change_email_button);
        email=findViewById(R.id.show_email_text);
        ///Frame Layout Screens
        authentication_screen=findViewById(R.id.authenticate_screen);
        change_email_screen=findViewById(R.id.change_email_Screen);
        change_password_screen=findViewById(R.id.change_password_Screen);
        /// IDs for Authentication Screen
        frame_email=findViewById(R.id.frame_email);
        frame_password=findViewById(R.id.frame_password);
        authenticate=findViewById(R.id.authenticate);
        button_closeFrame=findViewById(R.id.button_closeFrame);
        ///IDs for Change Email Screen
        new_email=findViewById(R.id.new_email);
        back_email_screen=findViewById(R.id.back_email_screen);
        button_verify_email=findViewById(R.id.button_update_email);
        email_help_info=findViewById(R.id.text_email_info);
        email_update_button_final=findViewById(R.id.button_update_email_final);
        email_update_button_final.setVisibility(View.GONE);
        ///IDs for Change Password Screen
        new_password=findViewById(R.id.new_password);
        confirm_password=findViewById(R.id.confirm_password);
        back_password_screen=findViewById(R.id.back_password_screen);
        update_password=findViewById(R.id.update_password);
        ///Visibility for Other Screens TO be "GONE" By Default
        change_screen_visibility();
        ///Check User Authentication Type

        ///Get Database
        user.reload();
        get_Data_From_Database();
        ///Check IF Email is Verified
        is_email_Verified();
        ///Handling Button to Verify Email Address If Its Not Already Verified
        verify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                verify_email_address();
            }
        });

        ///Handling Button to Change Email Address
        change_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {  ///use_type_id variable in an Integer type to determine whether user wants to change password or update his email
              ///use_type_id = 1 for changing email
              ///use_type_id = 2 for changing password
                authentication_screen.setVisibility(View.VISIBLE);
                get_authentication_details(1);

            }
        });
        ///Handling Button to Reset Password
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ///use_type_id variable in an Integer type to determine whether user wants to change password or update his email
                ///use_type_id = 1 for changing email
                ///use_type_id = 2 for changing password
                authentication_screen.setVisibility(View.VISIBLE);
                get_authentication_details(2);
            }
        });
        ///Handling Check box
        email_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    AlertDialog.Builder dialog= new AlertDialog.Builder(Settings.this)
                            .setTitle("Change Email Type").setMessage("Changing Email Type to Personal Will Allow For Patch notes and other updates via mail")
                            .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    reference.child("Users").child(user.getUid()).child("Settings").child("Email_Type").setValue("Personal");
                                    Toast.makeText(Settings.this,"Email Type Changed",Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    email_type.setChecked(false);
                                }
                            });
                    dialog.show();
                }
                else
                {
                    reference.child("Users").child(user.getUid()).child("Settings").child("Email_Type").setValue("Not Personal");
                    Toast.makeText(Settings.this, "Email Type Changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /// Phone Authentication switch Handler
        /// Handle Change Phone Link Status
        check_PhoneLinkChange();
    }
    /// Phone Authentication switch Handler
    /// Handle Change Phone Link Status
    private void check_PhoneLinkChange()
    {
        phone_link.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    if(phone_number.isEmpty())
                    {
                        Toast.makeText(Settings.this,"No Phone Number Added",Toast.LENGTH_SHORT).show();
                        phone_link.setChecked(false);
                    }
                    else
                    {
                        reference.child("Users").child(user.getUid()).child("Settings").child("Phone_Number_Linked").setValue("true");
                        Toast.makeText(Settings.this,"Phone Number Linked",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    reference.child("Users").child(user.getUid()).child("Settings").child("Phone_Number_Linked").setValue("false");
                    Toast.makeText(Settings.this,"Phone Number unLinked",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    ///Change Other Screen Visibility
    private void change_screen_visibility()
    {
        authentication_screen.setVisibility(View.GONE);
        change_email_screen.setVisibility(View.GONE);
        change_password_screen.setVisibility(View.GONE);
    }
    ///Get Authentication Details For the User to Re-authenticate account
    private void get_authentication_details(int use_type_id)
    {
        ///use_type_id variable in an Integer type to determine whether user wants to change password or update his email
        ///use_type_id = 1 for changing email
        ///use_type_id = 2 for changing password
        ///Handling Back Button of Authentication Screen
        button_closeFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                authentication_screen.setVisibility(View.GONE);
                frame_email.setText("");
                frame_password.setText("");
            }
        });

        ///Handling Button to Authenticate User
        authenticate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(frame_email.getText().toString().isEmpty() || frame_password.getText().toString().isEmpty())
                {
                    Toast.makeText(Settings.this,"Enter Required Details",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    re_authenticate_user(frame_email.getText().toString(), frame_password.getText().toString(), use_type_id);
                }
            }
        });

    }
    ///Re-authentication of The User
    private void re_authenticate_user(String email, String password,int use_type_id)
    {
        ///This function is used to re-authenticate user when they enter their current password to change their email or password
        ///use_type_id variable in an Integer type to determine whether user wants to change password or update his email
        ///use_type_id = 1 for changing email
        ///use_type_id = 2 for changing password
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(email.isEmpty()||password.isEmpty())
        {
            Toast.makeText(Settings.this,"Enter Required Details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (user != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (use_type_id == 1)
                            {
                                email_update_ScreenHandler();
                            }
                            if (use_type_id == 2)
                            {

                                password_update_ScreenHandler();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(Settings.this,"Incorrect Credentials",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }
    ///Handling The Email Reset/Update Screen
     private void email_update_ScreenHandler()
     {
         change_email_screen.setVisibility(View.VISIBLE);
         authentication_screen.setVisibility(View.GONE);
         /// Back Buton Handler
         back_email_screen.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 change_email_screen.setVisibility(View.GONE);
                 authentication_screen.setVisibility(View.VISIBLE);
                 email_help_info.setText(R.string.email_help_default);
                 new_email.setText("");
             }
         });
         button_verify_email.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v)
             {
                 if(new_email.getText().toString().isEmpty())
                 {
                     Toast.makeText(Settings.this,"Enter the new Email Address",Toast.LENGTH_SHORT).show();
                 }
                 else if(!new_email.getText().toString().contains("@")||!new_email.getText().toString().contains(".com"))
                 {
                     Toast.makeText(Settings.this,"Enter a valid Email Address",Toast.LENGTH_SHORT).show();
                 }
                 else if(new_email.getText().toString().equals(frame_email.getText().toString()))
                 {
                     Toast.makeText(Settings.this,"Email Address is already in use ",Toast.LENGTH_SHORT).show();

                 }
                 else
                 {
                     update_email(new_email.getText().toString());

                 }
             }

         });
         email_update_button_final.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 handle_update_email_process();
             }
         });

     }
     ///Updating The User's Email Address'
    private void update_email(String email)
    {
        if(email!=null)
        {
            if(user!=null)
            {
                user.verifyBeforeUpdateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Settings.this,"Verification Link Sent To Your Email Address!",Toast.LENGTH_SHORT).show();
                            email_help_info.setText(R.string.email_help_required_reauthentication);
                            email_update_button_final.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(Settings.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    private void handle_update_email_process()
    {

                AlertDialog.Builder dialog= new AlertDialog.Builder(Settings.this)
                        .setTitle("Update Confirmation")
                        .setMessage("Verify Your Email Address Through the Link then Proceed to Login ")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                startActivity(new Intent(Settings.this,Login_Activity.class));
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                email_update_ScreenHandler();
                                email_help_info.setText(R.string.email_help_default);
                                email_update_button_final.setVisibility(View.GONE);

                            }
                        });
                dialog.create().show();



    }



    ///Handling The Password Reset/Update Screen
    private void password_update_ScreenHandler()
    {
        change_password_screen.setVisibility(View.VISIBLE);
        authentication_screen.setVisibility(View.GONE);
        /// Back Buton Handler
        back_password_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                change_password_screen.setVisibility(View.GONE);
                authentication_screen.setVisibility(View.VISIBLE);
                new_password.setText("");
                confirm_password.setText("");
            }
        });
        update_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(new_password.getText().toString().isEmpty()||confirm_password.getText().toString().isEmpty())
                {
                    Toast.makeText(Settings.this,"Enter Required Details",Toast.LENGTH_SHORT).show();
                }
                else if(new_password.getText().toString().length()<6)
                {
                    Toast.makeText(Settings.this,"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }
                else if(!new_password.getText().toString().equals(confirm_password.getText().toString()))
                {
                    Toast.makeText(Settings.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    reset_password(new_password.getText().toString());
                }
            }
        });
    }
    ///Handling Button to Reset Password
    private void reset_password(String password)
    {
        if(password!=null)
        {
            if(user!=null)
            {
                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            change_password_screen.setVisibility(View.GONE);
                            authentication_screen.setVisibility(View.GONE);
                            frame_email.setText("");
                            frame_password.setText("");
                            Toast.makeText(Settings.this,"Password Updated",Toast.LENGTH_SHORT).show();
                            user.reload();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(Settings.this,"Error Occurred",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }

    }

    ///Method To Check IF Email Address is Verified
    private void is_email_Verified()
    {
        user.reload();
        if(user.isEmailVerified())
        {
            verify.setVisibility(View.GONE);
            verified.setVisibility(View.VISIBLE);
        }
        else
        {
            verify.setVisibility(View.VISIBLE);
            verified.setVisibility(View.GONE);
        }
    }

    ///Method To Send a Link TO the User email Address to verify it
    private void verify_email_address()
    {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Settings.this,"Verification Email Sent",Toast.LENGTH_SHORT).show();
                            user.reload();
                            if(user.isEmailVerified())
                            {
                                verify.setVisibility(View.GONE);
                                verified.setVisibility(View.VISIBLE);
                                reference.child("Users").child(user.getUid()).child("Settings").child("Email Verified").setValue(true);
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    ///Method To Get Data From Database
    public void get_Data_From_Database()
    {
        /// Getting Basic Info From Database
        user.reload();
        email.setText(user.getEmail());
        reference.child("Users").child(user.getUid()).child("Personal Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    phone_number=snapshot.child("Phone Number").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
        reference.child("Users").child(user.getUid()).child("Settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    phone_number_Linked=  snapshot.child("Phone_Number_Linked").getValue(String.class);
                    mail_type=snapshot.child("Email_Type").getValue(String.class);

                    if(phone_number_Linked.equals("true"))
                    {
                        phone_link.setChecked(true);
                    }
                    else
                    {
                        phone_link.setChecked(false);
                    }
                    // Setting Data from the Database
                    if(mail_type.equals("Personal"))
                    {
                        email_type.setChecked(true);

                    }
                    else
                    {
                        email_type.setChecked(false);

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Settings.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}