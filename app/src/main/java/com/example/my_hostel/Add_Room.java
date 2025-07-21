package com.example.my_hostel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Room extends AppCompatActivity
{
    private TextView room_name_confirmed,room_size_confirmed,room_type_confirmed;
    private EditText room_name,room_size,room_type;
    private Button add_room_button_confirm,add_room_button_add;
    int count=0;

    private String ROOM_NUMBER,ROOM_TYPE;
    private int ROOM_SIZE;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_room);
        Toolbar toolbar=findViewById(R.id.toolbar_addRoom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Room");
        room_name=findViewById(R.id.add_room_name);
        room_size=findViewById(R.id.add_room_size);
        room_type=findViewById(R.id.add_room_type);
        room_name_confirmed=findViewById(R.id.add_room_confirm_room_name);
        room_size_confirmed=findViewById(R.id.add_room_confirm_room_size);
        room_type_confirmed=findViewById(R.id.add_room_confirm_room_type);
        add_room_button_confirm=findViewById(R.id.add_room_button_confirm);
        add_room_button_add=findViewById(R.id.add_room_button_add);
        add_room_button_add.setVisibility(View.INVISIBLE);
        room_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                room_name_confirmed.setText("Room Name : "+room_name.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                room_name_confirmed.setText("Room Name : "+room_name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                room_name_confirmed.setText("Room Name : "+room_name.getText().toString());
            }
        });
        room_size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                room_size_confirmed.setText("Room Size : "+room_size.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                room_size_confirmed.setText("Room Size : "+room_size.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                room_size_confirmed.setText("Room Size : "+room_size.getText().toString());
            }
        });
        room_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                room_type_confirmed.setText("Type : "+room_type.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                room_type_confirmed.setText("Type : "+room_type.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                room_type_confirmed.setText("Type : "+room_type.getText().toString());
            }
        });
        add_room_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(count%2==0)
                {
                    get_room_details_entry();
                    count++;
                }
                else
                {
                    add_room_button_confirm.setText("Confirm");
                    enable_editTexts();
                    count++;
                }
            }
        });
        add_room_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                update_Database();
            }
        });
    }
    private  void update_Database()
    {
        Toast.makeText(Add_Room.this,"Room Added Successfully",Toast.LENGTH_SHORT).show();
        database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Occupied").setValue(0);
        database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Is Empty").setValue("True");
        database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Type").setValue(ROOM_TYPE);
        database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Size").setValue(ROOM_SIZE);
        database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Status").setValue("Available");
        finish();
    }
    private void disable_editTexts()
    {
        room_name.setEnabled(false);
        room_size.setEnabled(false);
        room_type.setEnabled(false);
        add_room_button_add.setVisibility(View.VISIBLE);
    }
    private void enable_editTexts()
    {
        room_name.setEnabled(true);
        room_size.setEnabled(true);
        room_type.setEnabled(true);
        add_room_button_add.setVisibility(View.INVISIBLE);
    }
    private void get_room_details_entry()
    {
        if(!room_name.getText().toString().isEmpty() && !room_size.getText().toString().isEmpty() && !room_type.getText().toString().isEmpty())
        {
            if(room_name.getText().toString().length()>3)
            {
                Toast.makeText(this,"The Name Should be Less than 3 Characters Example 2A or 3C",Toast.LENGTH_SHORT).show();
            }
            else if(room_size.getText().toString().length()>1)
            {
                Toast.makeText(this,"The Size Should be Less than 2 Characters Example 2 or 3",Toast.LENGTH_SHORT).show();
            }
            else
            {
                String room_name_string=room_name.getText().toString();
                String room_size_string=room_size.getText().toString();
                String room_type_string=room_type.getText().toString();
                confirm_room_details(room_name_string,room_size_string,room_type_string);
                add_room_button_confirm.setText("Change Details ?");
                disable_editTexts();
            }
        }
        else
        {
            Toast.makeText(this,"Please Enter All Details",Toast.LENGTH_SHORT).show();
        }
    }
    private void confirm_room_details(String room_name,String room_size,String room_type)
    {
        room_name_confirmed.setText("Room Name : "+room_name);
        room_size_confirmed.setText("Room Size : "+room_size);
        room_type_confirmed.setText("Type : "+room_type);
        ROOM_TYPE=room_type;
        ROOM_SIZE=Integer.parseInt(room_size);
        ROOM_NUMBER=room_name;
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed()
    {
       finish();
        super.onBackPressed();
    }
}