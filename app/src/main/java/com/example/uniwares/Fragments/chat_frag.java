package com.example.uniwares.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.uniwares.R;
import com.google.android.material.tabs.TabLayout;

public class chat_frag extends Fragment {

    private ViewPager p;

    public chat_frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_frag, container, false);

        TabLayout t = view.findViewById(R.id.tb1);
        p = view.findViewById(R.id.vp1);

        ViewPagerChatting msg = new ViewPagerChatting(getChildFragmentManager());
        p.setAdapter(msg);
        t.setupWithViewPager(p);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewPagerChatting msg = new ViewPagerChatting(getChildFragmentManager());
        p.setAdapter(msg);
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fm = getChildFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
