package com.aarstrand.zindre.pokechecklist;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarstrand.zindre.pokechecklist.db.PokeCheckListContract;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.db.models.Catch;
import com.aarstrand.zindre.pokechecklist.db.models.Pokemon;
import com.aarstrand.zindre.pokechecklist.tools.Tools;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CatchActivity extends AppCompatActivity {

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

        Catch c = dbHelper.getCatch(id);
        String nick = c.getNickname();
        int nr = c.getNumber();
        if(nick.equals("")){
            Pokemon a = dbHelper.getPokemon(nr);
            nickname.setText(a.getName());
        }else{
            nickname.setText(nick);
        }

        encounters.setText(
                String.valueOf(c.getAttempts())
        );
        method.setText(
                c.getMethod()
        );
        game.setText(
                c.getGame()
        );
        number.setText(
                String.format("#%03d",nr)
        );

        img.setImageBitmap(
                c.getImage()
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
