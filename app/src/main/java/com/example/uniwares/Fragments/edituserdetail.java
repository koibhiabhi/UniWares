package com.example.uniwares.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class edituserdetail extends Fragment {



    public edituserdetail() {
        // Required empty public constructor
    }


    EditText nname, nphone, nadd;
    Button save;

    FirebaseDatabase db;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edituserdetail, container, false);

        nname = view.findViewById(R.id.neweditName);
        nphone = view.findViewById(R.id.neweditph);
        nadd = view.findViewById(R.id.neweditaddress);
        save = view.findViewById(R.id.savebtn);
        db = FirebaseDatabase.getInstance();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newname = nname.getText().toString();
                String newphone = nphone.getText().toString();
                String newaddress = nadd.getText().toString();

                HashMap<String, Object> obj = new HashMap<>();

                if (!newname.isEmpty()) {
                    obj.put("username", newname);
                }

                if (!newphone.isEmpty()) {
                    obj.put("phonenumber", newphone);
                }

                if (!newaddress.isEmpty()) {
                    obj.put("address", newaddress);
                }

                if (obj.isEmpty()) {
                    Toast.makeText(getContext(), "No changes to update", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj);

                Toast.makeText(getContext(), "Details Updated", Toast.LENGTH_SHORT).show();
            }
        });





        return view;
    }
}