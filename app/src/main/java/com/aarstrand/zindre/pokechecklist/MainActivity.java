package com.aarstrand.zindre.pokechecklist;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private Button pokedexButton, caughtButton, progressButton, randomHuntButton;
    private PokeCheckListDbHelper dbHelper;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        setSupportActionBar(toolbar);

        pokedexButton = (Button)findViewById(R.id.button);
        caughtButton = (Button)findViewById(R.id.button2);
        progressButton = (Button)findViewById(R.id.button3);
        randomHuntButton = (Button)findViewById(R.id.button4);

        randomHuntButton.setEnabled(false);
        progressButton.setEnabled(false);
        pokedexButton.setEnabled(true);
        caughtButton.setEnabled(true);


        dbHelper = PokeCheckListDbHelper.getInstance(this);

        setButtonListeners();
    }

    private void setButtonListeners() {

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

                //todo: this is just for making testing easier. The line below will need to be removed at some point
                dbHelper.recreateDb();
                getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
                        .edit().putBoolean(getString(R.string.first),true).apply();
                //todo: uncomment the line below when testing is done
                //startActivity(collection);
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

    private class SetupOtherDbStuff extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if(dbHelper.getAllMethods().getCount()!= getResources().getStringArray(R.array.method_array).length){
                String[] methods = getResources().getStringArray(R.array.method_array);
                for (String method:methods) {
                    String[] s = method.split("-");
                    dbHelper.insertMethod(s[0],s[1]);
                }

            }if(dbHelper.getAllGames().getCount() != getResources().getStringArray(R.array.game_array).length){
                String[] games = getResources().getStringArray(R.array.game_array);
                for (String game:games){
                    String[] s = game.split("-");
                    dbHelper.insertGame(s[0],Integer.parseInt(s[1]));
                }
            }
            return null;
        }
    }
}
