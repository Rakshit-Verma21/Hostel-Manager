package com.example.my_hostel.Account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.my_hostel.MainActivity;
import com.example.my_hostel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
   private Button create_account , login ;

    private EditText email,password;

    private ProgressBar bar;

    private TextView forgot_password;
    private FirebaseAuth auth = FirebaseAuth.getInstance();


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
                    bar.setVisibility(View.VISIBLE);
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

    private void sign_in()
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

                        // Login To the Account
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bar.setVisibility(View.GONE);
                                Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 5000);

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








