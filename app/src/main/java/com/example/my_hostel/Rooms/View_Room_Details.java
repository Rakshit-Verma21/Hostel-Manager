package com.example.my_hostel.Rooms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.Adapters.Rooms.Room_Assigned_To_Adapter;
import com.example.my_hostel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Room_Details extends AppCompatActivity
{
    private String ROOM_NUMBER="";
    private DatabaseReference database= FirebaseDatabase.getInstance().getReference();
    private String ROOM_PATH="Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Rooms/"+ROOM_NUMBER;
    private ArrayList<String> room_mates_id_list=new ArrayList<>();
    private RecyclerView recyclerView;
    private Room_Assigned_To_Adapter adapter;
    private TextView room_name,room_size,room_status,room_occupied,room_type,room_is_empty,textView75,room_price;
    private EditText change_room_price;
    private FloatingActionButton edit_room_price_fb;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_room_details);
        ROOM_NUMBER=getIntent().getStringExtra("Room_Uid");
        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar_view_room_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Room "+ROOM_NUMBER);
        room_name=findViewById(R.id.VRD_room_name);
        room_name.setText("Room "+ROOM_NUMBER);
        room_size=findViewById(R.id.VRD_room_size);
        room_status=findViewById(R.id.VRD_room_status);
        room_occupied=findViewById(R.id.VRD_room_occupied);
        room_price=findViewById(R.id.price_text_View);
        room_type=findViewById(R.id.VRD_room_type);
        room_is_empty=findViewById(R.id.VRD_room_is_empty);
        recyclerView=findViewById(R.id.VRD_recycler_view);
        change_room_price=findViewById(R.id.room_price_edit);
        edit_room_price_fb=findViewById(R.id.edit_room_price_fb);
        change_room_price.setEnabled(false);
        change_room_price.setVisibility(View.GONE);
        textView75=findViewById(R.id.textView72);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        getRoomDetails();
        adapter=new Room_Assigned_To_Adapter(room_mates_id_list,this,ROOM_NUMBER,textView75);
        recyclerView.setAdapter(adapter);
        edit_room_price_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count%2 == 0)
                {
                    ///  Change Room price Handler
                    change_room_price.setEnabled(true);
                    change_room_price.setVisibility(View.VISIBLE);
                    change_room_price.setText("");
                    edit_room_price_fb.setImageDrawable(getDrawable(R.drawable.baseline_check_circle_24));

                }
                else
                {
                    change_room_price.setEnabled(false);
                    // Change Room Price Function Call
                    change_price();
                    change_room_price.setVisibility(View.GONE);
                    edit_room_price_fb.setImageDrawable(getDrawable(R.drawable.baseline_edit_24));


                }
                count++;
            }
        });
    }
    private void change_price()
    {
        if(change_room_price.getText().toString().isEmpty())
        {
            Toast.makeText(View_Room_Details.this, "No Input Provided", Toast.LENGTH_SHORT).show();
        }
        else if(change_room_price.getText().toString().length()<4)
        {
            Toast.makeText(View_Room_Details.this, "Room Price Cannot Be Less Than Rs. 1000", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AlertDialog dialog=new AlertDialog.Builder(View_Room_Details.this)
                    .setTitle("Confirm")
                    .setMessage("Do you Want To Change Room Price").setPositiveButton("Change", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            database.child(ROOM_PATH).child(ROOM_NUMBER).child("Price").setValue(Integer.parseInt(change_room_price.getText().toString()));
                            Toast.makeText(View_Room_Details.this, "Room Price Changed", Toast.LENGTH_SHORT).show();
                            room_mates_id_list.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();



        }

    }
    private void getRoomDetails()
    {
        database.child(ROOM_PATH).child(ROOM_NUMBER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    room_size.setText("Size : "+snapshot.child("Size").getValue(Integer.class));
                    room_status.setText("Status : "+snapshot.child("Status").getValue(String.class));
                    room_occupied.setText("Occupied : "+snapshot.child("Occupied").getValue(Integer.class));
                    room_type.setText("Type : "+snapshot.child("Type").getValue(String.class));
                    room_is_empty.setText("Is Empty : "+snapshot.child("Is Empty").getValue(String.class));
                    room_price.setText("Price : Rs "+snapshot.child("Price").getValue(Integer.class));
                    for(DataSnapshot snap:snapshot.child("Assigned To").getChildren())
                    {
                        room_mates_id_list.add(snap.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }
    @Override
    public boolean onSupportNavigateUp() {
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