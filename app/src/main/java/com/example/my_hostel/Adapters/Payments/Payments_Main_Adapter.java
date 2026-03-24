package com.example.my_hostel.Adapters.Payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Payments_Main_Adapter extends RecyclerView.Adapter<Payments_Main_Adapter.Payments_Main_ViewHolder>
{
    Context context;
    ArrayList<String> hostler_uid_list;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public Payments_Main_Adapter(Context context, ArrayList<String> hostler_uid_list) {
        this.context = context;
        this.hostler_uid_list = hostler_uid_list;
    }

    @NonNull
    @Override
    public Payments_Main_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_payment_main_list_,parent,false);
        return new Payments_Main_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Payments_Main_ViewHolder holder, int position)
    {
        Calendar calendar = Calendar.getInstance();
        int monthIndex = calendar.get(Calendar.MONTH);
        String monthName = new DateFormatSymbols().getMonths()[monthIndex];
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        try
        {
            // Basic Details
            database.child("Users").child(user.getUid()).child("Hostlers").child(hostler_uid_list.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists()){
                        holder.name.setText(snapshot.child("Personal Details").child("Name").getValue().toString());
                        Picasso.get().load(snapshot.child("Personal Details").child("Profile Picture").getValue().toString()).into(holder.image);
                        String room = snapshot.child("Room").getValue().toString();
                        database.child("Users").child(user.getUid()).child("Rooms").child(room).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if(snapshot.exists())
                                {
                                    holder.room_number.setText("Room : " + room + " \nAmount Charged : ₹" + snapshot.child("Price").getValue().toString());
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}});
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}});

            // Payment Details
            database.child("Users").child(user.getUid()).child("Payments").child(year).child(monthName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot snap:snapshot.getChildren())
                    {
                        if(snap.getKey().equals(hostler_uid_list.get(position)))
                        {
                            holder.status.setText("Payment Status : " + snap.child("Status").getValue().toString());
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return hostler_uid_list.size();
    }

    public class Payments_Main_ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView image;
        TextView name,room_number,status;
        ImageButton status_button;

        Button view_button;

        public Payments_Main_ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image=itemView.findViewById(R.id.payment_main_rv_image);
            name=itemView.findViewById(R.id.payment_main_rv_name);
            room_number=itemView.findViewById(R.id.payment_main_rv_room_number);
            status=itemView.findViewById(R.id.payment_main_rv_tv_status);
            status_button=itemView.findViewById(R.id.payment_main_ib_status);
            view_button = itemView.findViewById(R.id.payment_main_button_view);

        }
    }
}

