package com.aarstrand.zindre.pokechecklist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarstrand.zindre.pokechecklist.db.PokeCheckListContract;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.Catch;
import com.aarstrand.zindre.pokechecklist.tools.Tools;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;


public class RegisterActivity extends AppCompatActivity {

    private TextView name,number,method,game,shiny,enc;
    private ImageView img;
    private Button register;
    private EditText encounters;
    private SwitchCompat shinyCharm;
    private int nr;
    private PokeCheckListDbHelper dbHelper;
    private ArrayAdapter<String> methods;
    private ArrayAdapter<String> games;
    private String pName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar)findViewById(R.id.register_toolbar);
        toolbar.setTitle(R.string.title_activity_register);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        nr = getIntent().getExtras().getInt(getString(R.string.number));

        dbHelper = PokeCheckListDbHelper.getInstance(getApplicationContext());
        Cursor pokemon = dbHelper.getPokemon(nr);
        pokemon.moveToFirst();

        name = (TextView)findViewById(R.id.register_name);
        number = (TextView)findViewById(R.id.register_number);
        method = (TextView)findViewById(R.id.register_method);
        game = (TextView)findViewById(R.id.register_game);
        img = (ImageView)findViewById(R.id.register_image);
        register = (Button)findViewById(R.id.register_button);
        encounters = (EditText)findViewById(R.id.register_edit_encounters);
        shiny = (TextView)findViewById(R.id.textView2);
        shinyCharm = (SwitchCompat)findViewById(R.id.register_switch);
        enc = (TextView)findViewById(R.id.register_encounters);

        pName = pokemon.getString(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME));
        name.setText(pName);
        number.setText(String.format("#%03d",nr));
        img.setImageBitmap(Tools.convertFromBlobToBitmap(pokemon.getBlob(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG))));

        String[] gamestrings = Tools.getAvailableGames(pokemon.getInt(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN)));
        games = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,gamestrings);

        game.setText(games.getItem(0));

        String[] m = Tools.getAvailableMethods(games.getItem(0));
        methods = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,m);

        method.setText(methods.getItem(0));

        method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableMethods();
            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableGames();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
                returnResults();
            }
        });

        shinyCharm.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.performClick();
                }
            }
        });

        encounters.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });

        evolutionCheck(method.getText().toString());
        shinyCharmCheck(game.getText().toString());
    }

    private void returnResults() {
        Intent i = new Intent();
        i.putExtra(getString(R.string.name),pName);
        setResult(Activity.RESULT_OK,i);
        onBackPressed();
    }

    private void register() {
        Catch c = new Catch();
        if(!encounters.getText().toString().equals("")){
            c.setAttempts(Integer.parseInt(encounters.getText().toString()));
        }else{
            c.setAttempts(0);
        }
        if(pName.equals(name.getText().toString())){
            c.setNickname("");
        }else{
            c.setNickname(name.getText().toString());
        }
        c.setGame(game.getText().toString());
        c.setNumber(nr);
        c.setOdds(shinyCharm.isChecked());
        c.setMethod(method.getText().toString());
        dbHelper.registerCatch(c);
    }

    private void evolutionCheck(String s) {
        if(s.toLowerCase().equals("evolution")){
            enc.setVisibility(View.GONE);
            encounters.setVisibility(View.GONE);
            shiny.setVisibility(View.GONE);
            shinyCharm.setVisibility(View.GONE);
        }else{
            enc.setVisibility(View.VISIBLE);
            encounters.setVisibility(View.VISIBLE);
            shiny.setVisibility(View.VISIBLE);
            shinyCharm.setVisibility(View.VISIBLE);
        }
    }

    private void shinyCharmCheck(String s) {
        if(Tools.isShinyCharmAvailable(s)){
            shiny.setVisibility(View.VISIBLE);
            shinyCharm.setVisibility(View.VISIBLE);
        }else{
            shinyCharm.setVisibility(View.GONE);
            shiny.setVisibility(View.GONE);
        }
    }

    private void showAvailableGames() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.register_choose_game);
        builder.setAdapter(games, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String g = games.getItem(which);
                game.setText(g);
                methods = makeNewAdapter(g);
                method.setText(methods.getItem(0));
                shinyCharmCheck(g);
            }
        });
        builder.create().show();
    }

    private ArrayAdapter<String> makeNewAdapter(String game) {
        return new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Tools.getAvailableMethods(game));
    }

    private void showAvailableMethods() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.register_choose_method);
        builder.setAdapter(methods, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m = methods.getItem(which);
                method.setText(m);
                evolutionCheck(m);
            }
        });
        builder.create().show();
    }
}
