package com.example.my_hostel.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.R;
import com.example.my_hostel.View_Hostler_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
public class ManagementHostlers_Display_Adapter extends RecyclerView.Adapter<ManagementHostlers_Display_Adapter.ManagementHostlers_Display_Adapter_ViewHolder> {
    private Context context;
    private ArrayList<String> hostler_uid_list;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public ManagementHostlers_Display_Adapter(Context context, ArrayList<String> hostler_uid_list) {
        this.context = context;
        this.hostler_uid_list = hostler_uid_list;
    }
    @NonNull
    @Override
    public ManagementHostlers_Display_Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view,parent,false);
        return new ManagementHostlers_Display_Adapter_ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ManagementHostlers_Display_Adapter_ViewHolder holder, int position)
    {
        reference.child("Users").child(user.getUid()).child("Hostlers").child(hostler_uid_list.get(position)).child("Personal Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                   holder.name.setText(snapshot.child("Name").getValue().toString());
                   Picasso.get().load(snapshot.child("Profile Picture").getValue().toString()).into(holder.profile_image);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
        reference.child("Users").child(user.getUid()).child("Hostlers").child(hostler_uid_list.get(position)).child("Education Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    holder.current_degree.setText(snapshot.child("Current Degree").getValue().toString());
                    holder.college.setText(snapshot.child("College").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       reference.child("Users").child(user.getUid()).child("Hostlers").child(hostler_uid_list.get(position)).addValueEventListener(new ValueEventListener()
       {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot)
           {
               if(snapshot.exists())
               {
                   if(snapshot.child("Room").getValue(String.class).equals(("Not Assigned")))
                   {
                       holder.Room_notSet_indicator.setVisibility(View.VISIBLE);
                       holder.room.setTextSize(8);
                   }
                   else
                   {
                       holder.Room_notSet_indicator.setVisibility(View.GONE);
                       holder.room.setTextSize(12);
                   }
               }
               holder.room.setText("Room "+snapshot.child("Room").getValue(String.class));
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }});
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               Intent intent = new Intent(context, View_Hostler_Details.class);
               intent.putExtra("Hostler_Uid",hostler_uid_list.get(position));
               context.startActivity(intent);
           }
       });
    }
    @Override
    public int getItemCount()
    {
        return hostler_uid_list.size();
    }


    class ManagementHostlers_Display_Adapter_ViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView profile_image;
        private TextView name,room,current_degree,college;
        private ImageButton Room_notSet_indicator;
        public ManagementHostlers_Display_Adapter_ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            profile_image = itemView.findViewById(R.id.card_h_image);
            name = itemView.findViewById(R.id.card_h_name);
            room = itemView.findViewById(R.id.card_h_room);
            current_degree = itemView.findViewById(R.id.card_h_current_degree);
            college = itemView.findViewById(R.id.card_h_college);
            Room_notSet_indicator = itemView.findViewById(R.id.imageButton_roomNot_Set);
        }
    }
}
