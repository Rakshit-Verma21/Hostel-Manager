package com.example.my_hostel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {
    Button create_account;
    Button login;
    EditText phone_number;
    EditText email;
    EditText password;
    ProgressBar bar;


    TextView forgot_password;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        create_account = findViewById(R.id.button2);
        login = findViewById(R.id.button);
        phone_number = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextText__email);
        password = findViewById(R.id.editTextTextPassword);
        bar=findViewById(R.id.progressBar);
        bar.setVisibility(View.GONE);
        forgot_password=findViewById(R.id.forgot_password);



        ///Forgot Password
        forgot_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle=new Bundle();
                try
                {
                    bundle.putString("email", email.getText().toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Fragment fragment=new Reset_password();
                fragment.setArguments(bundle);
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        /// Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(Login_Activity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sign_in();
                }
            }
        });
        // Create Account
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Sign_up_Activity.class);
                startActivity(intent);
                bar.setVisibility(View.GONE);
            }
        });
    }

    public void sign_in()
    {
        //// Sign In
        try {
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user = auth.getCurrentUser();
                        bar.setVisibility(View.VISIBLE);
                        check_Phone_requirement(user.getUid());
                    } else
                    {
                        Toast.makeText(Login_Activity.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                    }
                }

            }).addOnFailureListener(new OnFailureListener()
                {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login_Activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void check_Phone_requirement(String uid) {
        // Check if User Had enabled Phone Number Requirement [ Phone Link Check Box ]
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(uid).child("Settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        String phone_number_requirement = snapshot.child("Phone_Number_Linked").getValue(String.class);
                        if (phone_number_requirement.equals("Yes"))
                        {
                            if (phone_number.getText().toString().isEmpty())
                            {
                                Toast.makeText(Login_Activity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                                bar.setVisibility(View.GONE);
                            }
                            else
                            {
                                /// Verify The Input Phone Number with the One In Database if Phone Link is Enabled
                                verify_phone_number(uid);
                            }

                        }
                        else
                        {
                            if(!phone_number.getText().toString().isEmpty())
                            {
                                // IF Phone Link is Disabled For the User But the User Still inputs a Phone Number in the Prompt
                                AlertDialog dialog=new AlertDialog.Builder(Login_Activity.this)
                                        .setTitle("Alert User").setMessage("The Account You are Trying to Sign in Does Not has Phone Number Link Enabled hence any input is Disregarded")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which)
                                                    {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                bar.setVisibility(View.GONE);
                            }
                            // Login To the Account
                            phone_number.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 3000);

                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bar.setVisibility(View.GONE);
            }
        });
    }

    public void verify_phone_number(String uid) {
        /// Verify Phone Number in Database With The Enterd one

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(uid).child("Personal Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    if (snapshot.exists())
                    {
                        String phone_number_text = snapshot.child("Phone Number").getValue(String.class);
                        if (phone_number_text.equals(phone_number.getText().toString()))
                        {
                            Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else
                        {
                            bar.setVisibility(View.GONE);
                            Toast.makeText(Login_Activity.this, "Phone Number is not verified PLease Check ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onStart()
    {
        /// Check if User is Already Logged In
        super.onStart();
        if(auth.getCurrentUser()!=null)
        {
          Intent intent=new Intent(Login_Activity.this,MainActivity.class);
            startActivity(intent);
          finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}








