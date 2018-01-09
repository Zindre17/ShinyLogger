package com.aarstrand.zindre.pokechecklist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListContract;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.Tools;

public class CatchActivity extends AppCompatActivity{

    private TextView nickname, number, method, game, encounters;
    private ImageButton editName, editMethod, editGame, editEncounters;
    private ImageView img;
    private PokeCheckListDbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        System.out.println("catch activity started");

        nickname = (TextView)findViewById(R.id.catch_name);
        method = (TextView)findViewById(R.id.catch_method);
        game = (TextView)findViewById(R.id.catch_game);
        encounters = (TextView)findViewById(R.id.catch_encounters);
        number = (TextView)findViewById(R.id.catch_number);

        editName = (ImageButton)findViewById(R.id.catch_edit_name);
        editGame = (ImageButton)findViewById(R.id.catch_edit_game);
        editMethod = (ImageButton)findViewById(R.id.catch_edit_method);
        editEncounters = (ImageButton)findViewById(R.id.catch_edit_encounters);

        img = (ImageView)findViewById(R.id.catch_img);

        dbHelper = PokeCheckListDbHelper.getInstance(this);

        Long id = getIntent().getLongExtra(getString(R.string.id),0);

        Cursor c = dbHelper.getCatch(id);
        c.moveToFirst();
        String nick = c.getString(c.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_NICKNAME));
        int nr = c.getInt(c.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER));
        if(nick.equals("")){
            Cursor a = dbHelper.getPokemon(nr);
            a.moveToFirst();
            nickname.setText(a.getString(a.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
        }else{
            nickname.setText(nick);
        }

        encounters.setText(
                String.valueOf(c.getInt(c.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_ATTEMPTS)))
        );
        method.setText(
                c.getString(c.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_METHOD))
        );
        game.setText(
                c.getString(c.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_GAME))
        );
        number.setText(
                String.format("#%03d",nr)
        );

        img.setImageBitmap(
                Tools.convertFromBlobToBitmap(
                        dbHelper.getPokemonImage(
                                Integer.parseInt(number.getText().toString().substring(1))
                        )
                )
        );
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
