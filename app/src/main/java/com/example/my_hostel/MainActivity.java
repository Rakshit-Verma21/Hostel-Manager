package com.example.my_hostel;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.my_hostel.Adapters.Main_activity_hostler_Adapter;
import com.example.my_hostel.Adapters.Main_activity_room_Adapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainActivity extends AppCompatActivity
{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    /// Data Variables
     String Hostel_Name;
     String Owner_Name;
     Uri profile;
     CircleImageView header_profile_pic ;
    /// Header Layout Components
     TextView header_hostel_name ;
     TextView header_owner_name ;
    /// Main Layout Components
     RecyclerView rv_room_display,rv_hosteler_display;
     Main_activity_room_Adapter adapter_room;
     Main_activity_hostler_Adapter adapter_hosteler;
     ArrayList<String>room=new ArrayList<>();
     ArrayList<String>hosteler=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /// Getting Data From Database
        get_Data_From_Database();
        /// Action Bar
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nv);
        /// Header Layout Components
        View header=navigationView.getHeaderView(0);
        header_hostel_name = header.findViewById(R.id.textView);
        header_owner_name = header.findViewById(R.id.textView5);
        header_profile_pic = header.findViewById(R.id.imageView);
        /// Main Layout Components
        rv_room_display=findViewById(R.id.main_activity_room_list);
        rv_room_display.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter_room=new Main_activity_room_Adapter(MainActivity.this,room,rv_room_display);
        rv_room_display.setAdapter(adapter_room);
        rv_hosteler_display=findViewById(R.id.rv_hostlers_main_activity);
        GridLayoutManager layoutManager = new GridLayoutManager(
                this,
                2,
                GridLayoutManager.HORIZONTAL,
                false);
        rv_hosteler_display.setLayoutManager(layoutManager);
        adapter_hosteler=new Main_activity_hostler_Adapter(MainActivity.this,hosteler,rv_hosteler_display);
        rv_hosteler_display.setAdapter(adapter_hosteler);
        /// Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ////// Navigation Drawer Creation //////
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //Navigation View Menu Items Management
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                /// Menu items Management 
                if(item.getItemId()==R.id.account)
                {
                   startActivity(new Intent(MainActivity.this,Account_view.class));
                }
                else if(item.getItemId()==R.id.settings)
                {
                    startActivity(new Intent(MainActivity.this,Settings.class));
                }
                else if(item.getItemId()==R.id.manage_room)
                {
                    startActivity(new Intent(MainActivity.this,Management_Rooms.class));
                }
                else if(item.getItemId()==R.id.manage_hosteler)
                {
                    startActivity(new Intent(MainActivity.this,Mangement_Hostelers.class));
                }
                else if(item.getItemId()==R.id.report)
                {
                    startActivity(new Intent(MainActivity.this,Report_Activity.class));
                }
                else if(item.getItemId()==R.id.exit)
                {
                    finishAffinity();
                }
                else if(item.getItemId()==R.id.logout)
                {
                    //// Sign Out Alert Message
                    AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Alert User")
                            .setMessage("Do you Want To Sign Out of your Account")
                            .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                   FirebaseAuth.getInstance().signOut();
                                   startActivity(new Intent(MainActivity.this, Login_Activity.class));
                                   finish();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void get_Data_From_Database() {
        /// Getting Basic Info From Database
        reference.child("Users").child(user.getUid()).child("Personal Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Hostel_Name = snapshot.child("Hostel Name").getValue(String.class);
                    Owner_Name = snapshot.child("Owner Name").getValue(String.class);
                    profile = Uri.parse(snapshot.child("Profile Picture").getValue().toString());
                    getSupportActionBar().setSubtitle("Hostel Management for " + Hostel_Name);
                    getSupportActionBar().setTitle("Welcome " + Owner_Name);
                    Picasso.get().load(user.getPhotoUrl()).into(header_profile_pic);
                    header_hostel_name.setText(Hostel_Name);
                    header_owner_name.setText(user.getDisplayName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        reference.child("Users").child(user.getUid()).child("Rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    if (!room.contains(snapshot.getKey())) {
                        room.add(snapshot.getKey());
                    }
                    adapter_room.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        reference.child("Users").child(user.getUid()).child("Hostlers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    if (!hosteler.contains(snapshot.getKey())) {
                        hosteler.add(snapshot.getKey());
                    }
                    adapter_hosteler.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                hosteler.remove(snapshot.getKey());
                adapter_hosteler.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
            finishAffinity();
        }
    }
}