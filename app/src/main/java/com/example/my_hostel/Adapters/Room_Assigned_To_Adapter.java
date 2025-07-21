package com.example.my_hostel.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
public class Room_Assigned_To_Adapter extends RecyclerView.Adapter<Room_Assigned_To_Adapter.Room_Assigned_To_ViewHolder>
{
    private ArrayList<String> room_mates_id_list;
    private Context context;
    private String ROOM_NUMBER;
    private TextView textView75;
    DatabaseReference database= FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Room_Assigned_To_Adapter(ArrayList<String> room_mates_id_list, Context context, String ROOM_NUMBER, TextView textView75) {
        this.room_mates_id_list = room_mates_id_list;
        this.context = context;
        this.ROOM_NUMBER = ROOM_NUMBER;
        this.textView75 = textView75;
    }
    @NonNull
    @Override
    public Room_Assigned_To_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_room_assigned_to,parent,false);
        return new Room_Assigned_To_ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Room_Assigned_To_ViewHolder holder, int position)
    {
        database.child("Users").child(user.getUid()).child("Hostlers").child(room_mates_id_list.get(position))
                .child("Personal Details")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            holder.name.setText(snapshot.child("Name").getValue(String.class));
                            Picasso.get().load(snapshot.child("Profile Picture").getValue(String.class)).into(holder.profile_image);
                            holder.remove_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                                    dialog.setMessage("Are you sure you want to remove this hostler from this room ?")
                                            .setTitle("Remove "+holder.name.getText().toString())
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    database.child("Users").child(user.getUid()).child("Rooms")
                                                            .child(ROOM_NUMBER)
                                                            .child("Assigned To")
                                                            .child(room_mates_id_list.get(position))
                                                            .removeValue();
                                                    database.child("Users").child(user.getUid()).child("Hostlers")
                                                            .child(room_mates_id_list.get(position))
                                                            .child("Room").setValue("Not Assigned");
                                                    updateDatabase();
                                                    room_mates_id_list.remove(room_mates_id_list.get(position));
                                                    notifyDataSetChanged();
                                                }
                                            }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    dialog.dismiss();
                                                }
                                            }).create().show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}});
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, View_Hostler_Details.class);
                intent.putExtra("Hostler_Uid",room_mates_id_list.get(position));
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(room_mates_id_list.isEmpty())
        {
            textView75.setText("Room Is Not Assigned to Anyone");
        }
        else
        {
            textView75.setText("Assigned To : "+room_mates_id_list.size()+" Hostler");
        }
        return room_mates_id_list.size();
    }
    public void updateDatabase()
    {
        database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int Occupied_no = snapshot.child("Occupied").getValue(Integer.class);
                int new_occupied = Occupied_no-1;
                if (new_occupied != 0)
                {
                    database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Occupied").setValue(new_occupied);
                    database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Is Empty").setValue("False");
                }
                if(new_occupied == 0)
                {
                    database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Occupied").setValue(0);
                    database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Is Empty").setValue("True");
                }
                database.child("Users").child(user.getUid()).child("Rooms").child(ROOM_NUMBER).child("Status").setValue("Available");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    class Room_Assigned_To_ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView profile_image;
        private TextView name;
        private Button remove_button;
        public Room_Assigned_To_ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.single_room_image);
            name = itemView.findViewById(R.id.single_room_assigned_to_name);
            remove_button = itemView.findViewById(R.id.remove_from_room_button);
        }
    }
}
