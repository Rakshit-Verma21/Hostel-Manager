package com.example.my_hostel;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
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


import com.example.my_hostel.Account.Account_view;
import com.example.my_hostel.Account.Login_Activity;
import com.example.my_hostel.Account.Settings;
import com.example.my_hostel.Adapters.Hostelers.Main_activity_hosteler_Adapter;
import com.example.my_hostel.Adapters.Rooms.Main_activity_room_Adapter;
import com.example.my_hostel.Hostelers.Add_Hostelers;
import com.example.my_hostel.Hostelers.Mangement_Hostelers;
import com.example.my_hostel.Rooms.Add_Room;
import com.example.my_hostel.Rooms.Management_Rooms;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    /// Data Variables
     private String Hostel_Name;
     private String Owner_Name;
     private Uri profile;
     private CircleImageView header_profile_pic ;
    /// Header Layout Components
     private TextView header_hostel_name ;
     private TextView header_owner_name ;
    /// Main Layout Components
     private RecyclerView rv_room_display,rv_hosteler_display;
     private Main_activity_room_Adapter adapter_room;
     private Main_activity_hosteler_Adapter adapter_hosteler;
     private  ArrayList<String>room=new ArrayList<>();
     private  ArrayList<String>hosteler=new ArrayList<>();

     private TextView room_details;
     private TextView hostler_details;
     private TextView owner_name_display;
     private TextView hostel_name_display;
    FloatingActionButton fabMain, fabOption1, fabOption2, fabOption3;
    LinearLayout fabMenu;
    boolean isOpen = false;
    Animation slideIn, slideOut;

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


        slideIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in);
        slideOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out);

        /// Action Bar
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nv);
        room_details=findViewById(R.id.main_activity_room_details);
        hostler_details=findViewById(R.id.main_activity_hostler_details);
        owner_name_display = findViewById(R.id.textView21);
        hostel_name_display = findViewById(R.id.textView19);

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
        rv_hosteler_display = findViewById(R.id.rv_hostlers_main_activity);
        GridLayoutManager layoutManager = new GridLayoutManager(
                this,
                2,
                GridLayoutManager.HORIZONTAL,
                false);
        rv_hosteler_display.setLayoutManager(layoutManager);
        adapter_hosteler=new Main_activity_hosteler_Adapter(MainActivity.this,hosteler,rv_hosteler_display);
        rv_hosteler_display.setAdapter(adapter_hosteler);
        room_details.setText("Loading...");
        hostler_details.setText("Loading...");
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
                    Intent intent = new Intent(MainActivity.this, Account_view.class);
                    intent.putStringArrayListExtra("room_list", room);
                    intent.putStringArrayListExtra("hosteler_list", hosteler);
                    startActivity(intent);

                }
                else if(item.getItemId()==R.id.settings)
                {
                    startActivity(new Intent(MainActivity.this, Settings.class));
                }
                else if(item.getItemId()==R.id.manage_room)
                {
                    Intent intent = new Intent(MainActivity.this, Management_Rooms.class);
                    intent.putStringArrayListExtra("room_list", room);
                    startActivity(intent);
                }
                else if(item.getItemId()==R.id.manage_hosteler)
                {
                    Intent intent = new Intent(MainActivity.this, Mangement_Hostelers.class);
                    intent.putStringArrayListExtra("hostler_list", hosteler);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.manage_payment)
                {
                    startActivity(new Intent(MainActivity.this,Payment_Main.class));

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
        ///  Floating action Button [ FAB ]
        fabMain = findViewById(R.id.fabMain);
        fabOption1 = findViewById(R.id.fabOption1);
        fabOption2 = findViewById(R.id.fabOption2);
        fabOption3=findViewById(R.id.fabOption3);
        fabMenu = findViewById(R.id.fabMenu);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMenu();
            }
        });

        fabOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add_Hostelers.class));

            }
        });

        fabOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add_Room.class));

            }
        });
        fabOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this,MainActivity.class));

            }
        });
    }
private void toggleFabMenu() {
    if (isOpen) {
        fabMenu.startAnimation(slideOut);
        fabMenu.setVisibility(View.GONE);
        fabMain.setImageResource(R.drawable.outline_add_24); // plus icon
        isOpen = false;
    } else {
        fabMenu.startAnimation(slideIn);
        fabMenu.setVisibility(View.VISIBLE);
        fabMain.setImageResource(R.drawable.outline_arrow_forward_24); // close icon
        isOpen = true;
    }
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

    /// Set Total Rooms and no of Rooms Full or Empty
    private void setbasic_details()
    {

        AtomicInteger count_available_room = new AtomicInteger(0);
        AtomicInteger count_full_room = new AtomicInteger(0);
        AtomicInteger completed = new AtomicInteger(0);

        reference.child("Users")
                .child(user.getUid())
                .child("Personal Details")
                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot)
                    {
                        String hostel_type = dataSnapshot.child("Hostel Type").getValue(String.class);
                        if(hosteler.isEmpty())
                        {
                            hostler_details.setText(String.format("Hostel Type : %s\nNo Hostlers Available", hostel_type));
                            return;
                        }
                        else
                        {
                            hostler_details.setText(String.format("Hostel Type: %s\n" + "Total Members: %d ",hostel_type,hosteler.size()));
                        }
                    }
                });

        for (String i : room) {

            reference.child("Users")
                    .child(user.getUid())
                    .child("Rooms")
                    .child(i)
                    .child("Status")
                    .get()
                    .addOnSuccessListener(snapshot -> {

                        String status = snapshot.getValue(String.class);

                        if ("Available".equals(status)) {
                            count_available_room.incrementAndGet();
                        } else {
                            count_full_room.incrementAndGet();
                        }

                        // ✅ Update UI only when all async calls finish
                        if (completed.incrementAndGet() == room.size()) {
                            room_details.setText(String.format(
                                    "No of Rooms: %d\nFull Rooms: %d\nAvailable Rooms: %d",
                                    room.size(),
                                    count_full_room.get(),
                                    count_available_room.get()
                            ));
                        }

                    });

        }
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
                    getSupportActionBar().setTitle("Home");
                    owner_name_display.setText(String.format("Welcome Back! %s", Owner_Name));
                    hostel_name_display.setText(Hostel_Name);
                    if(profile!=null){
                        Picasso.get().load(user.getPhotoUrl()).into(header_profile_pic);
                    }
                    else {
                        header_profile_pic.setImageResource(R.drawable.pfp);
                    }

                    header_hostel_name.setText(Hostel_Name);
                    header_owner_name.setText(user.getDisplayName());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        reference.child("Users").child(user.getUid()).child("Rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    rv_room_display.setVisibility(View.VISIBLE);
                    room_details.setText("Loading...");
                    reference.child("Users").child(user.getUid()).child("Rooms").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            if (!room.contains(snapshot.getKey())) {
                                room.add(snapshot.getKey());
                            }

                        }
                        adapter_room.notifyDataSetChanged();
                        setbasic_details();

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

                }
                else
                {
                    room_details.setText("No Rooms");
                    rv_room_display.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Users").child(user.getUid()).child("Hostlers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    rv_hosteler_display.setVisibility(View.VISIBLE);
                    hostler_details.setText("Loading...");
                    reference.child("Users").child(user.getUid()).child("Hostlers").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.exists())
                            {
                                if (!hosteler.contains(snapshot.getKey())) {
                                    hosteler.add(snapshot.getKey());

                                }

                            }
                            adapter_hosteler.notifyDataSetChanged();
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
                else
                {
                    hostler_details.setText("0 Members");
                    rv_hosteler_display.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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