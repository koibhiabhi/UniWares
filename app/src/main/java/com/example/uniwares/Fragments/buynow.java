package com.example.uniwares.Fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.uniwares.Models.OrderModel;
import com.example.uniwares.NotificationHelper;
import com.example.uniwares.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class buynow extends Fragment implements PaymentResultListener {

    private DatabaseReference userRef;
    private TextView addressTextView;

    private CardView cashond;
    private TextView codegen;
    private EditText coderead;
    private int tempCode;

    private TextView amt;


    private String productTitle;
    private String sellerName;
    private String imageUrl;
    private String price;

    String sellerUid;

    private HashMap<String, OrderModel> orderMap = new HashMap<>();






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buynow, container, false);

        CardView cardView7 = view.findViewById(R.id.cardView7);
        CardView Address = view.findViewById(R.id.Address);
        CardView donate = view.findViewById(R.id.donate);
        CardView payment = view.findViewById(R.id.payment);
        Button placeOrder = view.findViewById(R.id.placeorder);

        cashond = view.findViewById(R.id.cashond);
        codegen = view.findViewById(R.id.codegen);
        coderead = view.findViewById(R.id.coderead);
        amt = view.findViewById(R.id.totalsum);

        RadioButton cod1 = view.findViewById(R.id.cod);


        productTitle = "";





        // Initialize views
        addressTextView = view.findViewById(R.id.address);

        // Firebase database reference to fetch user details
        userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid());

        // Fetch and display user address
        fetchUserAddress();

        // Retrieve arguments
        Bundle args = getArguments();
        if (args != null && args.getBoolean("isBuyNowClicked", false)) {
            String title = args.getString("title");

            sellerUid = args.getString("uid");
            String username = args.getString("username");
            imageUrl = args.getString("imageUrl");
            sellerName = args.getString("username");
            String category = args.getString("category");
            String brand = args.getString("brand");
            String status = args.getString("status");
            long timestamp = args.getLong("timestamp");
            productTitle = args.getString("title");
            price = args.getString("price");


            // Populate the UI with the ad details
            TextView titleTextView = view.findViewById(R.id.title);
            titleTextView.setText(title);

            TextView priceTextView = view.findViewById(R.id.price);
            priceTextView.setText(price);

            TextView sellerTextView = view.findViewById(R.id.sellerName);
            sellerTextView.setText("Sold By: " + username);

            ImageView imageView = view.findViewById(R.id.postadImage);
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.logo)
                    .into(imageView);

            TextView amount = view.findViewById(R.id.totalsum);
            amount.setText("Rs. " + price);

            // Change the color of views v1 and bagtab
            View v1 = view.findViewById(R.id.v1);
            v1.setBackgroundColor(Color.parseColor("#FF004D40")); // Very dark green color

            RadioButton bagTab = view.findViewById(R.id.bagtab);
            bagTab.setChecked(true);
            bagTab.setTextColor(Color.parseColor("#FF004D40")); // Very dark green color
            bagTab.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF004D40"))); // Very dark green color
        }



        CardView donateCard = view.findViewById(R.id.donatefrag);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                donateCard.setVisibility(View.VISIBLE);
                donateCard.animate().alpha(1f).setDuration(5500).start(); // Fade-in animation
            }
        }, 1000); // Delay 1 second (1000 milliseconds)

        donateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the donation fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new donate_frag());
                transaction.addToBackStack(null); // Optional, to add the transaction to the back stack
                transaction.commit();
            }
        });

        View v1 = view.findViewById(R.id.v1);
        View v2 = view.findViewById(R.id.v2);
        RadioButton bagtab = view.findViewById(R.id.bagtab);
        RadioButton paymenttab = view.findViewById(R.id.paymenttab);
        int originalColor = paymenttab.getCurrentTextColor();



        // Set OnClickListener for the "Place Order" button
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the color of v1, v2, bagtab, and paymenttab
                v1.setBackgroundColor(Color.parseColor("#FF004D40"));
                v2.setBackgroundColor(Color.parseColor("#FF004D40"));
                bagtab.setTextColor(Color.parseColor("#FF004D40"));
                paymenttab.setTextColor(Color.parseColor("#FF004D40"));
                paymenttab.setChecked(true);
                paymenttab.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF004D40"))); // Very dark green color


                // Hide card views cardView7, Address, and donate
                cardView7.setVisibility(View.GONE);
                Address.setVisibility(View.GONE);
                donate.setVisibility(View.GONE);

                // Show cardView payment
                payment.setVisibility(View.VISIBLE);

                placeOrder.setText("Confirm Order");
            }
        });


        ((RadioGroup) view.findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bagtab) {
                    // Restore original color of bagtab and paymenttab
                    paymenttab.setTextColor(originalColor);
                    v2.setBackgroundColor(originalColor);


                    // Show card views cardView7, Address, and donate
                    cardView7.setVisibility(View.VISIBLE);
                    Address.setVisibility(View.VISIBLE);
                    donate.setVisibility(View.VISIBLE);
                    cashond.setVisibility(View.GONE);
                    placeOrder.setText("Place Order");

                    placeOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Change the color of v1, v2, bagtab, and paymenttab
                            v1.setBackgroundColor(Color.parseColor("#FF004D40"));
                            v2.setBackgroundColor(Color.parseColor("#FF004D40"));
                            bagtab.setTextColor(Color.parseColor("#FF004D40"));
                            paymenttab.setTextColor(Color.parseColor("#FF004D40"));
                            paymenttab.setChecked(true);
                            paymenttab.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF004D40"))); // Very dark green color


                            // Hide card views cardView7, Address, and donate
                            cardView7.setVisibility(View.GONE);
                            Address.setVisibility(View.GONE);
                            donate.setVisibility(View.GONE);

                            // Show cardView payment
                            payment.setVisibility(View.VISIBLE);

                            placeOrder.setText("Confirm Order");
                        }
                    });


                    // Hide cardView payment
                    payment.setVisibility(View.GONE);

                } else if (checkedId == R.id.paymenttab) {
                    // Change the color of bagtab and paymenttab
                    bagtab.setTextColor(Color.parseColor("#FF004D40"));
                    paymenttab.setTextColor(Color.parseColor("#FF004D40"));
                    paymenttab.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF004D40"))); // Very dark green color

                    placeOrder.setText("Confirm Order");
                    cod1.setChecked(false);








                    // Hide card views cardView7, Address, and donate
                    cardView7.setVisibility(View.GONE);
                    Address.setVisibility(View.GONE);
                    donate.setVisibility(View.GONE);

                    // Show cardView payment
                    payment.setVisibility(View.VISIBLE);
                }
            }
        });



        ((RadioGroup) view.findViewById(R.id.radioGroup1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cod) {
                    // Show cashond CardView
                    cashond.setVisibility(View.VISIBLE);
                    placeOrder.setText("Confirm Order");



                    // Generate a random 4-digit number for codegen
                    tempCode = generateRandomCode();
                    codegen.setText(String.valueOf(tempCode));


                    view.findViewById(R.id.placeorder).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the entered code from the EditText
                            String enteredCode = coderead.getText().toString().trim();

                            // Check if the entered code matches the generated code
                            if (enteredCode.equals(String.valueOf(tempCode))) {


                                // Show a toast message "Order confirmed"
                                Toast.makeText(getContext(), "Order confirmed", Toast.LENGTH_SHORT).show();
                                NotificationHelper notificationHelper = new NotificationHelper(getContext());
                                notificationHelper.sendNotificationToSeller(productTitle, sellerUid);

                                addOrderToMap();
                            } else {
                                // Show a toast message "Invalid code"
                                Toast.makeText(getContext(), "Invalid code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    // Hide cashond CardView
                    placeOrder.setText("Pay Online");


                    placeOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startPayment();
                        }
                    });
                    cashond.setVisibility(View.GONE);
                }
            }
        });





        return view;
    }


    private void addOrderToMap() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        OrderModel orderModel = new OrderModel(productTitle, price, sellerName, imageUrl, currentUserUid);

        String orderId = FirebaseDatabase.getInstance().getReference().child("Orders").push().getKey();
        orderMap.put(orderId, orderModel);

        // Save the order details to Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId).setValue(orderModel);
    }






    private int generateRandomCode() {
        // Generate a random 4-digit number
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }

    private void fetchUserAddress() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String address = dataSnapshot.child("address").getValue(String.class);
                    if (address != null && !address.isEmpty()) {
                        addressTextView.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logo);
        final Activity activity = requireActivity();
        //final Fragment fragment = this;


        try {
            String amountStr = amt.getText().toString().replaceAll("\\D+", "");
            int amount = Integer.parseInt(amountStr);
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name)); // Use getString() to get String resources
            options.put("description", "Payment for anything");
            options.put("send_sns_hash", true);
            options.put("allow rotation", false);
            options.put("currency", "INR");
            options.put("amount", amount * 100);

            JSONObject prefill = new JSONObject();
            prefill.put("Email", "test@gmail.com");
            prefill.put("contact", "7854236985");

            options.put("prefill", prefill);

            checkout.open(requireActivity(), options); // Use buynow.this instead of this

            addOrderToMap();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in Payment" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }


    }

    @Override
    public void onPaymentSuccess(String s) {



        Toast.makeText(getContext(), "Payment Success: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(), "Payment Error: " + s, Toast.LENGTH_SHORT).show();
    }


}





