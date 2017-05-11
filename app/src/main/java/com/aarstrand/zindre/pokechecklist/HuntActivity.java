package com.aarstrand.zindre.pokechecklist;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


public class HuntActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("startet huntactivity");
        setContentView(R.layout.activity_hunt);
        PokeCheckListDbHelper dbHelper = new PokeCheckListDbHelper(this);
        Cursor c = dbHelper.getPokemon(getIntent().getExtras().getInt("number"));
        c.moveToFirst();
        ((ImageView) findViewById(R.id.hunt_image)).setImageBitmap(PokeCheckListDbHelper.convertFromBlobToBitmap(c.getBlob(PokeCheckListDbHelper.POKEMON_IMAGE)));
        ((TextView) findViewById(R.id.hunt_name)).setText(c.getString(PokeCheckListDbHelper.POKEMON_NAME));
        ((TextView) findViewById(R.id.hunt_number)).setText("#" + String.valueOf(c.getInt(PokeCheckListDbHelper.POKEMON_NUMBER)));
        c.close();
        dbHelper.close();
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
