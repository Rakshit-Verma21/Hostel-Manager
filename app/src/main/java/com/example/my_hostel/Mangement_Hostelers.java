package com.example.my_hostel;

import android.content.Intent;
import android.os.Bundle;

import com.example.my_hostel.Adapters.ManagementHostlers_Display_Adapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mangement_Hostelers extends AppCompatActivity
{
    private RecyclerView rv_hostler_display;
    private SearchView search_hostlers;
    private ManagementHostlers_Display_Adapter adapter;
    private ArrayList<String> hostler_uid_list = new ArrayList<>();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mangement_hostelers);
        Toolbar toolbar= findViewById(R.id.toolbar_management_hostelers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hostelers Management");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_hostler_display = findViewById(R.id.rv_management_hostlers);
        search_hostlers = findViewById(R.id.search_hostlers);
        getHostlers_id();
        rv_hostler_display.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new ManagementHostlers_Display_Adapter(this,hostler_uid_list);
        rv_hostler_display.setAdapter(adapter);

        search_hostlers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                filterData(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                filterData(newText);
                return false;
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return false;
    }
    private void filterData(String query)
    {
        ArrayList<String> filtered_list = new ArrayList<>();
        for(String uid : hostler_uid_list)
        {
            reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists()) {

                            if (snapshot.child("Name").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                                if(!filtered_list.contains(uid)) {
                                    filtered_list.add(uid);
                                }
                            }

                    }}
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}});
            reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Education Details").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {

                            if (snapshot.child("Current Degree").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                                if(!filtered_list.contains(uid)) {
                                    filtered_list.add(uid);
                                }
                            }
                            if (snapshot.child("College").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                                if(!filtered_list.contains(uid)) {
                                    filtered_list.add(uid);
                                }
                            }
                            if (snapshot.child("Education Details").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                                if(!filtered_list.contains(uid)) {
                                    filtered_list.add(uid);
                                }
                            }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}});
            reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists())
                    {
                        if (snapshot.child("Room").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                            if(!filtered_list.contains(uid)) {
                                filtered_list.add(uid);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}});
        }
        adapter = new ManagementHostlers_Display_Adapter(this,filtered_list);
        rv_hostler_display.setAdapter(adapter);
    }
    private void getHostlers_id()
    {
        reference.child("Users").child(user.getUid()).child("Hostlers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String uid = snapshot.getKey();
                if(!hostler_uid_list.contains(uid))
                {
                    hostler_uid_list.add(uid);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot)
            {
                String uid = snapshot.getKey();
                if(hostler_uid_list.contains(uid))
                {
                    hostler_uid_list.remove(uid);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.hostelers_management_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_hostelers) {
            startActivity(new Intent(Mangement_Hostelers.this, Add_Hostlers.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

}