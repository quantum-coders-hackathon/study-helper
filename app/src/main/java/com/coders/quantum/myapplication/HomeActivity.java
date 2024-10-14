package com.coders.quantum.myapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coders.quantum.myapplication.ui.main.SectionsPagerAdapter;
import com.coders.quantum.myapplication.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.star);  // Icon for Tab 1
        tabs.getTabAt(1).setIcon(R.drawable.home_icon);  // Icon for Tab 2
        tabs.getTabAt(2).setIcon(R.drawable.sketchbook);  // Icon for Tab 3
        tabs.getTabAt(3).setIcon(R.drawable.baseline_watch_later_24);  // Icon for Tab 4

        viewPager.setCurrentItem(1);

    }
}