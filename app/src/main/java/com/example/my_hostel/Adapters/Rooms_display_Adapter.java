package com.example.my_hostel.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.R;
import com.example.my_hostel.View_Room_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Rooms_display_Adapter extends RecyclerView.Adapter<Rooms_display_Adapter.Room_Viewholder>
{
    private Context context;
    private ArrayList<String> room_uid_list=new ArrayList<>();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    public Rooms_display_Adapter(Context context, ArrayList<String> room_uid_list)
    {
        this.context = context;
        this.room_uid_list = room_uid_list;
    }
    @NonNull
    @Override
    public Room_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_room,parent,false);
        return new Room_Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Room_Viewholder holder, int position)
    {



            reference.child("Users").child(user.getUid()).child("Rooms").child(room_uid_list.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        holder.room_name.setText(room_uid_list.get(position));
                        holder.room_status.setText("Status : " + snapshot.child("Status").getValue().toString());
                        holder.room_size.setText("Size : " + snapshot.child("Size").getValue().toString());
                        holder.room_type.setText("Type : " + snapshot.child("Type").getValue().toString());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, View_Room_Details.class);
                    intent.putExtra("Room_Uid", room_uid_list.get(position));
                    context.startActivity(intent);
                }
            });

    }
    @Override
    public int getItemCount()
    {
        return room_uid_list.size();
    }

    class Room_Viewholder extends RecyclerView.ViewHolder
    {
        private TextView room_name,room_status,room_size,room_type;
        public Room_Viewholder(@NonNull View itemView) {
            super(itemView);
            room_name=itemView.findViewById(R.id.room_cv_room_name);
            room_status=itemView.findViewById(R.id.room_cv_status);
            room_size=itemView.findViewById(R.id.room_cv_size);
            room_type=itemView.findViewById(R.id.room_cv_type);
        }
    }


}
