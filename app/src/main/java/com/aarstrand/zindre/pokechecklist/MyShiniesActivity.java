package com.aarstrand.zindre.pokechecklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.aarstrand.zindre.pokechecklist.adapters.GridViewAdapter;

public class MyShiniesActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shinies);

        gridView = (GridView)findViewById(R.id.my_shinies_grid);
        gridView.setAdapter(new GridViewAdapter(this));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo: make something happen
                System.out.println("hallo");
                System.out.println(gridView.getAdapter().getItemId(position));
                System.out.println(((GridViewAdapter)gridView.getAdapter()).getItemId(position));
                Intent intent = new Intent(MyShiniesActivity.this,CatchActivity.class);
                intent.putExtra(getString(R.string.id), gridView.getAdapter().getItemId(position));
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_shinies_toolbar);
        toolbar.setTitle(R.string.title_activity_my_shinies);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
