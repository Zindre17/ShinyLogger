package com.aarstrand.zindre.pokechecklist;

import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;

import java.sql.Blob;

/**
 * Created by Zindre on 29-Dec-16.
 */
public class PokemonAsListItem {
    public String name;
    public int number;
    public boolean caught;

    public String getName(){
        return name;
    }
    public boolean isCaught(){
        return caught;
    }
    public int getNumber(){
        return number;
    }

    public PokemonAsListItem(String name, int number) {
        this.name = name;
        this.number = number;
        //TODO: sjekk om pokemon er fanget og velg riktig icon til "icon"
    }

}
