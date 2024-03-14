package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.uniwares.R;

import java.util.ArrayList;
import java.util.Random;


public class donate_frag extends Fragment {


    public donate_frag() {
        // Required empty public constructor
    }


//    ConstraintLayout cl;
//
//    AnimationDrawable anidraw;

    private String[] quotes = {
            "The best way to find yourself is to lose yourself in the service of others. - Mahatma Gandhi",
            "No one has ever become poor by giving. - Anne Frank",
            "The meaning of life is to find your gift. The purpose of life is to give it away. - Pablo Picasso",
            "We make a living by what we get, but we make a life by what we give. - Winston Churchill",
            "The simplest acts of kindness are by far more powerful than a thousand heads bowing in prayer. - Mahatma Gandhi",
            "The greatest use of life is to spend it for something that will outlast it. - William James",
            "To give away money is an easy matter and in any man's power. But to decide to whom to give it, and how large and when, and for what purpose and how, is neither in every man's power nor an easy matter. - Aristotle",
            "The measure of life is not its duration, but its donation. - Peter Marshall",
            "We rise by lifting others. - Robert Ingersoll",
            "Don't wait for other people to be loving, giving, compassionate, grateful, forgiving, generous, or friendly... lead the way! - Steve Maraboli",
            "It's not how much we give but how much love we put into giving. - Mother Teresa",
            "The only gift is a portion of thyself. - Ralph Waldo Emerson",
            "I have found that among its other benefits, giving liberates the soul of the giver. - Maya Angelou",
            "The highest use of capital is not to make more money, but to make money do more for the betterment of life. - Henry Ford",
            "We make a living by what we get, but we make a life by what we give. - Winston Churchill",
            "The joy of giving lasts longer than the joy of receiving. - Unknown",
            "Every sunrise is an invitation for us to arise and brighten someone's day. - Richelle E. Goodrich",
            "Giving is not just about making a donation. It is about making a difference. - Kathy Calvin",
            "Kindness in giving creates love. - Lao Tzu",
            "The true meaning of life is to plant trees, under whose shade you do not expect to sit. - Nelson Henderson",
            "The value of a man resides in what he gives and not in what he is capable of receiving. - Albert Einstein",
            "You have not lived today until you have done something for someone who can never repay you. - John Bunyan",
            "The happiest people are not those getting more, but those giving more. - H. Jackson Brown Jr.",
            "Service to others is the rent you pay for your room here on earth. - Muhammad Ali",
            "A kind gesture can reach a wound that only compassion can heal. - Steve Maraboli"
    };



    TextView t1;

    Button b1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_donate_frag, container, false);


        t1 = view.findViewById(R.id.quote);

        Random random = new Random();
        String randomQuote = quotes[random.nextInt(quotes.length)];
        t1.setText(randomQuote);

        b1 = view.findViewById(R.id.donatebtn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new donation_details());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });






//        cl = view.findViewById(R.id.anibg);
//        anidraw = (AnimationDrawable) cl.getBackground();
//        anidraw.setEnterFadeDuration(2500);
//        anidraw.setExitFadeDuration(5000);
//        anidraw.start();

        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list

        imageList.add(new SlideModel(R.drawable.don, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.don2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.dondasasd, ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);





        return view;

    }
}