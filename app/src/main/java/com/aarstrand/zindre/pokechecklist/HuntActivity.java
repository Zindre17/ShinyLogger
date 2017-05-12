package com.aarstrand.zindre.pokechecklist;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


public class HuntActivity extends FragmentActivity {

    public static final int NUMBER_OF_TABS = 2;

    ViewPager viewPager;
    HuntPagerAdapter huntPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);
        int i = getIntent().getExtras().getInt("number");
        viewPager = (ViewPager)findViewById(R.id.hunt_pager);
        huntPagerAdapter = new HuntPagerAdapter(getSupportFragmentManager(),i);
        viewPager.setAdapter(huntPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.hunt_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
