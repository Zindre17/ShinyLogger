package com.aarstrand.zindre.pokechecklist;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import org.json.JSONArray;


public class PokedexActivity extends AppCompatActivity implements PokemonListAdapter.PokemonListListener,PokemonListAdapter.PokemonHolder.PokemonListener {

    private RecyclerView pokemonListView;
    private PokemonListAdapter plvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);
        pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
        pokemonListView.setLayoutManager(new LinearLayoutManager(this));
        plvAdapter = new PokemonListAdapter(this);
        pokemonListView.setAdapter(plvAdapter);
        setRecyclerViewScrollListener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.pokedex_toolbar);
        toolbar.setTitle(R.string.title_activity_pokedex);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pokemonListView.addItemDecoration(new SimpleDivider(this));
        SharedPreferences sp = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        PokeCheckListDbHelper dbHelper = PokeCheckListDbHelper.getInstance(this);

        if(sp.getBoolean(getString(R.string.first),true)){
            SetupPokedex p = new SetupPokedex(this);
            p.execute();
            sp.edit().putBoolean(getString(R.string.first),false).apply();
        }

    }



    private void setRecyclerViewScrollListener() {
        pokemonListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("nå ble jeg drept");
        plvAdapter.closeDbTransaction();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void OnButtonClicked(int pos) {
        //todo: gå til hunt activity

        Intent hunt = new Intent(PokedexActivity.this,HuntActivity.class);
        hunt.putExtra("number",pos+1);
        startActivity(hunt);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("tb click");
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPokemonClick() {
        //todo: bytte activity til pokemonActivity
    }

    private class SetupPokedex extends AsyncTask<Void,Integer,Void> {

        private String[] offsetlist;
        private String[] genswitch;
        private JSONArray pokemonArray;
        private ProgressDialog progressDialog;
        private PokeCheckListDbHelper dbHelper;

        public SetupPokedex(Context context) {
            this.dbHelper = PokeCheckListDbHelper.getInstance(context);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.db_dialog));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            try {
                pokemonArray = new JSONArray(context.getResources().getString(R.string.pokemons));
            }catch (Exception e) {
            }
            genswitch = context.getString(R.string.genswitch).trim().split(" ");
            offsetlist = context.getString(R.string.image_skip_list).trim().split(" ");
        }

        @Override
        protected void onPreExecute() {
            //todo: decide if progressdialog is the right view here.
            //progressDialog.setCancelable(false);
            progressDialog.show();
        }

        private void createAndFillInDB() {
            //1 indexed matrix
            int row = 1;
            int col = 1;

            //new attempt:
            int genswitchpos = 0;
            int offsetpos = 0;


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
                }try {
                    dbHelper.insertPokemon(pokemonArray.getString(i - 1), i, row, col);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                publishProgress(i);
                col++;
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
            plvAdapter.refresh();
        }

        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            System.out.println("db-check");
            System.out.println(dbHelper.getAllPokemon().getCount());
        }
    }

}
