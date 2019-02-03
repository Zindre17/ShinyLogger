package com.aarstrand.zindre.pokechecklist;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import com.aarstrand.zindre.pokechecklist.adapters.PokemonListAdapter;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.SimpleDivider;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PokedexActivity extends AppCompatActivity implements PokemonListAdapter.PokemonListListener,PokemonListAdapter.PokemonHolder.PokemonListener {

    public static final int REGISTER = 1;
    private RecyclerView pokemonListView;
    private PokemonListAdapter plvAdapter;
    private PokeCheckListDbHelper dbHelper;
    private SearchView searchView;
    private int filter;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);
        filter = 0;
        query = "";
        ((Spinner)findViewById(R.id.filter_spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String choice = ((TextView)view).getText().toString();
               if(choice.toLowerCase().equals("all")){
                   filter = 0;
               }else{
                   filter = Integer.parseInt(choice);
               }
               plvAdapter.update(query, filter);
           }
           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
        });
        pokemonListView = (RecyclerView) findViewById(R.id.pokemon_list);
        pokemonListView.setLayoutManager(new LinearLayoutManager(this));
        plvAdapter = new PokemonListAdapter(this);
        plvAdapter.setListener(this);
        pokemonListView.setAdapter(plvAdapter);
        setRecyclerViewScrollListener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.pokedex_toolbar);
        toolbar.setTitle(R.string.title_activity_pokedex);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                    plvAdapter.update(query,filter);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        plvAdapter.closeDbTransaction();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        plvAdapter.update(query,filter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REGISTER){
            if(resultCode== Activity.RESULT_OK) {
                Toast.makeText(this, String.format("Shiny %s registered!", data.getExtras().getString(getString(R.string.name))), Toast.LENGTH_SHORT).show();
                plvAdapter.updateCaughtList();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pokedex, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                plvAdapter.update(query,filter);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_filter:
                LinearLayout ll = (LinearLayout)findViewById(R.id.pokedex_filter);
                if(ll.getVisibility()==View.GONE){
                    ll.setVisibility(View.VISIBLE);
                }else{
                    ll.setVisibility(View.GONE);
                }
                return true;
            case R.id.action_search:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPokemonClick() {
        //todo: bytte activity til pokemonActivity
    }
}
