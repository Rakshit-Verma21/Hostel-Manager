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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
public class Main_activity_hostler_Adapter extends RecyclerView.Adapter<Main_activity_hostler_Adapter.Hostler_ViewHolder>
{
    private Context context;
    private ArrayList<String> uid_hostlers;
    private RecyclerView rv;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    public Main_activity_hostler_Adapter(Context context, ArrayList<String> uid_hostlers, RecyclerView rv) {
        this.context = context;
        this.uid_hostlers = uid_hostlers;
        this.rv = rv;
    }
    @NonNull
    @Override
    public Hostler_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_hostler_main_activity,parent,false);
        return new Hostler_ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Hostler_ViewHolder holder, int position)
    {
        databaseReference.child("Users").child(firebaseUser.getUid())
                .child("Hostlers")
                .child(uid_hostlers.get(position))
                .child("Personal Details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            try
                            {
                                String name_hostler=snapshot.child("Name").getValue().toString();
                                holder.name.setText(name_hostler);
                                String profile_pic=snapshot.child("Profile Picture").getValue().toString();
                                Picasso.get().load(profile_pic).into(holder.profile_image);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, View_Hostler_Details.class);
                intent.putExtra("Hostler_Uid",uid_hostlers.get(position));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        if(uid_hostlers==null)
        {
            rv.setVisibility(View.GONE);
        }
        else
        {
            rv.setVisibility(View.VISIBLE);
        }
        return uid_hostlers.size();
    }
    class Hostler_ViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView profile_image;
        private TextView name;
        public Hostler_ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_pic_mainactivity_rv);
            name=itemView.findViewById(R.id.rv_hostlers_main_activity_name);
        }
    }
}
