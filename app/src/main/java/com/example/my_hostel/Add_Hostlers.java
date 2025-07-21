package com.example.my_hostel;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.Adapters.room_mates_list_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Hostlers extends AppCompatActivity
{
    ///Components
    private ScrollView scrollView;
    /// Personal Information
    private CircleImageView hostler_pic;
    private EditText hostler_name, hostler_address ,hostler_education_details_past, hostler_phone_number;
    ///Education Details
    private EditText hostler_current_degree, hostler_college;
    ///Parental Details
    private EditText hostler_father_name, hostler_mother_name, hostler_father_phone_number, hostler_mother_phone_number;
    /// Documents
    private ImageView aadhaar_image,pan_image;
    /// Other
    private TextView info;
    private ImageButton save_hostler_details_button;
    private ImageButton edit_hostler_details_button;
    private Button add_hosteler_final;
   private  ProgressBar progressBar;
    /// Uri of the Images
    Uri profile_pic_uri;
    Uri aadhaar_uri;
    Uri pan_uri;

    ///Assign the Room to the Hostler Components////
    private FrameLayout assign_room_layout;
    private TextView room_number;
    private TextView hostler_name_display;
    private Button assign_room_button;
    private Button assign_room_back_button;
    private Spinner room_list_spinner;
    private TextView room_details_display;
    private TextView room_status_display;
    private TextView room_capacity_display;
    private TextView room_type_display;
    private TextView room_shared_display;
    private TextView room_occupied_display;
    private RecyclerView rv_other_roommates_list;
    /////////////////////////////////////
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    private String uid;
    private ArrayList<String> room = new ArrayList<>();
    private ArrayAdapter<String> roomAdapter;
    private room_mates_list_Adapter room_Mates_listAdapter;
    private ArrayList<String> room_mates_id_list=new ArrayList<>();

    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_hostlers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ///Id's For Components
        ///Personal Information
        hostler_pic = findViewById(R.id.hostler_pic);
        hostler_name = findViewById(R.id.hostler_name);
        hostler_address = findViewById(R.id.hostler_address);
        hostler_education_details_past = findViewById(R.id.hostler_education_details);
        hostler_phone_number = findViewById(R.id.hostler_phone_number);
        ///Education Details
        hostler_current_degree = findViewById(R.id.hostler_current_degree);
        hostler_college = findViewById(R.id.hostler_college);
        ///Parental Details
        hostler_father_name = findViewById(R.id.hostler_father_name);
        hostler_mother_name = findViewById(R.id.hostler_mother_name);
        hostler_father_phone_number = findViewById(R.id.hostler_father_phone_number);
        hostler_mother_phone_number = findViewById(R.id.hostler_mother_phone_number);
        ///Documents
        aadhaar_image = findViewById(R.id.photo_aadhaar);
        pan_image = findViewById(R.id.photo_pan);
        ///Other
        info = findViewById(R.id.text_info_save);
        info.setText(R.string.add_hostlers_fill_all_details);
        save_hostler_details_button = findViewById(R.id.hostler_save_changes);
        edit_hostler_details_button=findViewById(R.id.button_edit_hostler_information);
        edit_hostler_details_button.setVisibility(View.GONE);
        add_hosteler_final = findViewById(R.id.add_button_hostler_main);
        add_hosteler_final.setVisibility(View.GONE);
        progressBar=findViewById(R.id.progress_upload_documents_hostler);
        progressBar.setVisibility(View.GONE);
        scrollView =findViewById(R.id.scroll_view);
        /////Room Assign Components
        assign_room_layout=findViewById(R.id.assign_room_layout);
        assign_room_layout.setVisibility(View.GONE);
        room_number=findViewById(R.id.textView43);
        hostler_name_display=findViewById(R.id.name_display_room_assingment_page);
        assign_room_button=findViewById(R.id.confirm_room);
        assign_room_back_button=findViewById(R.id.button4);
        room_list_spinner=findViewById(R.id.spinner);
        room_details_display=findViewById(R.id.text_view_Details);
        room_status_display=findViewById(R.id.room_status);
        room_capacity_display=findViewById(R.id.room_size);
        room_type_display=findViewById(R.id.room_type);
        room_occupied_display=findViewById(R.id.room_occupied_no);
        room_shared_display=findViewById(R.id.other_roommates);
        //Recycler View for Other Roommates
        rv_other_roommates_list=findViewById(R.id.rv_other_roommates_list);
        rv_other_roommates_list.setLayoutManager(new LinearLayoutManager(this));
        room_Mates_listAdapter=new room_mates_list_Adapter(this,room_mates_id_list);
        rv_other_roommates_list.setAdapter(room_Mates_listAdapter);
        /////Room Assign Components Handling
        room_number.setText("");
        ///Handle Spinner and its Items
        roomAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,room);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        room_list_spinner.setAdapter(roomAdapter);
        getRoomsData();
        ///Add Spinner Click handler
        room_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                room_number.setText(room_list_spinner.getSelectedItem().toString());
                room_mates_id_list.clear();
                getRoomDetails_For_RoomAssingment(room_list_spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }});
        ///HANDLE ADD HOSTLER FINAL BUTTON CLICK////////////////////////////////////////////////////////////////////////////////
        assign_room_back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Add_Hostlers.this,"Room Not Assigned",Toast.LENGTH_SHORT).show();
                reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Room").setValue("Not Assigned");
                reference.child("Users").child(user.getUid()).child("Rooms").child(room_number.getText().toString()).child("Is Empty").setValue("True");
                finish();
                startActivity(new Intent(Add_Hostlers.this,Mangement_Hostelers.class));
            }
        });
        assign_room_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                    reference.child("Users").child(user.getUid()).child("Rooms").child(room_list_spinner.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int size = snapshot.child("Size").getValue(Integer.class).intValue();
                            int Occupied_no = snapshot.child("Occupied").getValue(Integer.class).intValue();
                            if (Occupied_no != size) {
                                int new_occupied = Occupied_no + 1;
                                reference.child("Users").child(user.getUid()).child("Rooms").child(room_number.getText().toString()).child("Occupied").setValue((new_occupied));
                                reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Room").setValue(room_list_spinner.getSelectedItem().toString());
                                reference.child("Users").child(user.getUid()).child("Rooms").child(room_number.getText().toString()).child("Is Empty").setValue("False");
                                reference.child("Users").child(user.getUid()).child("Rooms").child(room_number.getText().toString()).child("Assigned To").child(uid).setValue("Assigned");
                                if (Occupied_no + 1 == size) {
                                    reference.child("Users").child(user.getUid()).child("Rooms").child(room_number.getText().toString()).child("Status").setValue("Full");
                                    Toast.makeText(Add_Hostlers.this, "Room Is Now Full", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(Add_Hostlers.this, "Room Assigned", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Add_Hostlers.this, Mangement_Hostelers.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

        });

        hostler_college.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                    scrollView.smoothScrollTo(0,hostler_college.getBottom());
            }
        });
        ///HANDLE SAVE Details BUTTON CLICK////////////////////////////////////////////////////////////////////////////////
        save_hostler_details_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handle_information(1);
            }
        });
        ///HANDLE EDIT Details BUTTON CLICK////////////////////////////////////////////////////////////////////////////////
        edit_hostler_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                enable_edit_texts();
                info.setText(R.string.add_hostlers_fill_all_details);
                edit_hostler_details_button.setVisibility(View.GONE);
                save_hostler_details_button.setVisibility(View.VISIBLE);
                add_hosteler_final.setVisibility(View.GONE);
            }
        });
        ///HANDLE Final ADD BUTTON CLICK////////////////////////////////////////////////////////////////////////////////
       add_hosteler_final.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               handle_information(2);
           }
       });
        ///HANDLE IMAGE PICKER/////////////////////////////////////////////////////////////////////////////////
        ///request_code = 1 Profile Pic
        ///request_code = 2 Aadhaar
        ///request_code = 3 Pan
        hostler_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ///Implement Image Picker Here
                ImagePicker(1);
            }
        });
        aadhaar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ///Implement Image Picker Here
                ImagePicker(2);
            }
        });
        pan_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ///Implement Image Picker Here
                ImagePicker(3);
            }
        });
        ////////////////////////////////////////////////////////////////
    }
    private void getRoomDetails_For_RoomAssingment(String roomName)
    {
        reference.child("Users").child(user.getUid()).child("Rooms").child(roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child("Status").getValue().toString();
                int size = snapshot.child("Size").getValue(Integer.class);
                String type = snapshot.child("Type").getValue().toString();
                String is_empty=snapshot.child("Is Empty").getValue().toString();
                int Occupied_no= snapshot.child("Occupied").getValue(Integer.class);
                room_status_display.setText("Status : " + status);
                room_capacity_display.setText("Size : " + size);
                room_type_display.setText("Type : " + type);
                room_details_display.setText("Room Details Of : " + roomName);
                room_occupied_display.setText("Occupied : " + Occupied_no);
                if(is_empty.equals("True"))
                {
                    room_shared_display.setText("Room is Empty");
                }
                else
                {
                    room_shared_display.setText("Room Shared with the Following Members :-");
                    for(DataSnapshot snap:snapshot.child("Assigned To").getChildren())
                    {
                        room_mates_id_list.add(snap.getKey());
                    }
                    room_Mates_listAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
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
                        for (DataSnapshot snap : dataSnapshot.getChildren())
                        {
                            String room = snap.getKey();
                            String status = snap.child("Status").getValue().toString();

                            if (status.equals("Available"))
                            {
                                Add_Hostlers.this.room.add(room);
                            }
                        }
                        roomAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
    }


    ///Handling Information
    private void handle_information(int type)
    {
        ///Type Determines the type of Handling Information
        /// 1 is for Saving the Information
        /// 2 is for Final Upload of Information to the Database when the add_final buton is clicked

        //If User Uploads Only one of the two Documents
        // Then let int type_document be determined
        // IF only Aadhaar Document is Uploaded then let int type_document be 1
        // IF only Pan Document is Uploaded then let int type_document be 2
        int type_document=0;
        ///Personal Information
        String name=hostler_name.getText().toString();
        String address=hostler_address.getText().toString();
        String education_details=hostler_education_details_past.getText().toString();
        String phone_number=hostler_phone_number.getText().toString();
        ///Education Details
        String current_degree=hostler_current_degree.getText().toString();
        String college=hostler_college.getText().toString();
        ///Parental Details
        String father_name=hostler_father_name.getText().toString();
        String mother_name=hostler_mother_name.getText().toString();
        String father_phone_number=hostler_father_phone_number.getText().toString();
        String mother_phone_number=hostler_mother_phone_number.getText().toString();

        //Perform further validation and save to Firebase Realtime Database here.
        if(type==1)
        {
            if (name.isEmpty() || address.isEmpty() || education_details.isEmpty() || phone_number.isEmpty() || current_degree.isEmpty() || college.isEmpty() || father_name.isEmpty() || mother_name.isEmpty() || father_phone_number.isEmpty() || mother_phone_number.isEmpty()) {
                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                info.setText(R.string.add_hostlers_fill_all_details);
            } else if (profile_pic_uri == null) {
                Toast.makeText(this, "Please Upload Profile Picture", Toast.LENGTH_SHORT).show();

            } else if (aadhaar_uri == null && pan_uri == null) {
                Toast.makeText(this, "Please Upload Either Aadhaar or Pan Card", Toast.LENGTH_SHORT).show();
            } else {
                info.setText(R.string.add_hostlers_confirm_details);
                edit_hostler_details_button.setVisibility(View.VISIBLE);
                save_hostler_details_button.setVisibility(View.GONE);
                add_hosteler_final.setVisibility(View.VISIBLE);
                disable_edit_texts();
                //If User Uploads Only one of the two Documents
                // Then let int type_document be determined
                // IF only Aadhaar Document is Uploaded then let int type_document be 1
                // IF only Pan Document is Uploaded then let int type_document be 2
                // If Both Documents are Uploaded then let int type_document be 3

            }
        }
        else if(type==2)
        {
            if (aadhaar_uri == null) {
                type_document = 2;
            } else if (pan_uri == null) {
                type_document = 1;
            } else
            {
                type_document = 3;
            }

            databaseEntry(name, address, education_details, phone_number, current_degree, college, father_name, mother_name, father_phone_number, mother_phone_number, type_document);
        }
    }
    private void databaseEntry(String name,String address,String education_details,String phone_number,String current_degree,String college,String father_name,String mother_name,String father_phone_number,String mother_phone_number,int type_document)
    {

        add_hosteler_final.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ///Entry to "Users/userid/Hostlers/uniqueid/Details_category/"

         uid=reference.child("Users").child(user.getUid()).child("Hostlers").push().getKey();
        ///Database Entries
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Name").setValue(name);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Address").setValue(address);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Phone Number").setValue(phone_number);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Education Details").child("Education Details").setValue(education_details);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Education Details").child("Current Degree").setValue(current_degree);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Education Details").child("College").setValue(college);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Parental Details").child("Father Name").setValue(father_name);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Parental Details").child("Mother Name").setValue(mother_name);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Parental Details").child("Father Phone Number").setValue(father_phone_number);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Parental Details").child("Mother Phone Number").setValue(mother_phone_number);
        reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Room").setValue("Not Assigned");
        //Pushing Image Data to Firebase Storage
        //Entry to "user_id/profile_picture"
        //Entry to "user_id/aadhaar"
        //Entry to "user_id/pan"

        //If User Uploads Only one of the two Documents
        // Then let int type_document be determined
        // IF only Aadhaar Document is Uploaded then let int type_document be 1
        // IF only Pan Document is Uploaded then let int type_document be 2
        // If Both Documents are Uploaded then let int type_document be 3
        if (type_document == 3)
        {
            //Both Documents are Uploaded
            storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Aadhaar").child("aadhaar_card.jpg").putFile(aadhaar_uri);
            storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Pan").child("pan_card.jpg").putFile(pan_uri);
        }
        else if (type_document == 1)
        {
            //Only Aadhaar Document is Uploaded
            storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Aadhaar").child("aadhaar_card.jpg").putFile(aadhaar_uri);
        }
        else if (type_document == 2)
        {
            //Only Pan Document is Uploaded
            storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Pan").child("pan_card.jpg").putFile(pan_uri);
        }
        else
        {
            Toast.makeText(Add_Hostlers.this, "No Documents are Uploaded", Toast.LENGTH_SHORT).show();
        }
        storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Profile").child("profile.jpg").putFile(profile_pic_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    // Then let int type_document be determined
                    // IF only Aadhaar Document is Uploaded then let int type_document be 1
                    // IF only Pan Document is Uploaded then let int type_document be 2
                    // If Both Documents are Uploaded then let int type_document be 3
                    if(type_document==3)
                    {
                        storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Aadhaar").child("aadhaar_card.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Aadhaar").setValue(uri.toString());
                            }
                        });
                        storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Pan").child("pan_card.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Pan").setValue(uri.toString());
                            }
                        });
                    }
                    else if(type_document==1)
                    {
                        storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Aadhaar").child("aadhaar_card.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Aadhaar").setValue(uri.toString());
                            }
                        });

                    }
                    else if(type_document==2)
                    {
                        storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Pan").child("pan_card.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Pan").setValue(uri.toString());
                            }
                        });
                    }
                    storageReference.child(user.getUid()).child("Hostlers").child(uid).child("Profile").child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").child("Profile Picture").setValue(uri.toString());
                        }
                    });

                    Toast.makeText(Add_Hostlers.this, "All Documents are Uploaded", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    assign_room_layout.setVisibility(View.VISIBLE);
                    reference.child("Users").child(user.getUid()).child("Hostlers").child(uid).child("Personal Details").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {

                           hostler_name_display.setText(snapshot.child("Name").getValue().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    scrollView.setVisibility(View.GONE);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(Add_Hostlers.this, "Error Uploading Documents", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                add_hosteler_final.setVisibility(View.GONE);
                save_hostler_details_button.setVisibility(View.VISIBLE);
                edit_hostler_details_button.setVisibility(View.GONE);
                info.setText(R.string.add_hostlers_fill_all_details);
                scrollView.setVisibility(View.VISIBLE);
                assign_room_layout.setVisibility(View.GONE);
            }
        });
        //If no Documents are Uploaded then int type_document remains 0
    }

    private void disable_edit_texts()
    {
        hostler_name.setEnabled(false);
        hostler_address.setEnabled(false);
        hostler_education_details_past.setEnabled(false);
        hostler_phone_number.setEnabled(false);
        hostler_current_degree.setEnabled(false);
        hostler_college.setEnabled(false);
        hostler_father_name.setEnabled(false);
        hostler_mother_name.setEnabled(false);
        hostler_father_phone_number.setEnabled(false);
        hostler_mother_phone_number.setEnabled(false);
        hostler_pic.setClickable(false);
        aadhaar_image.setClickable(false);
        pan_image.setClickable(false);
    }
    private void enable_edit_texts()
    {
        hostler_name.setEnabled(true);
        hostler_address.setEnabled(true);
        hostler_education_details_past.setEnabled(true);
        hostler_phone_number.setEnabled(true);
        hostler_current_degree.setEnabled(true);
        hostler_college.setEnabled(true);
        hostler_father_name.setEnabled(true);
        hostler_mother_name.setEnabled(true);
        hostler_father_phone_number.setEnabled(true);
        hostler_mother_phone_number.setEnabled(true);
        hostler_pic.setClickable(true);
        aadhaar_image.setClickable(true);
        pan_image.setClickable(true);

    }
    ///Image Picker//////////////////////////////////////////
    private void ImagePicker(int request_code)
    {
        ///request_code = 1 Profile Pic
        ///request_code = 2 Aadhaar
        ///request_code = 3 Pan
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,request_code);
    }
    ///Activity Result Method Implementation//////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        ///request_code = 1 Profile Pic
        ///request_code = 2 Aadhaar
        ///request_code = 3 Pan
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            profile_pic_uri=data.getData();
            hostler_pic.setImageURI(data.getData());
        }
        else if(requestCode==2&&resultCode==RESULT_OK&&data!=null)
        {
            aadhaar_uri=data.getData();
            aadhaar_image.setImageURI(data.getData());
        }
        else if(requestCode==3&&resultCode==RESULT_OK&&data!=null)
        {
            pan_uri=data.getData();
            pan_image.setImageURI(data.getData());
        }
    }
    @Override
    public void onBackPressed()
    {
        finish();  // finish current activity and go back to the previous activity.
        super.onBackPressed();
    }
}