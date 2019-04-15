package com.aarstrand.zindre.pokechecklist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toolbar;

import com.aarstrand.zindre.pokechecklist.db.models.Catch;
import com.aarstrand.zindre.pokechecklist.db.models.Pokemon;
import com.aarstrand.zindre.pokechecklist.tools.Tools;

import java.util.ArrayList;
import java.util.List;


public class PokeCheckListDbHelper extends SQLiteOpenHelper {

    private final SQLiteDatabase db;

    private boolean working;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PokeCheckList.db";

    public Cursor getCaughtNumbers() {
        return db.query(
                PokeCheckListContract.Catch.TABLE_NAME,
                new String[]{PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER},
                null,
                null,
                null,
                null,
                null
        );
    }

    public int getPokemonCount() {
        Cursor c = getAllPokemon();
        int a = c.getCount();
        c.close();
        return a;
    }

    public List<Pokemon> getAll(){
        Cursor c = getAllPokemon();
        List<Pokemon> l = new ArrayList<>(c.getCount());
        for(int i = 0; i < c.getCount(); i++){
            c.moveToPosition(i);
            Pokemon p = new Pokemon();
            p.setNumber(c.getInt(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER)));
            p.setName(c.getString(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
            p.setImage(Tools.convertFromBlobToBitmap(c.getBlob(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG))));
            p.setGeneration(c.getInt(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN)));
            l.add(p);
        }
        return l;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getGen(int number) {
        Cursor c = getPokemonCursor(number);
        c.moveToFirst();
        int i = c.getInt(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN));
        c.close();
        return i;
    }

    public Cursor getSearch(String newText) {
        return db.rawQuery(
                "select * from "+ PokeCheckListContract.Pokemon.TABLE_NAME+
                        " where " + PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME + " like "+"'"+newText+"%'",
                null
        );
    }

    public Cursor getFiltered(int i) {
        return db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME +
        " where " + PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN + " = " + i,null);
    }

    public Cursor getFilteredSearch(String query, int filter) {
        return db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME +
                " where " + PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN + " = " + filter +
                " and " + PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME + " like " + "'" + query + "%'", null);
    }

    public Cursor getFilteredSearch(int query, int filter) {
        return db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME +
        " where " + PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN + " = " + filter +
        " and " + PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER + " = " + query, null);
    }

    public Catch getCatch(Long id) {
        Cursor a = db.rawQuery("select * from " + PokeCheckListContract.Catch.TABLE_NAME +
        " where " + PokeCheckListContract.Catch._ID + " = " + id, null);
        a.moveToFirst();
        int nr = a.getInt(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER));
        Catch c = new Catch();
        c.setNickname(a.getString(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_NICKNAME)));
        c.setNumber(nr);
        c.set_id(a.getInt(a.getColumnIndex(PokeCheckListContract.Catch._ID)));
        c.setGame(a.getInt(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_GAME)));
        c.setAttempts(a.getInt(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_ATTEMPTS)));
        c.setChain(a.getInt(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_CHAIN)));
        c.setShinyCharm(a.getInt(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_SHINY))==1);
        c.setOdds(a.getDouble(a.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_ODDS)));
        c.setImage(getPokemonImage(nr));
        a.close();
        return c;
    }

    public Cursor getPokemonCursor(int i){
        return db.query(
                PokeCheckListContract.Pokemon.TABLE_NAME,
                null,
                PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER+" = ?",
                new String[]{String.valueOf(i)},
                null,
                null,
                null
        );
    }

    public Bitmap getPokemonImage(int i) {
        Cursor c = getPokemonCursor(i);
        c.moveToFirst();
        byte[] a = c.getBlob(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG));
        c.close();
        return Tools.convertFromBlobToBitmap(a);
    }

    public interface DbListener{

        void onDataChanged();
    }

    private DbListener listener;

    public DbListener getListener() {
        return listener;
    }

    public void setListener(DbListener listener) {
        this.listener = listener;
    }





    /**
     * SQL for å opprette pokemon-tabellen
    * Column 1: Number (int)
    * Column 2: Name (String)
    * Column 3: Image (Blob)
     * Column 5: Gen (int)
    */
    private static final String CREATE_TABLE_POKEMON =
            "CREATE TABLE " + PokeCheckListContract.Pokemon.TABLE_NAME + " (" +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER + " INTEGER PRIMARY KEY," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME + " TEXT," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG + " BLOB," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN + " INTEGER)";
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
                    PokeCheckListContract.Catch.COLOUMN_NAME_GAME + " INTEGER," +
                    PokeCheckListContract.Catch.COLOUMN_NAME_METHOD + " INTEGER," +
                    PokeCheckListContract.Catch.COLOUMN_NAME_NICKNAME + " TEXT,"+
                    PokeCheckListContract.Catch.COLOUMN_NAME_SHINY+ " INTEGER,"+
                    PokeCheckListContract.Catch.COLOUMN_NAME_CHAIN+ " INTEGER,"+
                    PokeCheckListContract.Catch.COLOUMN_NAME_ODDS + " INTEGER)";


    private static final String DELETE_TABLE_POKEMON =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Pokemon.TABLE_NAME;
    private static final String DELETE_TABLE_CATCH =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Catch.TABLE_NAME;


    private static PokeCheckListDbHelper instance;

    public synchronized static PokeCheckListDbHelper getInstance(Context context) {
        if(instance== null){
            instance = new PokeCheckListDbHelper(context);
        }
        return instance;
    }

    private PokeCheckListDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = this.getReadableDatabase();
        working = false;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_POKEMON);
        db.execSQL(CREATE_TABLE_CATCH);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //TODO: fix this and find out how/why
        db.execSQL(DELETE_TABLE_POKEMON);
        db.execSQL(DELETE_TABLE_CATCH);
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
    }



    /**
     * henter ut én spesifik pokemon ut fra databasen
     * @param number er nummeret til pokemonen man vil ha ut
     * @return returnerer et Pokemon-object
     */
    public Pokemon getPokemon(int number){
        Cursor a = getPokemonCursor(number);
        a.moveToFirst();
        Pokemon p = new Pokemon();
        p.setNumber(a.getInt(a.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER)));
        p.setName(a.getString(a.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
        p.setImage(Tools.convertFromBlobToBitmap(a.getBlob(a.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG))));
        p.setGeneration(a.getInt(a.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN)));
        a.close();
        return p;
    }

    /**
     * Denne funksjonen henter alle pokemonene ut av pokemontabellen i databasen
     * @return returnerer en Cursor
     */
    public Cursor getAllPokemon(){
        return db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME,null);
    }

    /**
     * Denne funskjonen brukes bare når pokedexen skal fylles inn for første gang, og bruker til å sette én rad i
     * databasen
     *
     * @param name er navnet på pokemonen
     * @param number er nummeret til pokemonen
     * @param gen er hvilken generasjon pokemonen hører til
     * @param image er et bytearray med et bilde av pokemonen
     */
    public void insertPokemon(String name, int number, int gen, byte[] image){
        ContentValues values = new ContentValues();
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER,number);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME,name);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_GEN, gen);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG,image);

        db.insert(PokeCheckListContract.Pokemon.TABLE_NAME,null, values);
        if(listener!=null){
            listener.onDataChanged();
        }
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
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_ODDS,c.getOdds());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_METHOD,c.getMethod());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_NICKNAME,c.getNickname());
        //insert row into db
        db.insert(PokeCheckListContract.Catch.TABLE_NAME,null,values);
        if (listener!=null){
            listener.onDataChanged();
        }
    }

    /**
     * henter ut alle fanga pokemon. Til bruk i "my shinies" viewet
     * @return returnrer en Cursor
     */
    public Cursor getCaughtPokemon() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + PokeCheckListContract.Catch.TABLE_NAME,null);

    }

}
