package com.aarstrand.zindre.pokechecklist;

import android.provider.BaseColumns;

/**
 * Created by Zindre on 29-Dec-16.
 */
public final class PokeCheckListContract {
    private PokeCheckListContract(){}

    public static class Pokemon implements BaseColumns {
        public static String TABLE_NAME = "Pokémon";
        public static String COLOUMN_NAME_NUMBER = "Number";
        public static String COLOUMN_NAME_NAME = "Name";
        public static String COLOUMN_NAME_GIF_FILE_PATH = "gif";
    }
    public static class Catch implements BaseColumns{
        public static String TABLE_NAME = "Catch";
        public static String COLOUMN_NAME_NUMBER = "Number";
        public static String COLOUMN_NAME_ATTEMPTS = "Attempts";
        public static String COLOUMN_NAME_ODDS = "Odds";
        public static String COLOUMN_NAME_GAME = "Game";
        public static String COLOUMN_NAME_LOCATION = "Location";
    }
}
