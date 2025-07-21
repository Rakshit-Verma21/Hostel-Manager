package com.example.my_hostel;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reset_password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reset_password extends Fragment {

    private EditText email;
    private Button send_link; // Send Link Button
    private ImageButton close;// Close Fragment
    private TextView Timer_Display; // Timer to wait before Sending another Reset Link
    private CountDownTimer timer;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reset_password() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reset_password.
     */
    // TODO: Rename and change types and number of parameters
    public static Reset_password newInstance(String param1, String param2) {
        Reset_password fragment = new Reset_password();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reset_password, container, false);
        email=view.findViewById(R.id.email_reset_password);
        send_link=view.findViewById(R.id.send_link_reset_password);
        close=view.findViewById(R.id.close_fragment);
        Timer_Display=view.findViewById(R.id.timer);
        Timer_Display.setVisibility(View.GONE);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            ///Getting Email If user Had entered it During Email
            String email_text=bundle.getString("email");
            email.setText(email_text);
        }
        /// Close Fragment
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        /// Send Link Button
        send_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /// Send Link
                send_Reset_Link();
                Timer_Display.setVisibility(View.VISIBLE);
                start_timer(send_link,Timer_Display);
            }
        });
        return  view;
    }
    public void send_Reset_Link()
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        if(email.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        else
        {
            auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getActivity(), "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(getActivity(),"Error Occured",Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
    public void start_timer(Button send_link,TextView timer_Display)
    {
        /// Timer to wait before Sending another Reset Link
        new CountDownTimer(120000,1000)
        {

            @Override
            public void onTick(long millisUntilFinished)
            {
                send_link.setClickable(false);
                send_link.setText("Wait");
                String time_left=String.format(Locale.getDefault(),"%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timer_Display.setText(time_left);


            }

            @Override
            public void onFinish()
            {
                send_link.setClickable(true);
                send_link.setText("SEND");
                timer_Display.setVisibility(View.GONE);

            }
        }.start();
    }

}