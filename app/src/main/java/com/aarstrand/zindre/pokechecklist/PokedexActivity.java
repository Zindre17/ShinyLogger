package com.aarstrand.zindre.pokechecklist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;


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
        setSupportActionBar(toolbar);
        pokemonListView.addItemDecoration(new SimpleDivider(this));


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
}
