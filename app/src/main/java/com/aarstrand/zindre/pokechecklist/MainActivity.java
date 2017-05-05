package com.aarstrand.zindre.pokechecklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class MainActivity extends AppCompatActivity {

    private Button pokedexButton, caughtButton, progressButton, randomHuntButton;
    private PokeCheckListDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //todo: gjør slik at screenen med knappene kommer opp først og deretter en lag en progressbar for oppsett av db i en popup, og lag en sjekk for om den allerede er laget og er korrekt

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pokedexButton = (Button)findViewById(R.id.button);
        caughtButton = (Button)findViewById(R.id.button2);
        progressButton = (Button)findViewById(R.id.button3);
        randomHuntButton = (Button)findViewById(R.id.button4);

        randomHuntButton.setEnabled(false);
        progressButton.setEnabled(false);
        pokedexButton.setEnabled(false);
        caughtButton.setEnabled(false);

        dbHelper = new PokeCheckListDbHelper(this);

        //Fylle inn databasen
        createAndFillInDB();

        System.out.println("db-check");
        System.out.println(dbHelper.getAllPokemon().getCount());

        pokedexButton.setEnabled(true);

        pokedexButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent pokedex = new Intent(MainActivity.this,PokedexActivity.class);
                startActivity(pokedex);

            }
        });

        caughtButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent collection = new Intent(MainActivity.this,MyShiniesActivity.class);
                startActivity(collection);
            }
        });

        progressButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: change view to progressview
            }
        });

        randomHuntButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: change view to random hunt suggestion
            }
        });
    }

    private void createAndFillInDB() {

        JSONArray pokemonArray = null;
        dbHelper.recreateDbs();
        if (dbHelper.getAllPokemon().getCount() == 0 ){
            try {

                pokemonArray = new JSONArray(getResources().getString(R.string.pokemons));

                //1 indexed matrix
                int row = 1;
                int col = 1;

                //new attempt:
                int genswitchpos = 0;
                int offsetpos = 0;

                String[] genswitch = getResources().getString(R.string.genswitch).trim().split(" ");
                String[] offsetlist = getResources().getString(R.string.image_skip_list).trim().split(" ");

                int nextOffset = Integer.parseInt(offsetlist[offsetpos].split("-")[0]);
                int switchpoint = Integer.parseInt(genswitch[genswitchpos]);

                for(int i=1; i <= pokemonArray.length();i++){
                    if(i == switchpoint) {
                        col = 1;
                        row = 1;
                        genswitchpos++;
                        switchpoint = Integer.parseInt(genswitch[genswitchpos]);
                    }
                    else if(i == nextOffset){

                        col += Integer.parseInt(offsetlist[offsetpos].split("-")[1]);
                        offsetpos ++;
                        nextOffset = Integer.parseInt(offsetlist[offsetpos].split("-")[0]);
                        while(col >= 20){
                            row += (col/10)-1;
                            col -= 10;
                        }
                    }
                    if(col>10){
                        row ++;
                        col = col%10;
                    }
                    dbHelper.insertPokemon(pokemonArray.getString(i-1),i,row,col);
                    col ++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            dbHelper.close();

        }
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
        System.out.println("nå ble jeg også drept");
    }

}
