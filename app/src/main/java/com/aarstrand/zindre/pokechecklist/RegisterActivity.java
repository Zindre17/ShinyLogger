package com.aarstrand.zindre.pokechecklist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.*;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListContract;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.Catch;
import com.aarstrand.zindre.pokechecklist.tools.Tools;


public class RegisterActivity extends AppCompatActivity {

    private TextView name,number,method,game,shiny,enc;
    private ImageView img;
    private Button register;
    private EditText encounters;
    private SwitchCompat shinyCharm;
    private int nr;
    PokeCheckListDbHelper dbHelper;
    ArrayAdapter<String> methods;
    ArrayAdapter<String> games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        name.setText(pokemon.getString(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
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
                Catch c = new Catch();
                if(!encounters.getText().toString().equals("")){
                    c.setAttempts(Integer.parseInt(encounters.getText().toString()));
                }else{
                    c.setAttempts(0);
                }
                c.setGame(game.getText().toString());
                c.setNumber(nr);
                c.setOdds(shinyCharm.isChecked());
                c.setMethod(method.getText().toString());
                dbHelper.registerCatch(c);
                Intent i = new Intent();
                Cursor cur = dbHelper.getPokemon(nr);
                cur.moveToFirst();
                i.putExtra(getString(R.string.name),cur.getString(cur.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
                setResult(Activity.RESULT_OK,i);
                onBackPressed();
            }
        });

        shinyCharm.setOnFocusChangeListener(new SwitchCompat.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.performClick();
                }
            }
        });

        shinyCharmCheck(game.getText().toString());
        evolutionCheck(method.getText().toString());
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
