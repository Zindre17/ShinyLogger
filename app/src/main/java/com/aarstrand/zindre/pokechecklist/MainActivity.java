package com.aarstrand.zindre.pokechecklist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private Button pokedexButton, caughtButton, progressButton, randomHuntButton;
    private PokeCheckListDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        new SetupPokedex().execute();

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

    private class SetupPokedex extends AsyncTask<Void,Integer,Void>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //todo: decide if progressdialog is the right view here. if one clicks the view goes away, but the process does keep going. This might be counter intuitive
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.db_dialog));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        private void createAndFillInDB() {

            JSONArray pokemonArray = null;
            if (dbHelper.getAllPokemon().getCount() != 721 ){
                dbHelper.recreateDb();
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

                    int size = pokemonArray.length();
                    progressDialog.setMax(size);

                    for(int i=1; i <= size ;i++){
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
                        publishProgress(i);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbHelper.close();

            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            createAndFillInDB();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            pokedexButton.setEnabled(true);
            caughtButton.setEnabled(true);
            System.out.println("db-check");
            System.out.println(dbHelper.getAllPokemon().getCount());
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
