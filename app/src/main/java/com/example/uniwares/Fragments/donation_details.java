package com.example.uniwares.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import com.example.uniwares.Adapters.InfinitePagerAdapter;
import com.example.uniwares.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class donation_details extends Fragment {

    RadioGroup categoryRadioGroup;
    EditText donerName, phoneNumber, pickupAddress, pickupDate, description;
    Button requestButton, addimg;

    FirebaseDatabase db;

    FirebaseStorage storage;

    FirebaseAuth auth;
    String uid;
    String timestamp;
    String donationId;
    String category;

    public donation_details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_details, container, false);

        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        categoryRadioGroup = view.findViewById(R.id.category_radio_group);
        donerName = view.findViewById(R.id.doner);
        phoneNumber = view.findViewById(R.id.pickphone);
        pickupAddress = view.findViewById(R.id.pickaddress);
        pickupDate = view.findViewById(R.id.pickdate);
        requestButton = view.findViewById(R.id.request);
        description = view.findViewById(R.id.description);
        addimg = view.findViewById(R.id.addimg);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        InfinitePagerAdapter adapter = new InfinitePagerAdapter(getContext(), viewPager);
        viewPager.setAdapter(adapter);


        uid = FirebaseAuth.getInstance().getUid();
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        donationId = uid + "_" + timestamp;

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Storing details, please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Handler().postDelayed(() -> {
                    progressDialog.dismiss();

                    if (categoryRadioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int selectedId = categoryRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = categoryRadioGroup.findViewById(selectedId);
                    category = selectedRadioButton.getText().toString();

                    String name = donerName.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    String address = pickupAddress.getText().toString();
                    String date = pickupDate.getText().toString();
                    String desc = description.getText().toString();

                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || date.isEmpty() || desc.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HashMap<String, Object> donation = new HashMap<>();
                    donation.put("donorName", name);
                    donation.put("phoneNumber", phone);
                    donation.put("pickupAddress", address);
                    donation.put("pickupDate", date);
                    donation.put("description", desc);

                    db.getReference("donations").child(category).child(donationId).setValue(donation)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Donation request submitted successfully, you will be contacted shortly", Toast.LENGTH_SHORT).show();
                                donerName.setText("");
                                phoneNumber.setText("");
                                pickupAddress.setText("");
                                pickupDate.setText("");
                                description.setText("");

                                int earnedCoins = (int) (Math.random() * 10); // Randomly assign coins between 0-10
                                updateCoins(earnedCoins);

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = requireActivity().getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
                                ImageView imageView = dialogView.findViewById(R.id.imageView);
                                TextView textView = dialogView.findViewById(R.id.textView);

                                imageView.setImageResource(R.drawable.thankyou);
                                textView.setText("Thank you for showing kindness & Congratulations!! You have earned " + earnedCoins + " coins for this donation.");

                                builder.setView(dialogView)
                                        .setPositiveButton("OK", null)
                                        .show();
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to submit donation details", Toast.LENGTH_SHORT).show());
                }, 3500);
            }
        });





        return view;
    }

    public void updateCoins(int coins) {
        DatabaseReference totalCoinsRef = db.getReference("rewards").child(auth.getUid()).child("totalCoins");
        totalCoinsRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer totalCoins = mutableData.getValue(Integer.class);
                if (totalCoins == null) {
                    mutableData.setValue(coins);
                } else {
                    mutableData.setValue(totalCoins + coins);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null) {

                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getData() != null){
            Uri sFile = data.getData();

            final StorageReference reference = storage.getReference().child("donation pictures")
                    .child(auth.getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Uploaded",Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.getReference().child("donations")
                                    .child(category).child(donationId)
                                    .child("image")
                                    .setValue(uri.toString());

                            Toast.makeText(getContext(), "image added",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}

