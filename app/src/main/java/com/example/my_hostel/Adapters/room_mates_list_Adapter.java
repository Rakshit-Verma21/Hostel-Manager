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
import com.example.my_hostel.View_Hostler_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class room_mates_list_Adapter extends RecyclerView.Adapter<room_mates_list_Adapter.room_mates_list_Adapter_ViewHolder>
{
    private Context context;
    private ArrayList<String> room_mates_id_list=new ArrayList<String>();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private int count=1;

    public room_mates_list_Adapter(Context context, ArrayList<String> room_mates_id_list)
    {
        this.context = context;
        this.room_mates_id_list = room_mates_id_list;

    }
    @NonNull
    @Override
    public room_mates_list_Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_room_mate_item_layout,parent,false);
       return  new room_mates_list_Adapter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull room_mates_list_Adapter_ViewHolder holder, int position)
    {
       reference.child("Users").child(user.getUid()).child("Hostlers").child(room_mates_id_list.get(position)).child("Personal Details").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot)
           {
               holder.name.setText(count+". "+snapshot.child("Name").getValue().toString());
               count++;
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {}
       });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, View_Hostler_Details.class);
                intent.putExtra("uid",room_mates_id_list.get(position));
                context.startActivity(intent);
            }
        });
        count=1;
    }

    @Override
    public int getItemCount()
    {
        return room_mates_id_list.size();
    }

    class room_mates_list_Adapter_ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        public room_mates_list_Adapter_ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.name_room_mate_list);
        }
    }

}
