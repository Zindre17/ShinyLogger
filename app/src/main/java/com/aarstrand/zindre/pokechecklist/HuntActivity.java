package com.aarstrand.zindre.pokechecklist;


import android.os.Bundle;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;


public class HuntActivity extends FragmentActivity {

    private TextView method,game;
    private RelativeLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        method = (TextView)findViewById(R.id.hunt_method);
        game = (TextView)findViewById(R.id.hunt_game);
        layout = (RelativeLayout)findViewById(R.id.hunt_layout);

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
