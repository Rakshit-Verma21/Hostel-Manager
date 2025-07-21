package com.example.my_hostel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.Adapters.Room_Assigned_To_Adapter;
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
    private TextView room_name,room_size,room_status,room_occupied,room_type,room_is_empty,textView75;
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
        room_name.setText(ROOM_NUMBER);
        room_size=findViewById(R.id.VRD_room_size);
        room_status=findViewById(R.id.VRD_room_status);
        room_occupied=findViewById(R.id.VRD_room_occupied);
        room_type=findViewById(R.id.VRD_room_type);
        room_is_empty=findViewById(R.id.VRD_room_is_empty);
        recyclerView=findViewById(R.id.VRD_recycler_view);
        textView75=findViewById(R.id.textView75);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        getRoomDetails();
        adapter=new Room_Assigned_To_Adapter(room_mates_id_list,this,ROOM_NUMBER,textView75);
        recyclerView.setAdapter(adapter);
    }
    public void getRoomDetails()
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