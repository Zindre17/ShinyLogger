package com.aarstrand.zindre.pokechecklist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Toast;
import com.aarstrand.zindre.pokechecklist.adapters.PokemonListAdapter;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.SimpleDivider;


public class PokedexActivity extends AppCompatActivity implements PokeCheckListDbHelper.DbListener,PokemonListAdapter.PokemonListListener,PokemonListAdapter.PokemonHolder.PokemonListener {

    public static final int REGISTER = 1;
    private RecyclerView pokemonListView;
    private PokemonListAdapter plvAdapter;
    private boolean dbChange;
    private PokeCheckListDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);
        pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
        pokemonListView.setLayoutManager(new LinearLayoutManager(this));
        plvAdapter = new PokemonListAdapter(this);
        plvAdapter.setListener(this);
        pokemonListView.setAdapter(plvAdapter);
        setRecyclerViewScrollListener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.pokedex_toolbar);
        toolbar.setTitle(R.string.title_activity_pokedex);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pokemonListView.addItemDecoration(new SimpleDivider(this));
        SharedPreferences sp = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        dbHelper = PokeCheckListDbHelper.getInstance(this);

    }



    private void setRecyclerViewScrollListener() {
        pokemonListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1) && dbHelper.isWorking()){
                    System.out.println("bunnen av lista og ikke ferdig");
                    ((PokemonListAdapter)recyclerView.getAdapter()).refresh();
                }
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
        plvAdapter.refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REGISTER){
            if(resultCode== Activity.RESULT_OK){
                Toast.makeText(this,String.format("Shiny %s registered!",data.getExtras().getString(getString(R.string.name))),Toast.LENGTH_SHORT).show();
            }else{
                System.out.println("no results");
            }
        }
    }

    @Override
    public void OnButtonClicked(int pos) {
        Intent register = new Intent(PokedexActivity.this,RegisterActivity.class);
        register.putExtra(getString(R.string.number),pos+1);
        startActivityForResult(register,REGISTER);
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

    @Override
    public void onDataChanged() {
        dbChange = true;
    }
}
