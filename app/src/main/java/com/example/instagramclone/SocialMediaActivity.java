package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class SocialMediaActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Social Media APP");
        mToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(mToolbar);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mTabAdapter);

        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager, false);

    }
}
