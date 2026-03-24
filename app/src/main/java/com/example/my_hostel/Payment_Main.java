package com.example.my_hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_hostel.Adapters.Payments.Payments_Main_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class Payment_Main extends AppCompatActivity
{
    private TextView month , no_members_display;
    private Toolbar toolbar;
    private RecyclerView recyclerView_payments;

    private Payments_Main_Adapter paymentsMainAdapter;
     ArrayList<String> hostler_uid_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.payment_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        month = findViewById(R.id.payment_main_month_name);
        no_members_display = findViewById(R.id.payment_main_no_members_display);
        no_members_display.setVisibility(View.GONE);

        recyclerView_payments = findViewById(R.id.payment_main_recycler_view);
        getmonth();
        recyclerView_payments.setLayoutManager(new LinearLayoutManager(this));
        get_hostlers_list();
        paymentsMainAdapter = new Payments_Main_Adapter(Payment_Main.this, hostler_uid_list);
        recyclerView_payments.setAdapter(paymentsMainAdapter);





    }
    private void getmonth()
    {
        Calendar calendar = Calendar.getInstance(); // Get current date/time
        int monthIndex = calendar.get(Calendar.MONTH); // Month index (0 = January, 11 = December)
        String monthName = new DateFormatSymbols().getMonths()[monthIndex];
        month.setText(monthName);
    }
    private void get_hostlers_list()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
      try {
          reference.child("Users").child(user.getUid()).child("Hostlers").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.exists()) {
                      recyclerView_payments.setVisibility(View.VISIBLE);
                      no_members_display.setVisibility(View.GONE);
                      reference.child("Users").child(user.getUid()).child("Hostlers").addChildEventListener(new ChildEventListener() {
                          @Override
                          public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                              if (snapshot.exists()) {
                                  if (!hostler_uid_list.contains(snapshot.getKey())) {
                                      hostler_uid_list.add(snapshot.getKey());

                                  }

                              }
                              paymentsMainAdapter.notifyDataSetChanged();
                          }

                          @Override
                          public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                          }

                          @Override
                          public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                              hostler_uid_list.remove(snapshot.getKey());
                              paymentsMainAdapter.notifyDataSetChanged();
                          }

                          @Override
                          public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {
                          }
                      });
                  } else {
                      no_members_display.setVisibility(View.VISIBLE);
                      no_members_display.setText("Zero Hostelers");
                      recyclerView_payments.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

        }


    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}