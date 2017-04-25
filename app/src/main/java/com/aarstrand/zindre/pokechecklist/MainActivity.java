package com.aarstrand.zindre.pokechecklist;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button b1 = (Button)findViewById(R.id.button);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.button3);
        Button b4 = (Button)findViewById(R.id.button4);

        PokeCheckListDbHelper dbHelper = new PokeCheckListDbHelper(this);

        JSONArray pokemonArray = null;
        //Fylle inn databasen
        try {
            //JSONObject jsonObject = new JSONObject(getResources().getString(R.string.pokemons));
            pokemonArray = new JSONArray(getResources().getString(R.string.pokemons));
            for(int i=1; i <= pokemonArray.length();i++){
                dbHelper.insertPokemon(pokemonArray.getString(i-1),i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //#TODO: change view to pokedex view
                Intent pokedex = new Intent(MainActivity.this,PokedexActivity.class);
                startActivity(pokedex);

            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //#TODO: change view to a list of all caught shinies
            }
        });

        b3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //#TODO: change view to a random pokemon page (not in already caught list)
            }
        });

        b4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //#TODO: change view to something else
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
