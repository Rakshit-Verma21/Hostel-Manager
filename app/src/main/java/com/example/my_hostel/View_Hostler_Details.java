package com.example.my_hostel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class View_Hostler_Details extends AppCompatActivity
{
    private String Main_UID_HOSTLER;
    private Toolbar toolbar;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    ///Components
    ///
    private ProgressBar imageUpload_ProgressBar;
    private TextView name,phone_number,father_name,mother_name,room_number;
    private EditText Address,CPD,college,father_phone_number,mother_phone_number;
    private Spinner room_spinner;
    private ArrayAdapter<String> room_Mates_listAdapter;
    private ArrayList<String> room = new ArrayList<>();
    private CircleImageView pic;
    private ImageView aadhar_pic,pan_pic;
    private String Hostler_name;
    private String ROOM_NUMBER;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Main_UID_HOSTLER=getIntent().getStringExtra("Hostler_Uid");
        System.out.println(Main_UID_HOSTLER);
        setContentView(R.layout.activity_view_hostler_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar_view_hostler_details);
        setSupportActionBar(toolbar);

        pic = findViewById(R.id.H_details_pic);
        name = findViewById(R.id.H_details_name);
        phone_number = findViewById(R.id.H_details_phone_Number);
        phone_number.setEnabled(false);
        room_number = findViewById(R.id.room_assigned);

        Address = findViewById(R.id.H_Details_Address);
        CPD = findViewById(R.id.view_hostler_CPD);
        college = findViewById(R.id.view_hostler_college);

        father_name = findViewById(R.id.view_hostler_father_name);
        mother_name = findViewById(R.id.view_hostler_mother_name);
        father_phone_number = findViewById(R.id.view_hostler_father_phone);
        mother_phone_number = findViewById(R.id.view_hostler_mother_phone);

        aadhar_pic = findViewById(R.id.view_hostler_aadhar);
        pan_pic = findViewById(R.id.view_hostler_pan);

        imageUpload_ProgressBar=findViewById(R.id.progressBar_View_hostlers);
        imageUpload_ProgressBar.setVisibility(View.INVISIBLE);

        room_spinner = findViewById(R.id.view_hosteler_spinner_Room);
        room_Mates_listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,room);
        room_Mates_listAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        room_spinner.setAdapter(room_Mates_listAdapter);

        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String room_number_string = snapshot.child("Room").getValue(String.class);
                            ROOM_NUMBER=room_number_string;
                            room_number.setText("Room "+room_number_string);
                            if(room_number_string.equals("Not Assigned"))
                            {
                                room_number.setVisibility(View.GONE);
                                getRoomsData();
                            }
                            else
                            {
                                room_spinner.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
        room_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position==0)
                {}
                else
                {
                    String room_number_string=room.get(position);
                    assign_room(room_number_string);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        get_Details();
        disable_Edits();
    }
    public void assign_room(String room_number_string)
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(View_Hostler_Details.this);
        dialog.setMessage("Are you sure you want to assign this room ?")
                .setTitle("Assign Room"+room_number_string)
                .setPositiveButton("Assign", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        room_spinner.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                        reference.child("Users")
                                .child(user.getUid())
                                .child("Hostlers")
                                .child(Main_UID_HOSTLER).child("Room").setValue(room_number_string);
                        UpdateRoomAssignment(room_number_string);
                        room_number.setVisibility(View.VISIBLE);
                        room_number.setText("Room "+room_number_string);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        room_spinner.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }).create().show();
    }
    public void UpdateRoomAssignment(String ROOM_NUMBER_1)
    {
        reference.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER_1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int size =  snapshot.child("Size").getValue(Integer.class);
                int Occupied_no=  snapshot.child("Occupied").getValue(Integer.class);
                if(Occupied_no!=size)
                {
                    int new_occupied=Occupied_no+1;
                    reference.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER_1).child("Occupied").setValue(new_occupied);
                    reference.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER_1).child("Is Empty").setValue("False");
                    reference.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER_1).child("Assigned To").child(Main_UID_HOSTLER).setValue("Assigned");
                    if(Occupied_no+1==size)
                    {
                        reference.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER_1).child("Status").setValue("Full");
                        Toast.makeText(View_Hostler_Details.this,"Room Is Now Full",Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(View_Hostler_Details.this,"Room Assigned",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(View_Hostler_Details.this,Mangement_Hostelers.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void update_Room_For_Hostler_Removed()
    {
        String Room_number_final=room_number.getText().toString().replace("Room ","");
        System.out.println(Room_number_final);
        reference.child("Users").child(user.getUid()).child("Rooms").child(Room_number_final).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        int Occupied_no=  snapshot.child("Occupied").getValue(Integer.class);
                        Occupied_no=Occupied_no-1;
                        reference.child("Users").child(user.getUid()).child("Rooms").child(Room_number_final).child("Occupied").setValue(Occupied_no);
                        reference.child("Users").child(user.getUid()).child("Rooms").child(Room_number_final).child("Is Empty").setValue("False");
                        reference.child("Users").child(user.getUid()).child("Rooms").child(Room_number_final).child("Status").setValue("Available");
                        reference.child("Users").child(user.getUid()).child("Rooms").child(Room_number_final).child("Assigned To").child(Main_UID_HOSTLER).removeValue();
                        if(Occupied_no==0)
                        {
                            reference.child("Users").child(user.getUid()).child("Rooms").child(Room_number_final).child("Is Empty").setValue("True");
                            Toast.makeText(View_Hostler_Details.this,"Room Is Now Empty",Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    public void Image_Picker(int requestCode)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri pic =data.getData();
            Picasso.get().load(pic).into(pan_pic);
            imageUpload_ProgressBar.setVisibility(View.VISIBLE);
            update_Pan_Aadhar(1,pic);
        }
        else if(requestCode==2 && resultCode==RESULT_OK)
        {
            Uri pic =data.getData();
            Picasso.get().load(pic).into(aadhar_pic);
            imageUpload_ProgressBar.setVisibility(View.VISIBLE);
            update_Pan_Aadhar(2,pic);
        }
    }

    public void update_Pan_Aadhar(int request_code,Uri pic_uri)
    {
        if(request_code==2)
        {
            storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Aadhaar")
                    .child("aadhaar_card.jpg").putFile(pic_uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                  storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Aadhaar")
                            .child("aadhaar_card.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                              @Override
                              public void onComplete(@NonNull Task<Uri> task) {
                                  reference.child("Users").child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Personal Details").child("Aadhaar").setValue(task.getResult().toString());
                                  Toast.makeText(View_Hostler_Details.this,"Aadhaar Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                  imageUpload_ProgressBar.setVisibility(View.INVISIBLE);
                              }
                          });

                }
            });
        }
        else if(request_code==1)
        {
            storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Pan")
                    .child("pan_card.jpg").putFile(pic_uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {
                            storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Pan")
                                    .child("pan_card.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            reference.child("Users").child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Personal Details").child("Pan").setValue(task.getResult().toString());
                                            Toast.makeText(View_Hostler_Details.this,"Pan Card Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                            imageUpload_ProgressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });

                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.view_hostlers_menu,menu);
        menu.findItem(R.id.edit_hostlers_detail).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item)
            {
                toolbar.setSubtitle("Edit Details");
                menu.findItem(R.id.edit_hostlers_detail).setVisible(false);
                menu.findItem(R.id.save_view_hostlers_menu).setVisible(true);
                enable_Edits();
                return false;
            }
        });

        menu.findItem(R.id.save_view_hostlers_menu).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                toolbar.setSubtitle("View");
                update_Details();
                menu.findItem(R.id.edit_hostlers_detail).setVisible(true);
                menu.findItem(R.id.save_view_hostlers_menu).setVisible(false);
                disable_Edits();
                return false;
            }
        });

        menu.findItem(R.id.delete_hostler_view_hostler).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item)
            {
                AlertDialog.Builder dialog=new AlertDialog.Builder(View_Hostler_Details.this);
                dialog.setTitle("Remove Hostler").setMessage("Are you sure you want to Remove "+Hostler_name+" ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                ///Remove Hostler
                                ///Code here
                               Toast.makeText(View_Hostler_Details.this,"Hostler Removed",Toast.LENGTH_SHORT).show();
                               reference.child("Users").child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).removeValue();
                               //Remove from Room ALso Code Here
                                if(!ROOM_NUMBER.equals("Not Assigned"))
                                {
                                    update_Room_For_Hostler_Removed();
                                }
                                // Remove From Storage
                                storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Aadhaar").child("aadhaar_card.jpg").delete();
                                storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Pan").child("pan_card.jpg").delete();
                                storageReference.child(user.getUid()).child("Hostlers").child(Main_UID_HOSTLER).child("Profile Picture").child("profile.jpg").delete();
                                storageReference.child(user.getUid()).child(Main_UID_HOSTLER).delete();
                                onBackPressed();
                            }
                        }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        }).create().show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void enable_Edits()
    {
        Address.setEnabled(true);
        CPD.setEnabled(true);
        college.setEnabled(true);
        father_phone_number.setEnabled(true);
        mother_phone_number.setEnabled(true);
    }
    private void disable_Edits()
    {
        Address.setEnabled(false);
        CPD.setEnabled(false);
        college.setEnabled(false);
        father_phone_number.setEnabled(false);
        mother_phone_number.setEnabled(false);
    }

    private void get_Details()
    {
        ///Get Details from Firebase
                 reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER)
                .child("Education Details")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String current_degree=snapshot.child("Current Degree").getValue(String.class);
                    String college_string=snapshot.child("College").getValue(String.class);
                    CPD.setText(current_degree);
                    college.setText(college_string);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER)
                .child("Parental Details").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            String father_name=snapshot.child("Father Name").getValue(String.class);
                            String mother_name=snapshot.child("Mother Name").getValue(String.class);
                            String father_phone_number=snapshot.child("Father Phone Number").getValue(String.class);
                            String mother_phone_number=snapshot.child("Mother Phone Number").getValue(String.class);
                            View_Hostler_Details.this.father_name.setText(father_name);
                            View_Hostler_Details.this.mother_name.setText(mother_name);
                            View_Hostler_Details.this.father_phone_number.setText(father_phone_number);
                            View_Hostler_Details.this.mother_phone_number.setText(mother_phone_number);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});

        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER)
                .child("Personal Details").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if (snapshot.exists())
                        {
                            String phone_number=snapshot.child("Phone Number").getValue(String.class);
                            String address=snapshot.child("Address").getValue(String.class);
                            String name=snapshot.child("Name").getValue(String.class);
                            Hostler_name=name;
                            toolbar.setTitle(name);
                            toolbar.setSubtitle("View");
                            String pic_profile=snapshot.child("Profile Picture").getValue(String.class);
                            View_Hostler_Details.this.name.setText(name);
                            View_Hostler_Details.this.phone_number.setText(phone_number);
                            View_Hostler_Details.this.Address.setText(address);
                            Picasso.get().load(pic_profile).into(pic);

                            String aadhar_pic_string=snapshot.child("Aadhaar").getValue(String.class);
                            String pan_pic_string=snapshot.child("Pan").getValue(String.class);
                            if (pan_pic_string != null )
                            {
                                aadhar_pic.setClickable(true);
                                Picasso.get().load(pan_pic_string).into(pan_pic);
                                pan_pic.setClickable(false);
                                aadhar_pic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Image_Picker(2);
                                    }
                                });
                            }
                            else if (aadhar_pic_string!=null)
                            {
                                pan_pic.setClickable(true);
                                Picasso.get().load(aadhar_pic_string).into(aadhar_pic);
                                aadhar_pic.setClickable(false);
                                pan_pic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Image_Picker(1);
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
    }
    private void getRoomsData()
    {
        reference.child("Users").child(user.getUid()).child("Rooms")
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        room.clear();
                        room.add(0,"Select a Room to Assign");
                        for (DataSnapshot snap : dataSnapshot.getChildren())
                        {
                            String room = snap.getKey();
                            String status = snap.child("Status").getValue(String.class);
                            if (status.equals("Available"))
                            {
                                View_Hostler_Details.this.room.add(room);
                            }
                        }
                        room_Mates_listAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {}
                });
    }
    private void update_Details()
    {
        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER).child("Education Details")
                .child("College").setValue(college.getText().toString());
        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER).child("Education Details")
                .child("Current Degree").setValue(CPD.getText().toString());

        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER).child("Personal Details")
                .child("Address").setValue(Address.getText().toString());

        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER).child("Parental Details")
                .child("Father Phone Number").setValue(father_phone_number.getText().toString());

        reference.child("Users")
                .child(user.getUid())
                .child("Hostlers")
                .child(Main_UID_HOSTLER).child("Parental Details")
                .child("Mother Phone Number").setValue(mother_phone_number.getText().toString());
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}