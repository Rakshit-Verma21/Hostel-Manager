package com.example.my_hostel;
import android.content.Intent;
import android.os.Bundle;;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.my_hostel.Adapters.Rooms_display_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Management_Rooms extends AppCompatActivity
{
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private String ROOM_PATH="Users/"+user.getUid()+"/Rooms";
    ArrayList<String> room_uid_list=new ArrayList<>();
    private RecyclerView recyclerView_rooms;
    private SearchView searchView_filter_rooms;
    private Rooms_display_Adapter adapter;
    private DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_rooms);
        Toolbar toolbar= findViewById(R.id.management_Rooms_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rooms Management");
        recyclerView_rooms=findViewById(R.id.rv_rooms_room_management);
        searchView_filter_rooms=findViewById(R.id.search_room_management);
        getRooms();
        recyclerView_rooms.setLayoutManager(new GridLayoutManager(this,2));
        adapter=new Rooms_display_Adapter(Management_Rooms.this,room_uid_list);
        recyclerView_rooms.setAdapter(adapter);

        searchView_filter_rooms.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                filterRooms(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                filterRooms(newText);
                return false;
            }
        });
    }
    public void filterRooms(String query)
    {
        ArrayList<String> filtered_list=new ArrayList<>();
        for(String uid : room_uid_list)
        {
            database.child(ROOM_PATH).child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists())
                    {
                        if(snapshot.child("Status").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                            if(!filtered_list.contains(uid))
                            {
                                filtered_list.add(uid);
                            }
                        }
                        int size=snapshot.child("Size").getValue(Integer.class);
                        if(String.valueOf(size).contains(query))
                        {
                            if(!filtered_list.contains(uid))
                            {
                                filtered_list.add(uid);
                            }
                        }
                        if(snapshot.child("Type").getValue(String.class).toLowerCase().contains(query.toLowerCase())) {
                            if (!filtered_list.contains(uid)) {
                                filtered_list.add(uid);
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        adapter=new Rooms_display_Adapter(Management_Rooms.this,filtered_list);
        recyclerView_rooms.setAdapter(adapter);
    }

    public void getRooms()
    {
        database.child("Users").child(user.getUid()).child("Rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                if(!room_uid_list.contains(snapshot.getKey().toString()))
                {
                    room_uid_list.add(snapshot.getKey().toString());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(!room_uid_list.contains(snapshot.getKey()))
                {
                    room_uid_list.remove(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.room_management_menu,menu);
        menu.findItem(R.id.add_room).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item)
            {
                Intent intent=new Intent(Management_Rooms.this,Add_Room.class);
                startActivity(intent);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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