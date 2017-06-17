package com.aarstrand.zindre.pokechecklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


public class PokeCheckListDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PokeCheckList.db";
    private Context context;

    private Bitmap grid;

    private int size;
    /**
     * SQL for å opprette pokemon-tabellen
    * Column 1: Number (int)
    * Column 2: Name (String)
    * Column 3: Image (Blob)
     * Column 4: Caught (int /Boolean)
     * Column 5: Gen (int)
    */
    private static final String CREATE_TABLE_POKEMON =
            "CREATE TABLE " + PokeCheckListContract.Pokemon.TABLE_NAME + " (" +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER + " INTEGER PRIMARY KEY," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME + " TEXT," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG + " BLOB," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_CAUGHT + " INTEGER," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN + " INTEGER)";
    public static final int POKEMON_NUMBER =0;

    public static final int POKEMON_NAME =1;
    public static final int POKEMON_IMAGE =2;
    public static final int POKEMON_CAUGHT =3;
    public static final int POKEMON_GEN =4;
    /**
     * SQL for å opprette caught-tabellen
    * Column 1: ID (autoincrement int)
    * Column 2: Number (int)
    * Column 3: Attempts (int)
    * Column 4: Game (String)
    * Column 5: Location (String)
    * Column 6: Odds (String)
    */
    private static final String CREATE_TABLE_CATCH =
            "CREATE TABLE " + PokeCheckListContract.Catch.TABLE_NAME + " (" +
                    PokeCheckListContract.Catch._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER + " INTEGER," +
                    PokeCheckListContract.Catch.COLOUMN_NAME_ATTEMPTS + " INTEGER," +
                    PokeCheckListContract.Catch.COLOUMN_NAME_GAME + " TEXT," +
                    PokeCheckListContract.Catch.COLOUMN_NAME_LOCATION + " TEXT," +
                    PokeCheckListContract.Catch.COLOUMN_NAME_ODDS + " TEXT)";

    public static final int CAUGHT_ID =0;

    public static final int CAUGHT_NUMBER =1;
    public static final int CAUGHT_ATTEMPTS =2;
    public static final int CAUGHT_GAME =3;
    public static final int CAUGHT_LOCATION =4;
    public static final int CAUGHT_ODDS =5;
    private static final String CREATE_TABLE_METHOD =
            "CREATE TABLE " + PokeCheckListContract.Method.TABLE_NAME + " (" +
            PokeCheckListContract.Method._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PokeCheckListContract.Method.COLOUMN_NAME_METHOD + " TEXT,"+
            PokeCheckListContract.Method.COLOUMN_NAME_GENS + " TEXT)";


    private static final String CREATE_TABLE_GAME =
            "CREATE TABLE " + PokeCheckListContract.Game.TABLE_NAME + " (" +
            PokeCheckListContract.Game._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PokeCheckListContract.Game.COLOUMN_NAME_GAME + " TEXT," +
            PokeCheckListContract.Game.COLOUMN_NAME_GEN + " INTEGER)";

    public static final int GAME_GEN = 2;

    private static final String DELETE_TABLE_POKEMON =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Pokemon.TABLE_NAME;
    private static final String DELETE_TABLE_CATCH =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Catch.TABLE_NAME;
    private static final String DELETE_TABLE_GAME =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Game.TABLE_NAME;
    private static final String DELETE_TABLE_METHOD =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Method.TABLE_NAME;

    public PokeCheckListDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
        size = 0;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_POKEMON);
        db.execSQL(CREATE_TABLE_CATCH);
        db.execSQL(CREATE_TABLE_GAME);
        db.execSQL(CREATE_TABLE_METHOD);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //TODO: fix this and find out how/why
        db.execSQL(DELETE_TABLE_POKEMON);
        db.execSQL(DELETE_TABLE_CATCH);
        db.execSQL(DELETE_TABLE_GAME);
        db.execSQL(DELETE_TABLE_METHOD);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //TODO: implement this method too
    }

    /**
     * denne funksjonen brukes når man vil lage pokedexdatabasen på nytt.
     * Den sletter pokemontabellen og oppretter den på nytt
     */
    public void recreateDb(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DELETE_TABLE_POKEMON);
        db.execSQL(CREATE_TABLE_POKEMON);
        db.execSQL(DELETE_TABLE_METHOD);
        db.execSQL(CREATE_TABLE_METHOD);
        db.execSQL(DELETE_TABLE_GAME);
        db.execSQL(CREATE_TABLE_GAME);
    }

    public static Bitmap convertFromBlobToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob,0,blob.length);
    }

    /**
     * henter ut én spesifik pokemon ut fra databasen
     * @param number er nummeret til pokemonen man vil ha ut
     * @return returnerer en Cursor
     */
    public Cursor getPokemon(int number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME + " where Number="+number+"",null);
        return cursor;
    }

    /**
     * Denne funksjonen henter alle pokemonene ut av pokemontabellen i databasen
     * @return returnerer en Cursor
     */
    public Cursor getAllPokemon(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME,null);
        return cursor;
    }

    /**
     * Denne funskjonen brukes bare når pokedexen skal fylles inn for første gang, og bruker til å sette én rad i
     * databasen
     *
     * @param name er navnet på pokemonen
     * @param number er nummeret til pokemonen
     * @param row er hvilken rad på bildearket man finner dens bilde
     * @param col er hvilken kollonne man finner den bilde
     */
    public void insertPokemon(String name, int number, int row, int col, int gen){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER,number);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME,name);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN, gen);
        chechIfTimeToChangeGrid(number);

        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG,getByteArrayImage(row,col));
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_CAUGHT,0);

        db.insert(PokeCheckListContract.Pokemon.TABLE_NAME,null, values);
    }

    /** Denne funksjonen tar inn rad og kolonne til hvor spriten til pokemonen ligger på arket,
     * lager et nytt bitmap med bare pokemonen i den gridcellen, og til slutt converterer den png'en til en bytestream
     * som den returnerer
     */
    private byte[] getByteArrayImage(int row, int col) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if(size==0) {
            size = grid.getWidth() / 10;
        }
        int left = (col-1) * size;
        int top = (row-1) * size;
        Bitmap pokemonThumbnail = Bitmap.createBitmap(grid,left,top,size,size);
        pokemonThumbnail.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Denne funksjonen sjekker om det trengs å skifte bildeark
     *
     * @param number er nummeret til pokemonen som skal settes inn
     */
    private void chechIfTimeToChangeGrid(int number) {
        if(number == 1)
            grid = BitmapFactory.decodeResource(context.getResources(),R.drawable.gen1);
        if(number == 152)
            grid = BitmapFactory.decodeResource(context.getResources(),R.drawable.gen2);
        if(number == 252)
            grid = BitmapFactory.decodeResource(context.getResources(),R.drawable.gen3);
        if(number == 387)
            grid = BitmapFactory.decodeResource(context.getResources(),R.drawable.gen4);
        if(number == 494)
            grid = BitmapFactory.decodeResource(context.getResources(),R.drawable.gen5);
        if(number == 650)
            grid = BitmapFactory.decodeResource(context.getResources(),R.drawable.gen6);
    }

    /**
     * legger inn en fangst i caughttabellen
     * @param c tar inn et Catchobject som lages ut i fra et gui
     */
    public void registerCatch(Catch c){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //assign values to ContentValues object from given Catch object
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER, c.getNumber());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_ATTEMPTS, c.getAttempts());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_GAME,c.getGame());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_LOCATION,c.getLocation());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_ODDS,c.getOdds());
        //insert row into db
        db.insert(PokeCheckListContract.Catch.TABLE_NAME,null,values);
    }

    /**
     * henter ut alle fanga pokemon. Til bruk i "my shinies" viewet
     * @return returnrer en Cursor
     */
    public Cursor getCaughtPokemon() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PokeCheckListContract.Catch.TABLE_NAME,null);
        return cursor;
    }

    public void insertMethod(String method,String gens) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PokeCheckListContract.Method.COLOUMN_NAME_METHOD,method);
        values.put(PokeCheckListContract.Method.COLOUMN_NAME_GENS,gens);
        db.insert(PokeCheckListContract.Method.TABLE_NAME,null,values);
    }

    public Cursor getAllMethods(){
        return getReadableDatabase().rawQuery("select * from " + PokeCheckListContract.Method.TABLE_NAME,null);
    }

    public Cursor getMethods(int gen){
        return getWritableDatabase().rawQuery(
                "select * from " + PokeCheckListContract.Method.TABLE_NAME +
                        " where " + PokeCheckListContract.Method.COLOUMN_NAME_GENS + " like '%"+ String.valueOf(gen) +"%'" +
                        " or " + PokeCheckListContract.Method.COLOUMN_NAME_GENS+ " = 0",null);
    }

    public void insertGame(String game,int gen) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PokeCheckListContract.Game.COLOUMN_NAME_GAME,game);
        values.put(PokeCheckListContract.Game.COLOUMN_NAME_GEN,gen);
        db.insert(PokeCheckListContract.Game.TABLE_NAME,null,values);
    }

    public Cursor getGames(int gen){
        return getWritableDatabase().rawQuery(
                "select * from " + PokeCheckListContract.Game.TABLE_NAME +
                " where " + PokeCheckListContract.Game.COLOUMN_NAME_GEN + " >= "+gen+"",null);
    }

    public Cursor getAllGames(){
        return getReadableDatabase().rawQuery("select * from " + PokeCheckListContract.Game.TABLE_NAME,null);
    }
}
