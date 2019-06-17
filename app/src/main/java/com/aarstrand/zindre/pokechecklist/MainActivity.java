package com.aarstrand.zindre.pokechecklist;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.old.Tools;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Button pokedexButton, huntButton, caughtButton, progressButton;
    private PokeCheckListDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.title_activity_main);
        setSupportActionBar(toolbar);

        pokedexButton = (Button)findViewById(R.id.button);
        huntButton = (Button)findViewById(R.id.button5);
        caughtButton = (Button)findViewById(R.id.button2);
        progressButton = (Button)findViewById(R.id.button3);

        progressButton.setEnabled(false);
        pokedexButton.setEnabled(true);
        caughtButton.setEnabled(true);
        dbHelper = PokeCheckListDbHelper.getInstance(this.getApplicationContext());

        setButtonListeners();
        if(dbHelper.getPokemonCount()!= Tools.DEX_COUNT && !dbHelper.isWorking()){
            new SetupPokedex(this.getApplicationContext()).execute(this);
        }
    }

    private void setButtonListeners() {

        pokedexButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent pokedex = new Intent(MainActivity.this,PokedexActivity.class);
                startActivity(pokedex);

            }
        });

        huntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hunt = new Intent(MainActivity.this,HuntActivity.class);
                startActivity(hunt);
            }
        });

        caughtButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent collection = new Intent(MainActivity.this,MyShiniesActivity.class);
                startActivity(collection);
            }
        });

        progressButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: change view to progressview
            }
        });

    }



    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("nå ble jeg også drept");
    }

    public static class SetupPokedex extends AsyncTask<Context,Integer,Void> {

        private String[] offsetList;
        private String[] genSwitchList;
        private JSONArray pokemonArray;
        private PokeCheckListDbHelper dbHelper;

        public SetupPokedex(Context context) {
            this.dbHelper = PokeCheckListDbHelper.getInstance(context);
            dbHelper.setWorking(true);
            try {
                pokemonArray = new JSONArray(context.getResources().getString(R.string.pokemons));
            }catch (Exception e) {
            }
            genSwitchList = context.getString(R.string.genswitch).trim().split(" ");
            offsetList = context.getString(R.string.image_skip_list).trim().split(" ");
        }

        private void createAndFillInDB(Context context) {

            Bitmap spriteSheet = BitmapFactory.decodeResource(
                    context.getApplicationContext().getResources(),
                    R.drawable.gen1);

            int row = 0;
            int col = 0;

            int genSwitchListPos = 0;
            int offsetListPos = 0;


            int nextOffset = Integer.parseInt(offsetList[offsetListPos].split("-")[0]);
            int genSwitchPoint = Integer.parseInt(genSwitchList[genSwitchListPos]);

            int size = pokemonArray.length();

            for(int dexNumber=1; dexNumber <= size ;dexNumber++){
                if(dexNumber == genSwitchPoint) {
                    col = 0;
                    row = 0;
                    genSwitchListPos++;
                    genSwitchPoint = Integer.parseInt(genSwitchList[genSwitchListPos]);
                    spriteSheet = switchSpriteSheet(context.getApplicationContext(),genSwitchListPos+1);

                }
                else if(dexNumber == nextOffset){

                    col += Integer.parseInt(offsetList[offsetListPos].split("-")[1]);
                    offsetListPos ++;
                    nextOffset = Integer.parseInt(offsetList[offsetListPos].split("-")[0]);
                }
                if(col>9){
                    row += Math.floor(col/10);
                    col = col%10;
                }try {
                    dbHelper.insertPokemon(
                            pokemonArray.getString(dexNumber - 1),
                            dexNumber,
                            genSwitchListPos+1,
                            getByteArrayImage(row,col,spriteSheet)
                    );
                }catch (Exception e) {
                    e.printStackTrace();
                }
                publishProgress(dexNumber);
                col++;
            }
        }

        private Bitmap switchSpriteSheet(Context context, int gen){
            switch (gen){
                case 1:
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.gen1);
                case 2:
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.gen2);
                case 3:
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.gen3);
                case 4:
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.gen4);
                case 5:
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.gen5);
                case 6:
                    return BitmapFactory.decodeResource(context.getResources(),R.drawable.gen6);
            }
            return null;
        }


        private byte[] getByteArrayImage(int row, int col, Bitmap bitmap) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int size = bitmap.getWidth() / 10;
            int left = (col) * size;
            int top = (row) * size;
            Bitmap pokemonThumbnail = Bitmap.createBitmap(bitmap,left,top,size,size);
            pokemonThumbnail.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }

        @Override
        protected Void doInBackground(Context... context) {
            createAndFillInDB(context[0]);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            dbHelper.setWorking(false);
            System.out.println("db-check");
            System.out.println(dbHelper.getAllPokemon().getCount());
        }
    }
}
