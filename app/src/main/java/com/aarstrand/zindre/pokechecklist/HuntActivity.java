package com.aarstrand.zindre.pokechecklist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


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
