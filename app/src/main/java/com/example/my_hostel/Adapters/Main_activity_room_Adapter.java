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

import java.util.ArrayList;

public class Main_activity_room_Adapter extends RecyclerView.Adapter<Main_activity_room_Adapter.viewholder>
{
    private ArrayList<String> list;
    private Context context;
    private RecyclerView rv;
    public Main_activity_room_Adapter(Context context, ArrayList<String> list, RecyclerView rv)
    {
        this.context = context;
        this.list = list;
        this.rv = rv;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_room_rv_single_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position)
    {

            holder.room_name.setText(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent=new Intent(context, View_Room_Details.class);
                    intent.putExtra("Room_Uid",list.get(position));
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount()
    {
        if(list==null)
        {
            rv.setVisibility(View.GONE);
        }
        else
        {
            rv.setVisibility(View.VISIBLE);
        }
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder
    {
        private TextView room_name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            room_name=itemView.findViewById(R.id.textView39);
        }
    }

}


