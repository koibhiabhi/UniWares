package com.example.uniwares.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uniwares.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class account_frag extends Fragment {


    public account_frag() {
        // Required empty public constructor
    }


    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;
    ImageView propic, addpropic;

    Button editdetails;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        editdetails = view.findViewById(R.id.editbtn);

        addpropic = view.findViewById(R.id.addprofileimg);

        propic = view.findViewById(R.id.profileimg);

        TextView nameTextView = view.findViewById(R.id.name);
        TextView emailTextView = view.findViewById(R.id.mail);
        TextView phoneTextView = view.findViewById(R.id.pho);
        TextView addressTextView = view.findViewById(R.id.add);
        TextView emproTextView = view.findViewById(R.id.empro);
        TextView nameBig = view.findViewById(R.id.namebold);


        editdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new edituserdetail());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        db.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //db se data get kena
                        String name = snapshot.child("username").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phonenumber").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);

                        nameTextView.setText(name != null ? name : "Null!");
                        emailTextView.setText(email != null ? email : "Null!");
                        phoneTextView.setText(phone != null ? phone : "Null!");
                        addressTextView.setText(address != null ? address : "Null!");
                        emproTextView.setText(email != null ? email : "Null!");
                        nameBig.setText(name != null ? name : "Null!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch user information: " + error.getMessage());
                        Toast.makeText(getContext(), "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                    }
                });






        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //taking image in the setting, isse setting mein bhi dikhegi image
                                String profilePicUrl = snapshot.child("profilepic").getValue(String.class);

                                Picasso.get()
                                        .load(profilePicUrl)
                                        .placeholder(R.drawable.avatar)
                                        .into(propic);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        addpropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //yeh ek intent banayega gallery mein se images laane ke liye
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });













        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getData() != null){
            Uri sFile = data.getData();
            propic.setImageURI(sFile);


            //this will help to create a folder of images in the firebase storage database
            final StorageReference reference = storage.getReference().child("profile pictures")
                    .child(auth.getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Uploaded",Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // yeh humara image ko storage se realtime database mein bhejega waha ek child node banakar profielpic naam ka
                            // jisko we can use further to show in the chatting section and in the setting section
                            db.getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getUid()).child("profilepic")
                                    .setValue(uri.toString());

                            Toast.makeText(getContext(), "Profile pic Updated",Toast.LENGTH_SHORT).show();


                        }
                    });




                }
            });

        }

    }

}