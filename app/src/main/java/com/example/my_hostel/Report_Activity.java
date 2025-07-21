package com.example.my_hostel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report_Activity extends AppCompatActivity
{
    private EditText email;
    private EditText subject;
    private EditText message;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        Toolbar toolbar=findViewById(R.id.toolbar_report_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer Support");
        email=findViewById(R.id.report_activity_email);
        subject=findViewById(R.id.report_activity_subject);
        message=findViewById(R.id.editTextTextMultiLine);
        send=findViewById(R.id.button3);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    check_editTexts();

            }
        });
    }
    public void check_editTexts()
    {
        if(email.getText().toString().isEmpty()||subject.getText().toString().isEmpty()||message.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
        }
        else if(!email.getText().toString().endsWith("@gmail.com"))
        {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
        }
        else if(subject.getText().toString().length()<3 || message.getText().toString().length()<20)
        {
            Toast.makeText(this, "Message Must Be More Than 20 Characters", Toast.LENGTH_SHORT).show();
        }
        else
        {
            send_report(email.getText().toString(),subject.getText().toString(),message.getText().toString());
        }
    }
    public void send_report(String email,String subject,String message)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        String key=reference.child("Reports").push().getKey();
        reference.child("Reports").child(key).child("Email").setValue(email);
        reference.child("Reports").child(key).child("Subject").setValue(subject);
        reference.child("Reports").child(key).child("Message").setValue(message);
        Toast.makeText(this, "Report Sent Successfully", Toast.LENGTH_SHORT).show();
        this.email.setText("");
        this.subject.setText("");
        this.message.setText("");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.report_menu,menu);
        menu.findItem(R.id.clear_report).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item)
            {
                email.setText("");
                subject.setText("");
                message.setText("");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}