package com.aarstrand.zindre.pokechecklist;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import org.json.JSONArray;

public class PokedexActivity extends AppCompatActivity {
    private RecyclerView pokemonListView;
    private RecyclerView.Adapter plvAdapter;
    private RecyclerView.LayoutManager plvLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
        //pokemonListView.setHasFixedSize(true);
        pokemonListView.setLayoutManager(new LinearLayoutManager(this));

        //PokemonListAdapter = new PokemonListAdapter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /**public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()){
                case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
            return super.onOptionsItemSelected(item);
            }
        }**/
    }
}
