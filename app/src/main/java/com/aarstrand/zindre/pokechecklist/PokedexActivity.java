package com.aarstrand.zindre.pokechecklist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


public class PokedexActivity extends AppCompatActivity {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

}
