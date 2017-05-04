package com.aarstrand.zindre.pokechecklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zindre on 29-Dec-16.
 */
public class PokeCheckListDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PokeCheckList.db";


    /*
    * Column 1: Number (int)
    * Column 2: Name (String)
    * Column 3: Gif/Image - path (String)
    */
    private static final String CREATE_TABLE_POKEMON =
            "CREATE TABLE " + PokeCheckListContract.Pokemon.TABLE_NAME + " (" +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER + " INTEGER PRIMARY KEY," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME + " TEXT," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_ROW + " INTEGER," +
                    PokeCheckListContract.Pokemon.COLOUMN_NAME_COL + " INTEGER)";

    /*
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

    private static final String DELETE_TABLE_POKEMON =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Pokemon.TABLE_NAME;

    private static final String DELETE_TABLE_CATCH =
            "DROP TABLE IF EXISTS " + PokeCheckListContract.Catch.TABLE_NAME;

    public PokeCheckListDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
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

    public void recreateDbs(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(DELETE_TABLE_CATCH);
        db.execSQL(DELETE_TABLE_POKEMON);

        db.execSQL(CREATE_TABLE_POKEMON);
        db.execSQL(CREATE_TABLE_CATCH);
    }

    public Cursor getPokemon(int number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME + " where Number="+number+"",null);
        return cursor;
    }
    public Cursor getAllPokemon(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PokeCheckListContract.Pokemon.TABLE_NAME,null);
        return cursor;
    }

    public long insertPokemon(String name, int number, int row, int col){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER,number);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME,name);

        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_ROW,row);
        values.put(PokeCheckListContract.Pokemon.COLOUMN_NAME_COL,col);

        long pokemonId = db.insert(PokeCheckListContract.Pokemon.TABLE_NAME,null, values);
        return pokemonId;
    }

    public long registerCatch(Catch c){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //assign values to ContentValues object from given Catch object
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER, c.getNumber());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_ATTEMPTS, c.getAttempts());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_GAME,c.getGame());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_LOCATION,c.getLocation());
        values.put(PokeCheckListContract.Catch.COLOUMN_NAME_ODDS,c.getOdds());
        //insert row into db
        long catchId = db.insert(PokeCheckListContract.Catch.TABLE_NAME,null,values);
        return catchId;
    }

    public Cursor getCaughtPokemon() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + PokeCheckListContract.Catch.TABLE_NAME,null);
        return cursor;
    }
}
